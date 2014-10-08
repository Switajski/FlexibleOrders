package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculator;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.InvoicingAddressService;
import de.switajski.priebes.flexibleorders.service.QuantityLeftCalculatorService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;

@Service
public class InvoicingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoConverterService itemDtoConverterService;
    @Autowired
    private InvoicingAddressService invoicingAddressService;
    @Autowired
    private QuantityLeftCalculatorService qtyLeftCalcService;

    @Transactional
    public Invoice invoice(InvoicingParameter invoicingParameter) {
        if (reportRepo.findByDocumentNumber(invoicingParameter.invoiceNumber) != null) throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

        Map<ReportItem, Integer> risWithQty = itemDtoConverterService.mapItemDtosToReportItemsWithQty(invoicingParameter.shippingItemDtos);
        Invoice invoice = createInvoice(invoicingParameter);
        invoice.setInvoiceAddress(retrieveInvoicingAddress(risWithQty.keySet()));

        for (Entry<ReportItem, Integer> entry : risWithQty.entrySet()) {
            ReportItem shippingItem = entry.getKey();
            Integer qty = entry.getValue();

            qtyLeftCalcService.validateQuantity(qty, shippingItem);
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

    private Address retrieveInvoicingAddress(Set<ReportItem> reportItems) {
        Set<Address> ias = invoicingAddressService.retrieve(reportItems);
        if (ias.size() > 1) throw new IllegalArgumentException("Verschiedene Rechnungsadressen in Auftr" + Unicode.aUml + "gen gefunden: "
                + BeanUtil.createStringOfDifferingAttributes(ias));
        else if (ias.size() == 0) throw new IllegalStateException("Keine Rechnungsaddresse aus Kaufvertr" + Unicode.aUml + "gen gefunden");
        Address invoicingAddress = ias.iterator().next();
        return invoicingAddress;
    }

    private Invoice createInvoice(InvoicingParameter invoicingParameter) {
        Invoice invoice = new Invoice(invoicingParameter.invoiceNumber, invoicingParameter.paymentConditions, null);
        invoice.setBilling(invoicingParameter.billing);
        invoice.setCreated((invoicingParameter.created == null) ? new Date() : invoicingParameter.created);
        // TODO: could this be retrieved from DB?
        invoice.setCustomerNumber(invoicingParameter.customerNumber);
        return invoice;
    }

}
