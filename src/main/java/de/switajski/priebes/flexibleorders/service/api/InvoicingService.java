package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class InvoicingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoToReportItemConversionService itemDtoConverterService;
    @Autowired
    private PurchaseAgreementService purchaseAgreementService;

    @Transactional
    public Invoice invoice(InvoicingParameter invoicingParameter) {
        // TODO: custom constraint!
        if (reportRepo.findByDocumentNumber(invoicingParameter.getInvoiceNumber()) != null) {
            throw new IllegalArgumentException("Rechnungsnr. existiert bereits");
        }

        Map<ReportItem, Integer> risWithQty = itemDtoConverterService.mapItemDtosToReportItemsWithQty(invoicingParameter.getItems());
        Invoice invoice = createInvoice(invoicingParameter);
        invoice.setInvoiceAddress(retrieveInvoicingAddress(risWithQty.keySet()));

        for (Entry<ReportItem, Integer> entry : risWithQty.entrySet()) {
            ReportItem shippingItem = entry.getKey();
            Integer qty = entry.getValue();

            QuantityUtility.validateQuantity(qty, shippingItem);
            InvoiceItem invoiceItem = new InvoiceItem(
                    invoice,
                    shippingItem.getOrderItem(),
                    qty,
                    new Date());
            invoiceItem.setPredecessor(shippingItem);
            invoice.addItem(invoiceItem);

        }

        // TODO Currency Handling
        Amount shippingCosts = Amount.ZERO_EURO;
        for (ItemDto item : invoicingParameter.getItems()) {
            if (item.getProductType() == ProductType.SHIPPING) {
                shippingCosts = shippingCosts.add(new Amount(item.getPriceNet(), Currency.EUR));
            }
        }
        invoice.setShippingCosts(shippingCosts);

        return reportRepo.save(invoice);
    }

    private Address retrieveInvoicingAddress(Set<ReportItem> reportItems) {
        Set<Address> ias = purchaseAgreementService.invoiceAddresses(reportItems);
        if (ias.size() > 1) throw new IllegalArgumentException("Verschiedene Rechnungsadressen in Auftr" + Unicode.A_UML + "gen gefunden: "
                + BeanUtil.createStringOfDifferingAttributes(ias));
        else if (ias.size() == 0) throw new IllegalStateException("Keine Rechnungsaddresse aus Kaufvertr" + Unicode.A_UML + "gen gefunden");
        Address invoicingAddress = ias.iterator().next();
        return invoicingAddress;
    }

    private Invoice createInvoice(InvoicingParameter invoicingParameter) {
        Invoice invoice = new Invoice(invoicingParameter.getInvoiceNumber(), null);
        invoice.setBilling(invoicingParameter.getBilling());
        invoice.setCreated((invoicingParameter.getCreated() == null) ? new Date() : invoicingParameter.getCreated());
        invoice.setDiscountRate(invoicingParameter.getDiscountRate());
        invoice.setDiscountText(invoicingParameter.getDiscountText());
        return invoice;
    }

}
