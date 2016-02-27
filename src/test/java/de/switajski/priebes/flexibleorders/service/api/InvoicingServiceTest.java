package de.switajski.priebes.flexibleorders.service.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.testdata.ItemDtoShorthand;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingServiceTest {

    @InjectMocks
    InvoicingService invoicingService = new InvoicingService();
    @Mock
    ReportRepository reportRepo;
    @Mock
    ItemDtoToReportItemConversionService itemDtoConverterService;
    @Mock
    PurchaseAgreementReadService purchaseAgreementService;

    InvoicingParameter invoicingParameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        invoicingParameter = new InvoicingParameter();
        invoicingParameter.setInvoiceNumber("L123");

    }

    @Test
    public void shouldSaveInvoice() {
        givenDocumentNumberDoesNotExist();
        givenInvoicingAddresses(1);
        givenItemsInParameter();

        whenInvoicing();

        verify(reportRepo).save(Matchers.any(Invoice.class));

    }

    private List<ItemDto> givenItemsInParameter(Integer... integers) {
        List<ItemDto> items = new ArrayList<>();
        for (Integer i : integers)
            items.add(ItemDtoShorthand.item(CatalogProductBuilder.buildWithGeneratedAttributes(i), i, invoicingParameter.getInvoiceNumber()));
        invoicingParameter.setItems(items);
        return items;
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvoicingIfDocumentNumberAlreadyExists() {
        when(reportRepo.findByDocumentNumber(invoicingParameter.getInvoiceNumber()))
                .thenReturn(new Invoice());

        whenInvoicing();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvoicingIfDifferentInvoicingAddressesExist() {
        givenDocumentNumberDoesNotExist();
        givenShippingItemsFromParameterExist();
        givenInvoicingAddresses(1, 2);

        whenInvoicing();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectInvoicingIfNoInvoicingAddressesExist() {
        givenDocumentNumberDoesNotExist();
        when(purchaseAgreementService.invoiceAddressesWithoutDeviation(Matchers.anySetOf(ReportItem.class))).thenReturn(java.util.Collections.emptySet());

        whenInvoicing();
    }

    private void givenShippingItemsFromParameterExist() {
        Map<ReportItem, Integer> map = new HashMap<ReportItem, Integer>();
        map.put(givenAgreementItemWith(null), 2);

        when(itemDtoConverterService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class))).thenReturn(map);
    }

    private void givenDocumentNumberDoesNotExist() {
        when(reportRepo.findByDocumentNumber(invoicingParameter.getInvoiceNumber())).thenReturn(null);
    }

    private void givenInvoicingAddresses(Integer... integers) {
        Set<Address> as = new HashSet<Address>();
        for (Integer i : integers)
            as.add(AddressBuilder.buildWithGeneratedAttributes(i));

        when(purchaseAgreementService.invoiceAddressesWithoutDeviation(Matchers.anySetOf(ReportItem.class))).thenReturn(as);
    }

    private ReportItem givenAgreementItemWith(PurchaseAgreement purchaseAgreement) {
        return new ConfirmationItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderConfirmationBuilder()
                                .setAgreementDetails(purchaseAgreement)
                                .build())
                .build();
    }

    private void whenInvoicing() {
        invoicingService.invoice(invoicingParameter);
    }

}
