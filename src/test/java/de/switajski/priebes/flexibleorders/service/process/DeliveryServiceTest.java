package de.switajski.priebes.flexibleorders.service.process;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.service.process.DeliveryService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.PurchaseAgreementBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliveryServiceTest {

    private static final Address ADDRESS_1 = AddressBuilder.buildWithGeneratedAttributes(2);

    private static final Address ADDRESS_2 = AddressBuilder.buildWithGeneratedAttributes(45);

    private static final String DN_NO = "L123";

    @InjectMocks
    DeliveryService deliveryService = new DeliveryService();
    @Mock
    PurchaseAgreementService purchaseAgreementService = new PurchaseAgreementService();
    @Mock
    ItemDtoConverterService convService;
    @Mock
    ReportRepository reportRepo;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        // GIVEN
        givenMocks();
        when(purchaseAgreementService.hasEqualPurchaseAgreements(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(false);
        when(purchaseAgreementService.getPurchaseAgreements(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(new HashSet<PurchaseAgreement>(Arrays.asList(
                        givenPurchaseAgreement(), 
                        givenPaWithOtherShippingAddress())));

        DeliverParameter deliverParam = new DeliverParameter();
        deliverParam.deliveryNotesNumber = DN_NO;
        
        // WHEN
        deliveryService.deliver(deliverParam);
    }

    @Test
    public void shouldDeliverIfContradictoryExpectedDeliveryDatesExistAndIgnoreFlagIsSet() {
        givenMocks();
        when(purchaseAgreementService.hasEqualPurchaseAgreements(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(false);
        when(purchaseAgreementService.getPurchaseAgreements(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(new HashSet<PurchaseAgreement>(Arrays.asList(
                        givenPurchaseAgreement(), 
                        givenPurchaseAgreementWithOtherExpectedDeliveryDate())));
        when(purchaseAgreementService.retrieveOne(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(givenPurchaseAgreement());

        DeliverParameter deliverParameter = new DeliverParameter();
        deliverParameter.deliveryNotesNumber = DN_NO;
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;

        // WHEN
        deliveryService.deliver(deliverParameter);

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }
    
    @Test
    public void shouldDeliverIfNoExpectedDeliveryDateIsSet() {
        givenMocks();
        when(purchaseAgreementService.hasEqualPurchaseAgreements(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(true);
        when(purchaseAgreementService.retrieveOne(Matchers.anyCollectionOf(ReportItem.class)))
                .thenReturn(givenPaWithNoExpectedDeliveryDate());

        DeliverParameter deliverParameter = new DeliverParameter();
        deliverParameter.deliveryNotesNumber = DN_NO;

        // WHEN
        deliveryService.deliver(deliverParameter);

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }

    private void assertThatDeliveryNotesIsSavedWithTwoItems() {
        ArgumentCaptor<DeliveryNotes> argument = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(argument.capture());
        assertThat(argument.getValue().getItems().size(), is(equalTo(2)));
    }

    private PurchaseAgreement givenPaWithNoExpectedDeliveryDate() {
        return new PurchaseAgreementBuilder().setShippingAddress(ADDRESS_1).build();
    }

    private void givenMocks() {
        MockitoAnnotations.initMocks(this);
        when(reportRepo.findByDocumentNumber(DN_NO)).thenReturn(null);
        when(convService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
                .thenReturn(givenAgreementItemMap());
    }

    private Map<ReportItem, Integer> givenAgreementItemMap() {
        Map<ReportItem, Integer> map = new HashMap<ReportItem, Integer>();
        map.put(givenAgreementItemWith(null), 2);
        map.put(givenAgreementItemOtherWith(null), 5);
        return map;
    }

    private ReportItem givenAgreementItemWith(PurchaseAgreement purchaseAgreement) {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(purchaseAgreement)
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreementWithOtherExpectedDeliveryDate() {
        PurchaseAgreement ad = givenPurchaseAgreement();
        ad.setExpectedDelivery(new LocalDate().plusDays(10));
        return ad;
    }

    private ReportItem givenAgreementItemOtherWith(PurchaseAgreement pa) {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(pa)
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        return new PurchaseAgreementBuilder()
        .setExpectedDelivery(new LocalDate())
        .setShippingAddress(ADDRESS_1).build();
    }

    private PurchaseAgreement givenPaWithOtherShippingAddress() {
        PurchaseAgreement pa = givenPurchaseAgreement();
        pa.setShippingAddress(ADDRESS_2);
        return pa;
    }

}
