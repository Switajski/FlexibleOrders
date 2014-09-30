package de.switajski.priebes.flexibleorders.service.process;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.BusinessErrorCode;
import de.switajski.priebes.flexibleorders.exceptions.SystemException;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliveryServiceTest {

    private static final Address ADDRESS_1 = AddressBuilder.buildWithGeneratedAttributes(2);

    private static final Address ADDRESS_2 = AddressBuilder.buildWithGeneratedAttributes(45);

    private static final String DN_NO = "L123";

    @InjectMocks
    DeliveryService deliveryService = new DeliveryService();
    @Mock
    ItemDtoConverterService convService;
    @Mock
    ReportRepository reportRepo;
    @Mock
    ShippingAddressService shippingAddressService;

    @Test
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        // GIVEN
        givenMocks();
        DeliverParameter deliverParam = new DeliverParameter();
        deliverParam.deliveryNotesNumber = DN_NO;
        givenTwoContradictingAddresses();
        
        try {
            // WHEN
            deliveryService.deliver(deliverParam);
            fail("SystemException expected, but test completed without Exceptions");
        } catch (SystemException e){
            
            // THEN expect SystemException
            assertThat(e.getErrorCode() == BusinessErrorCode.CONTRADICTORY_PAYMENT_AGREEMENTS, is(true));
        } 
    }

    private void givenTwoContradictingAddresses() {
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1, ADDRESS_2)));
    }

    @Test
    public void shouldDeliverIfContradictoryExpectedDeliveryDatesExistAndIgnoreFlagIsSet() {
        givenMocks();
        DeliverParameter deliverParameter = new DeliverParameter();
        deliverParameter.deliveryNotesNumber = DN_NO;
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenOneShippingAddress();

        // WHEN
        deliveryService.deliver(deliverParameter);

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }

    @Test
    public void shouldDeliverIfNoExpectedDeliveryDateIsSet() {
        givenMocks();
        DeliverParameter deliverParameter = new DeliverParameter();
        deliverParameter.deliveryNotesNumber = DN_NO;
        givenOneShippingAddress();

        // WHEN
        deliveryService.deliver(deliverParameter);

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }

    private void givenOneShippingAddress() {
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1)));
    }

    private void assertThatDeliveryNotesIsSavedWithTwoItems() {
        ArgumentCaptor<DeliveryNotes> argument = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(argument.capture());
        assertThat(argument.getValue().getItems().size(), is(equalTo(2)));
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

}
