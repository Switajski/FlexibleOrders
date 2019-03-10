package de.switajski.priebes.flexibleorders.service.api;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DateUtils;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class InvoicingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemRepository reportItemRepo;
    @Autowired
    private PurchaseAgreementReadService purchaseAgreementService;
    @Autowired
    private ReportItemToItemDtoConversionService reportItemToItemDtoConversionService;

    @Transactional
    public Set<ItemDto> invoice(InvoicingParameter invoicingParameter) throws ContradictoryAddressException {

        Map<ReportItem, Integer> risWithQty = mapItemDtosToReportItemsWithQty(invoicingParameter.getItems());
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

        Invoice createdInvoice = reportRepo.save(invoice);
        return createdInvoice.getItems().stream()
                .map(item -> reportItemToItemDtoConversionService.convert(item, item.getQuantity()))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Map<ReportItem, Integer> mapItemDtosToReportItemsWithQty(Collection<ItemDto> itemDtos) {
        Map<ReportItem, Integer> risWithQty = new HashMap<ReportItem, Integer>();
        for (ItemDto itemDtoToBeProcessed : itemDtos) {
            if (itemDtoToBeProcessed.getProductType() != ProductType.SHIPPING) {
                ReportItem agreementItem = reportItemRepo
                    .findById(itemDtoToBeProcessed.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Angegebene Position nicht gefunden"));
                risWithQty.put(
                        agreementItem,
                        itemDtoToBeProcessed.getQuantityLeft());
            }
        }
        return risWithQty;
    }

    private Address retrieveInvoicingAddress(Set<ReportItem> reportItems) throws ContradictoryAddressException {
        Set<Address> ias = purchaseAgreementService.invoiceAddresses(reportItems);

        if (ias.size() > 1) {
            throw new ContradictoryAddressException(
                    "Widerspruechliche Rechnungsadressen gefunden");
        }
        else if (ias.size() == 0) {
            throw new IllegalStateException("Keine Rechnungsaddresse aus Kaufvertr" + Unicode.A_UML + "gen gefunden");
        }
        return ias.iterator().next();
    }

    private Invoice createInvoice(InvoicingParameter invoicingParameter) {
        Invoice invoice = new Invoice(invoicingParameter.getInvoiceNumber(), null);
        invoice.setBilling(invoicingParameter.getBilling());
        LocalDate created = (invoicingParameter.getCreated() == null) ? LocalDate.now() : invoicingParameter.getCreated();
        invoice.setCreated(DateUtils.asDate(created));
        invoice.setDiscountRate(invoicingParameter.getDiscountRate());
        invoice.setDiscountText(invoicingParameter.getDiscountText());
        return invoice;
    }

}
