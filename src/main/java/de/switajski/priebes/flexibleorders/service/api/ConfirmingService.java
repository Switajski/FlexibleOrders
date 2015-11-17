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
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ConfirmingService {

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

        addConfirmationItemsSafely(confirmParameter.itemsToBeConfirmed, cr);
        
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
	
	private Set<ReportItem> addConfirmationItemsSafely(List<ItemDto> itemDtos, OrderConfirmation orderConfirmation) {
        Set<ReportItem> cis = new HashSet<ReportItem>();
        for (ItemDto entry : itemDtos) {
        	boolean added = false;
            Integer qtyToBeConfirmed = entry.quantityLeft;
			if (entry.id != null) {
                cis.add(createConfirmationItemById(orderConfirmation, entry.id, qtyToBeConfirmed));
                added = true;
                
            } else if (!StringUtils.isEmpty(entry.product) && !entry.product.equals("0")) {
            	Set<OrderItem> notConfirmed = matchNotConfirmed(entry.product, entry.orderNumber);
				if (notConfirmed.size() == 1){
					OrderItem match = notConfirmed.iterator().next();
					if (match.toBeConfirmed() <= qtyToBeConfirmed){
						cis.add(new ConfirmationItem(orderConfirmation, match, qtyToBeConfirmed));
						added = true;
					} else {
						StringBuilder errMsgBuilder = new StringBuilder().append("Angegebene Menge zum best").append(Unicode.A_UML)
								.append("tigen ist ").append(qtyToBeConfirmed).append(". Dagegen m sind nur noch ").append(match.toBeConfirmed()).append(Unicode.U_UML).append("brig");
						throw new IllegalArgumentException(errMsgBuilder.toString());
					}
				} else {
					OrderItem match = findExactMatch(notConfirmed, qtyToBeConfirmed);
					if (match != null){
						cis.add(new ConfirmationItem(orderConfirmation, match, qtyToBeConfirmed));
						added = true;
					} else {
						String string = new StringBuilder().append("Zwar sind noch nicht best").append(Unicode.A_UML).append("tigte Bestellpositionen vorhanden, aber die angegbene Menge passt zu keiner").toString();
						throw new IllegalArgumentException(string);
					}
				}
            }
            if (!added) 
            	throw new IllegalArgumentException("Weder ID von Bestellposition noch Bestellung mit Artikelnummer angegeben");
        }
        return cis;
    }
	

	private OrderItem findExactMatch(Set<OrderItem> matching, Integer quantity) {
		for (OrderItem candidate:matching){
			if (quantity == candidate.toBeConfirmed()){
				return candidate;
			}
		}
		return null;
	}

	private Set<OrderItem> matchNotConfirmed(String productNumber, String orderNumber) {
		List<OrderItem> ois = orderItemRepo.findByOrderNumber(orderNumber);
		Set<OrderItem> matchingOis = new HashSet<OrderItem>(); 
        for (OrderItem oi : ois) {
            if (oi.getProduct().getProductNumber().equals(productNumber) && (oi.toBeConfirmed() > 0)) {
            	matchingOis.add(oi);
            }
        }
        return matchingOis;
	}

    protected ReportItem createConfirmationItemById(OrderConfirmation orderConfirmation, long orderItemId, int qty) {
        OrderItem oi = orderItemRepo.findOne(orderItemId);
        if (oi == null) throw new NotFoundException("Bestellposition mit gegebener ID nicht gefunden");
        return new ConfirmationItem(orderConfirmation, oi, qty);
    }



}
