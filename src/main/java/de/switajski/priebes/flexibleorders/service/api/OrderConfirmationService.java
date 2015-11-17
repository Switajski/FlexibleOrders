package de.switajski.priebes.flexibleorders.service.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class OrderConfirmationService {

    @Autowired
    private CatalogDeliveryMethodRepository deliveryMethodRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private OrderItemRepository orderItemRepo;


	
	@Transactional
    public OrderConfirmation confirm(ConfirmParameter confirmParameter) {
        Address invoiceAddress = confirmParameter.invoiceAddress;
        Address shippingAddress = confirmParameter.shippingAddress;
        validateConfirm(confirmParameter.orderNumber, confirmParameter.confirmNumber, confirmParameter.itemsToBeConfirmed, shippingAddress);

        Order order = orderRepo.findByOrderNumber(confirmParameter.orderNumber);
        if (order == null) throw new IllegalArgumentException("Bestellnr. nicht gefunden");

        Customer cust = order.getCustomer();
        Address address = (cust.getInvoiceAddress() == null) ? cust.getShippingAddress() : cust.getInvoiceAddress();
        shippingAddress = (shippingAddress.isComplete()) ? shippingAddress : address;
        invoiceAddress = (invoiceAddress.isComplete()) ? invoiceAddress : address;

        PurchaseAgreement pAgree = new PurchaseAgreement();
        pAgree.setShippingAddress(shippingAddress);
        pAgree.setInvoiceAddress(invoiceAddress);
        pAgree.setExpectedDelivery(confirmParameter.expectedDelivery);
        pAgree.setCustomerNumber(confirmParameter.customerNumber);
        pAgree.setPaymentConditions(confirmParameter.paymentConditions);
        if (confirmParameter.deliveryMethodNo != null) {
            CatalogDeliveryMethod catalogDeliveryMethod = deliveryMethodRepo.findOne(confirmParameter.deliveryMethodNo);
            pAgree.setDeliveryMethod(catalogDeliveryMethod.getDeliveryMethod());
        }

        OrderConfirmation cr = new OrderConfirmation();
        cr.setDocumentNumber(confirmParameter.confirmNumber);
        cr.setPurchaseAgreement(pAgree);
        cr.setCustomerDetails(confirmParameter.customerDetails);

        for (ReportItem ci : createConfirmationItemsSafely(confirmParameter.itemsToBeConfirmed))
            cr.addItem(ci);
        
        return reportRepo.save(cr);
    }

	private void validateConfirm(String orderNumber, String confirmNumber,
            List<ItemDto> orderItems, Address shippingAddress) {
        if (reportRepo.findByDocumentNumber(confirmNumber) != null) throw new IllegalArgumentException("Auftragsnr. " + confirmNumber
                + " besteht bereits");
        if (orderItems.isEmpty()) throw new IllegalArgumentException("Keine Positionen angegeben");
        if (orderNumber == null) throw new IllegalArgumentException("Keine Bestellnr angegeben");
        if (confirmNumber == null) throw new IllegalArgumentException("Keine AB-nr angegeben");
        if (shippingAddress == null) throw new IllegalArgumentException("Keine Lieferadresse angegeben");
    }
	
	private Set<ReportItem> createConfirmationItemsSafely(List<ItemDto> itemDtos) {
        Set<ReportItem> cis = new HashSet<ReportItem>();
        for (ItemDto entry : itemDtos) {
            ReportItem item = null;
            if (entry.id != null) {
                item = createConfirmationItemById(entry.id, entry.quantityLeft);
            }
            else if (!StringUtils.isEmpty(entry.product) && !entry.product.equals("0")) {
                item = createConfirmationItemByOrderNumber(entry.product, entry.orderNumber, entry.quantityLeft);
            }
            if (item == null) 
            	throw new IllegalArgumentException("Weder ID von Bestellposition noch Bestellung mit Artikelnummer angegeben");
            else cis.add(item);
        }
        return cis;
    }
	
    private ReportItem createConfirmationItemByOrderNumber(String productNumber, String orderNumber, Integer quantity) {
        List<OrderItem> ois = orderItemRepo.findByOrderNumber(orderNumber);
        for (OrderItem oi : ois) {
            if (oi.getProduct().getProductNumber().equals(productNumber)) 
            	return new ConfirmationItem(oi, quantity);
        }
        throw new NotFoundException(String.format("Bestellposition mit Artikelnummer %s nicht in Bestellung %s gefunden", productNumber, orderNumber));
    }

    private ReportItem createConfirmationItemById(long orderItemId, int qty) {
        OrderItem oi = orderItemRepo.findOne(orderItemId);
        if (oi == null) throw new NotFoundException("Bestellposition mit gegebener ID nicht gefunden");
        return new ConfirmationItem(oi, qty);
    }



}
