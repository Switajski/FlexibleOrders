package de.switajski.priebes.flexibleorders.service.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
import org.mockito.internal.verification.VerificationModeFactory;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
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
    ExpectedDeliveryService expectedDeliveryService;
    @Mock
    ShippingAddressService shippingAddressService;
    @Mock
    QuantityLeftCalculatorService qtyLeftCalcService;
    
    DeliverParameter deliverParameter;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        // GIVEN
    	givenOrdersWithConfirmationAndDweliverParameter();
    	deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenTwoShippingAddresses();
        givenDeliverParameter();

        // WHEN / THEN
        whenDelivering();

    }

	private void whenDelivering() {
		deliveryService.deliver(deliverParameter);
	}
    
    private void givenReportItems() {
        when(convService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
                .thenReturn(givenItemsWithDifferentExpectedDeliveryDates());
    }

    private void givenDeliverParameter() {
        deliverParameter = new DeliverParameter();
        deliverParameter.deliveryNotesNumber = DN_NO;
    }

    @Test
    public void shouldDeliverIfContradictoryExpectedDeliveryDatesExistAndIgnoreFlagIsSet() {
        givenOrdersWithConfirmationAndDweliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenOneShippingAddress();

        whenDelivering();

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }
    
    @Test(expected=ContradictoryPurchaseAgreementException.class)
    public void shouldNotDeliverIfContradictoryExpectedDeliveryDatesExist() {
        givenOrdersWithConfirmationAndDweliverParameter();
        LocalDate now = new LocalDate(new Date());
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = false;
        deliverParameter.created = new Date();
        when(expectedDeliveryService.retrieve(anySet())).thenReturn(new HashSet<LocalDate>(Arrays.asList(now, now.plusWeeks(1))));
        givenOneShippingAddress();

        whenDelivering();

        // THEN
        assertThatNoDeliveryNotesIsSaved();
    }
    
    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldNotDeliverIfContradictoryShippingAddressesExist() {
        givenOrdersWithConfirmationAndDweliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenTwoShippingAddresses();

        whenDelivering();

        assertThatNoDeliveryNotesIsSaved();
    }
    
	private void givenOrdersWithConfirmationAndDweliverParameter() {
		mockValidation();
        givenReportItems();
        givenDeliverParameter();
	}


    private void assertThatNoDeliveryNotesIsSaved() {
    	verify(reportRepo, VerificationModeFactory.noMoreInteractions()).save(Matchers.any(Report.class));
	}

	@Test
    public void shouldDeliverIfNoExpectedDeliveryDateIsSet() {
        mockValidation();
        givenReportItems();
        givenOneShippingAddress();
        givenDeliverParameter();

        // WHEN
        whenDelivering();

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }
    
    @Test
    public void shouldCreateShippingCosts(){
        // GIVEN
        mockValidation();
        givenReportItems();
        givenOneShippingAddress();
        givenDeliverParameter();
        deliverParameter.shipment = SHIPPING_COSTS;
        deliverParameter.deliveryMethod = DELIVERY_METHOD;
        
        // WHEN
        whenDelivering();
        
        // THEN
        ArgumentCaptor<DeliveryNotes> deliveryNotes = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(deliveryNotes.capture());
        assertThat(deliveryNotes.getValue().getShippingCosts(), is(equalTo(SHIPPING_COSTS)));
        assertThat(deliveryNotes.getValue().getDeliveryMethod(), is(equalTo(DELIVERY_METHOD)));
        
    }

    private void givenOneShippingAddress() {
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1)));
    }
    
    private void givenTwoShippingAddresses() {
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1, ADDRESS_2)));
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

    private Map<ReportItem, Integer> givenItemsWithDifferentExpectedDeliveryDates() {
        Map<ReportItem, Integer> map = new HashMap<ReportItem, Integer>();
        
        map.put(givenItemWith(), 2);
        map.put(givenItemOtherWith(), 5);
        return map;
    }

    private ReportItem givenItemWith() {
        int quantity = 6;
		int orderedQuantity = 12;
		return createConfirmationItem(quantity, orderedQuantity);
    }

	private ReportItem createConfirmationItem(int quantity, int orderedQuantity) {
		return new ConfirmationItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(orderedQuantity)
                                .build())
                .setQuantity(quantity)
                .setReport(
                        new OrderConfirmationBuilder()
                                .setAgreementDetails(null) // purchase agreement here is irrelevant. All info about purchase agreements are retrieved via services.
                                .build())
                .build();
	}

    private ReportItem givenItemOtherWith() {
        int orderedQuantity = 25;
		int quantity = 9;
		return createConfirmationItem(quantity, orderedQuantity);
    }

}
