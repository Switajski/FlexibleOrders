package de.switajski.priebes.flexibleorders.service.process;

import static de.switajski.priebes.flexibleorders.service.process.ProcessServiceHelper.extractReportItemIds;
import static de.switajski.priebes.flexibleorders.service.process.ProcessServiceHelper.validateQuantity;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculator;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.BusinessInputException;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;

@Service
public class InvoicingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemRepository reportItemRepo;
    @Autowired
    private ItemDtoConverterService itemDtoConverterService;
    @Autowired
    private PurchaseAgreementService addressService;
    @Autowired
    private PurchaseAgreementService purchaseAgreementService;

    @Transactional
    public Invoice invoice(InvoicingParameter invoicingParameter) {
        if (reportRepo.findByDocumentNumber(invoicingParameter.invoiceNumber) != null) throw new BusinessInputException("Rechnungsnr. existiert bereits");

        Map<ReportItem, Integer> risWithQty = itemDtoConverterService.mapItemDtosToReportItemsWithQty(invoicingParameter.shippingItemDtos);
        Address invoicingAddress = purchaseAgreementService.retrieveOneOrFail(
                extractReportItemIds(invoicingParameter.shippingItemDtos))
                .getInvoiceAddress();
        
        Invoice invoice = createInvoice(invoicingParameter);

        for (Entry<ReportItem, Integer> entry : risWithQty.entrySet()) {
            ReportItem shippingItem = entry.getKey();
            Integer qty = entry.getValue();

            validateQuantity(qty, shippingItem);
            invoice.setInvoiceAddress(invoicingAddress);
            invoice.addItem(new InvoiceItem(
                    invoice,
                    shippingItem.getOrderItem(),
                    qty, 
                    new Date()));

        }

        invoice.setShippingCosts(new ShippingCostsCalculator()
                .calculate(itemDtoConverterService
                        .convertToShippingItems(invoicingParameter.shippingItemDtos)));

        return reportRepo.save(invoice);
    }

    private Invoice createInvoice(InvoicingParameter invoicingParameter) {
        Invoice invoice = new Invoice(invoicingParameter.invoiceNumber, invoicingParameter.paymentConditions, null);
        invoice.setBilling(invoicingParameter.billing);
        invoice.setCreated((invoicingParameter.created == null) ? new Date() : invoicingParameter.created);
        //TODO: could this be retrieved from DB?
        invoice.setCustomerNumber(invoicingParameter.customerNumber);
        return invoice;
    }

}
