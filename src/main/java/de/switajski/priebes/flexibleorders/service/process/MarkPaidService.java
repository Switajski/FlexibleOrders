package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

@Service
public class MarkPaidService {

    @Autowired
    private ReportRepository reportRepo;
    
    @Transactional
    public Receipt markAsPayed(BillingParameter billingParameter) {
        if (reportRepo.findByDocumentNumber(billingParameter.receiptNumber) != null)
            billingParameter.receiptNumber.concat("-2");

        Invoice invoice = retrieveInvoiceSavely(billingParameter.invoiceNumber);
        Receipt receipt = new Receipt(billingParameter.receiptNumber, billingParameter.date);
        ReportItem reportItem = null;
        for (ReportItem ri : invoice.getItems()) {
            receipt.addItem(
                    new ReceiptItem(receipt, ri.getOrderItem(), ri
                            .getQuantity(), new Date()));
            if (reportItem == null)
                reportItem = ri;
        }
        receipt.setCustomerNumber(reportItem
                .getOrderItem()
                .getOrder()
                .getCustomer()
                .getCustomerNumber());
        return reportRepo.save(receipt);
    }
    
    private Invoice retrieveInvoiceSavely(String invoiceNumber) {
        Report r = reportRepo.findByDocumentNumber(invoiceNumber);
        if (r == null || !(r instanceof Invoice))
            throw new IllegalArgumentException("Rechnungsnr nicht gefunden");
        return (Invoice) r;
    }

}
