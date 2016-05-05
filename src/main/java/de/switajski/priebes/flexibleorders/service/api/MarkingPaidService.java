package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.BillingParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class MarkingPaidService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemToItemDtoConversionService conversionService;

    @Transactional
    public Set<ItemDto> markAsPayed(BillingParameter billingParameter) {
        if (billingParameter.getReceiptNumber() == null) {
            billingParameter.setReceiptNumber("B" + billingParameter.getInvoiceNumber());
        }

        if (reportRepo.findByDocumentNumber(billingParameter.getReceiptNumber()) != null) {
            billingParameter.setReceiptNumber(billingParameter.getReceiptNumber().concat("-2"));
        }

        Invoice invoice = retrieveInvoiceSavely(billingParameter.getInvoiceNumber());
        Date date = billingParameter.getDate() == null ? new Date() : billingParameter.getDate();

        Receipt receipt = new Receipt(billingParameter.getReceiptNumber(), date);
        ReportItem reportItem = null;
        for (ReportItem ri : invoice.getItems()) {
            ReceiptItem receiptItem = new ReceiptItem(receipt, ri.getOrderItem(), ri
                    .getQuantity(), new Date());
            receiptItem.setPredecessor(ri);
            receipt.addItem(
                    receiptItem);
            if (reportItem == null) reportItem = ri;
        }
        Receipt createdReceipt = reportRepo.save(receipt);
        return conversionService.convert(createdReceipt.getItems());
    }

    private Invoice retrieveInvoiceSavely(String invoiceNumber) {
        Report r = reportRepo.findByDocumentNumber(invoiceNumber);
        if (r == null || !(r instanceof Invoice)) throw new IllegalArgumentException("Rechnungsnr nicht gefunden");
        return (Invoice) r;
    }

}
