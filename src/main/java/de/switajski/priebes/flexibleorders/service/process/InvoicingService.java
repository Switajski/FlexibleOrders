package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculator;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class InvoicingService {

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private ReportItemRepository reportItemRepo;

    @Autowired
    private ItemDtoConverterService itemDtoConverterService;

    @Transactional
    public Invoice invoice(InvoicingParameter invoicingParameter) {
        if (reportRepo.findByDocumentNumber(invoicingParameter.invoiceNumber) != null) throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

        Invoice invoice = new Invoice(invoicingParameter.invoiceNumber, invoicingParameter.paymentConditions, null);
        invoice.setBilling(invoicingParameter.billing);

        Order order = null;
        Address invoiceAddress = null;
        for (ItemDto entry : invoicingParameter.shippingItemDtos) {
            ReportItem shipEventToBeInvoiced = reportItemRepo.findOne(entry.id);

            OrderItem orderItemToBeInvoiced = shipEventToBeInvoiced
                    .getOrderItem();

            ServiceHelper.validateQuantity(entry.quantityLeft, shipEventToBeInvoiced);

            // validate addresses - DRY at deliver method
            AgreementHistory aHistory = new AgreementHistory(DeliveryHistory.createFrom(orderItemToBeInvoiced));
            invoiceAddress = validateInvoiceAdress(invoiceAddress, aHistory);

            invoice.setInvoiceAddress(invoiceAddress);

            invoice.addItem(new InvoiceItem(
                    invoice,
                    shipEventToBeInvoiced.getOrderItem(),
                    entry.quantityLeft, // TODO: GUI sets the quantity to
                                        // this nonsense place
                    new Date()));

            if (order == null) order = orderItemToBeInvoiced.getOrder();
        }

        invoice.setShippingCosts(new ShippingCostsCalculator()
                .calculate(itemDtoConverterService
                        .convertToShippingItems(invoicingParameter.shippingItemDtos)));
        invoice.setCreated((invoicingParameter.created == null) ? new Date() : invoicingParameter.created);
        // TODO: refactor DRY!
        invoice.setCustomerNumber(order.getCustomer().getCustomerNumber());

        return reportRepo.save(invoice);
    }

    private Address validateInvoiceAdress(Address invoiceAddress, AgreementHistory aHistory) {
        Address temp = aHistory.retrieveOnePurchaseAgreementOrFail().getInvoiceAddress();
        if (invoiceAddress == null) invoiceAddress = temp;
        else if (!invoiceAddress.equals(temp)) 
            throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");
        return invoiceAddress;
    }
}
