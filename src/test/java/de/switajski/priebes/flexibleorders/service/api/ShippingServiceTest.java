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
import de.switajski.priebes.flexibleorders.service.QuantityUtility;
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

public class ShippingServiceTest {

    private static final Address ADDRESS_1 = AddressBuilder.buildWithGeneratedAttributes(2);

    private static final Address ADDRESS_2 = AddressBuilder.buildWithGeneratedAttributes(45);

    private static final String DN_NO = "L123";
    
    private static final Amount SHIPPING_COSTS = new Amount(BigDecimal.TEN, Currency.EUR);

    private static final DeliveryMethod DELIVERY_METHOD = new DeliveryMethodBuilder().dhl().build();

    @InjectMocks
	ShippingService shippingService = new ShippingService();
    @Mock
    ItemDtoConverterService convService;
    @Mock
    ReportRepository reportRepo;
    @Mock
    ExpectedDeliveryService expectedDeliveryService;
    @Mock
    ShippingAddressService shippingAddressService;
    @Mock
    QuantityUtility qtyLeftCalcService;
    
    DeliverParameter deliverParameter;
    
    Map<ReportItem, Integer> itemsWithExpectedDeliveryDates;
    
    Map<ReportItem, Integer> pendingItems;

	ArgumentCaptor<DeliveryNotes> capturedDeliveryNotes;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        // GIVEN
    	givenOrdersWithConfirmationAndDeliverParameter();
    	deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenTwoShippingAddresses();
        givenDeliverParameter();

        // WHEN / THEN
        whenDelivering();

    }
    
    @Test
    public void shouldNotDeliverPending() {
        // GIVEN
    	givenOrdersWithConfirmationAndDeliverParameter();
    	givenPendingItems();
        givenOneShippingAddress();
        givenDeliverParameter();

        // WHEN / THEN
        whenDelivering();
        
        DeliveryNotes dn = captureSavedDeliveryNotes();
        assertThat(dn.getItems().size(), is(equalTo(3)));
        assertThat(dn.getPendingItems().size(), is(equalTo(1)));
        
    }

	private void givenPendingItems() {
		pendingItems = new HashMap<ReportItem, Integer>();
		pendingItems.put(givenItem(), 8);
		when(convService.mapPendingItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
    		.thenReturn(pendingItems);
		
	}

	private DeliveryNotes captureSavedDeliveryNotes() {
		capturedDeliveryNotes = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(capturedDeliveryNotes.capture());
        return capturedDeliveryNotes.getValue();
	}


	private void whenDelivering() {
		shippingService.ship(deliverParameter);
	}
    
    private void givenDeliverParameter() {
        deliverParameter = new DeliverParameter();
        deliverParameter.deliveryNotesNumber = DN_NO;
    }

    @Test
    public void shouldDeliverIfContradictoryExpectedDeliveryDatesExistAndIgnoreFlagIsSet() {
        givenOrdersWithConfirmationAndDeliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenOneShippingAddress();

        whenDelivering();

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }
    
    @Test(expected=ContradictoryPurchaseAgreementException.class)
    public void shouldNotDeliverIfContradictoryExpectedDeliveryDatesExist() {
        givenOrdersWithConfirmationAndDeliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = false;
        deliverParameter.created = new Date();
        when(expectedDeliveryService.retrieveWeekOfYear(anySet())).thenReturn(new HashSet<Integer>(Arrays.asList(45, 56)));
        givenOneShippingAddress();

        whenDelivering();

        // THEN
        assertThatNoDeliveryNotesIsSaved();
    }
    
    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldNotDeliverIfContradictoryShippingAddressesExist() {
        givenOrdersWithConfirmationAndDeliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenTwoShippingAddresses();

        whenDelivering();

        assertThatNoDeliveryNotesIsSaved();
    }
    
	private void givenOrdersWithConfirmationAndDeliverParameter() {
		mockValidation();
		givenItemsWithDifferentExpectedDeliveryDates();
		when(convService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
        	.thenReturn(itemsWithExpectedDeliveryDates);
        givenDeliverParameter();
	}


    private void assertThatNoDeliveryNotesIsSaved() {
    	verify(reportRepo, VerificationModeFactory.noMoreInteractions()).save(Matchers.any(Report.class));
	}

	@Test
    public void shouldDeliverIfNoExpectedDeliveryDateIsSet() {
        givenTestData();

        // WHEN
        whenDelivering();

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }

	private void givenTestData() {
		mockValidation();
        givenItemsWithDifferentExpectedDeliveryDates();
        when(convService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
        .thenReturn(itemsWithExpectedDeliveryDates);
        givenOneShippingAddress();
        givenDeliverParameter();
	}
    
    @Test
    public void shouldCreateShippingCosts(){
        givenTestData();
        deliverParameter.shipment = SHIPPING_COSTS;
        deliverParameter.deliveryMethod = DELIVERY_METHOD;
        
        // WHEN
        whenDelivering();
        
        // THEN
        DeliveryNotes deliveryNotes = captureSavedDeliveryNotes();
        assertThat(deliveryNotes.getShippingCosts(), is(equalTo(SHIPPING_COSTS)));
        assertThat(deliveryNotes.getDeliveryMethod(), is(equalTo(DELIVERY_METHOD)));
        
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
        itemsWithExpectedDeliveryDates = new HashMap<ReportItem, Integer>();
        
        itemsWithExpectedDeliveryDates.put(givenItem(), 2);
        itemsWithExpectedDeliveryDates.put(givenItemOther(), 5);
        return itemsWithExpectedDeliveryDates;
    }

    private ReportItem givenItem() {
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

    private ReportItem givenItemOther() {
        int orderedQuantity = 25;
		int quantity = 9;
		return createConfirmationItem(quantity, orderedQuantity);
    }

}
