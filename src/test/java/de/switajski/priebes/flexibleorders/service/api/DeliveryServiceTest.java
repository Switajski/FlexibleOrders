package de.switajski.priebes.flexibleorders.service.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.QuantityLeftCalculatorService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryMethodBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliveryServiceTest {

    private static final Address ADDRESS_1 = AddressBuilder.buildWithGeneratedAttributes(2);

    private static final Address ADDRESS_2 = AddressBuilder.buildWithGeneratedAttributes(45);

    private static final String DN_NO = "L123";
    
    private static final Amount SHIPPING_COSTS = new Amount(BigDecimal.TEN, Currency.EUR);

    private static final DeliveryMethod DELIVERY_METHOD = new DeliveryMethodBuilder().dhl().build();

    @InjectMocks
    DeliveryService deliveryService = new DeliveryService();
    @Mock
    ItemDtoConverterService convService;
    @Mock
    ReportRepository reportRepo;
    @Mock
    ShippingAddressService shippingAddressService;
    @Mock
    QuantityLeftCalculatorService qtyLeftCalcService;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        // GIVEN
        mockValidation();
        givenReportItems();
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class)))
                .thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1, ADDRESS_2)));

        // WHEN / THEN
        deliveryService.deliver(givenDeliverParameter());

    }
    
    private void givenReportItems() {
        when(convService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
                .thenReturn(givenItemMap());
    }

    private DeliverParameter givenDeliverParameter() {
        DeliverParameter deliverParam = new DeliverParameter();
        deliverParam.deliveryNotesNumber = DN_NO;
        return deliverParam;
    }

    @Test
    public void shouldDeliverIfContradictoryExpectedDeliveryDatesExistAndIgnoreFlagIsSet() {
        mockValidation();
        givenReportItems();
        DeliverParameter deliverParameter = givenDeliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenOneShippingAddress();

        // WHEN
        deliveryService.deliver(deliverParameter);

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }

    @Test
    public void shouldDeliverIfNoExpectedDeliveryDateIsSet() {
        mockValidation();
        givenReportItems();
        givenOneShippingAddress();

        // WHEN
        deliveryService.deliver(givenDeliverParameter());

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }
    
    @Test
    public void shouldCreateShippingCosts(){
        // GIVEN
        mockValidation();
        givenReportItems();
        givenOneShippingAddress();
        DeliverParameter param = givenDeliverParameter();
        param.shipment = SHIPPING_COSTS;
        param.deliveryMethod = DELIVERY_METHOD;
        
        // WHEN
        deliveryService.deliver(param);
        
        // THEN
        ArgumentCaptor<DeliveryNotes> deliveryNotes = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(deliveryNotes.capture());
        assertThat(deliveryNotes.getValue().getShippingCosts(), is(equalTo(SHIPPING_COSTS)));
        assertThat(deliveryNotes.getValue().getDeliveryMethod(), is(equalTo(DELIVERY_METHOD)));
        
    }

    private void givenOneShippingAddress() {
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1)));
    }

    private void assertThatDeliveryNotesIsSavedWithTwoItems() {
        ArgumentCaptor<DeliveryNotes> argument = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(argument.capture());
        assertThat(argument.getValue().getItems().size(), is(equalTo(2)));
    }

    private void mockValidation() {
        MockitoAnnotations.initMocks(this);
        when(reportRepo.findByDocumentNumber(DN_NO)).thenReturn(null);
    }

    private Map<ReportItem, Integer> givenItemMap() {
        Map<ReportItem, Integer> map = new HashMap<ReportItem, Integer>();
        map.put(givenItemWith(null), 2);
        map.put(givenItemOtherWith(null), 5);
        return map;
    }

    private ReportItem givenItemWith(PurchaseAgreement purchaseAgreement) {
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

    private ReportItem givenItemOtherWith(PurchaseAgreement pa) {
        return new ConfirmationItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderConfirmationBuilder()
                                .setAgreementDetails(pa)
                                .build())
                .build();
    }

}
