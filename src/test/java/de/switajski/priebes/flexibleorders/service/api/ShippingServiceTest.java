package de.switajski.priebes.flexibleorders.service.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryMethodBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ShippingServiceTest {

    private static final String DN_NO = "L123";

    private static final BigDecimal SHIPPING_COSTS = BigDecimal.TEN;

    private static final DeliveryMethod DELIVERY_METHOD = new DeliveryMethodBuilder().dhl().build();

    @InjectMocks
    ShippingService shippingService = new ShippingService();
    @Mock
    ItemDtoToReportItemConversionService convService;
    @Mock
    ReportRepository reportRepo;
    @Mock
    ExpectedDeliveryService expectedDeliveryService;
    @Mock
    PurchaseAgreementService purchaseAgreementService;

    DeliverParameter deliverParameter;

    ArgumentCaptor<DeliveryNotes> capturedDeliveryNotes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateShippingCosts() {
        givenDeliverParameter();
        deliverParameter.setShipment(SHIPPING_COSTS);
        deliverParameter.setDeliveryMethod(DELIVERY_METHOD);
        deliverParameter.setItems(new ArrayList<ItemDto>());
        deliverParameter.getItems().add(new ItemDto());

        // WHEN
        whenDelivering();

        // THEN
        DeliveryNotes deliveryNotes = captureSavedDeliveryNotes();
        assertThat(deliveryNotes.getShippingCosts(), is(equalTo(SHIPPING_COSTS)));
        assertThat(deliveryNotes.getDeliveryMethod(), is(equalTo(DELIVERY_METHOD)));

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
        deliverParameter.setDeliveryNotesNumber(DN_NO);
    }

}
