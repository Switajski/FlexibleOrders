package de.switajski.priebes.flexibleorders.service.conversion;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ItemDtoConverterServiceTest {

    @Mock
    ReportItemRepository repo;

    @InjectMocks
    ItemDtoConverterService service = new ItemDtoConverterService();

    DeliveryNotes deliveryNotes;

    ItemDto itemDto;

    ConfirmationItem confirmationItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deliveryNotes = new DeliveryNotes();
    }

    @Test
    public void shouldMapConfirmationItem() {
        givenTestData();

        whenMappingItemDtoToDeliveryNotes();

        assertThat(deliveryNotes.getItems(), hasItem(instanceOf(ShippingItem.class)));
    }

    private void givenTestData() {
        givenItemDtoForInput();
        givenMappedConfirmationItem();
        when(repo.findOne(1L)).thenReturn(confirmationItem);
    }

    @Test
    public void shouldMapPendingItem() {
        givenTestData();
        itemDto.pending = true;

        whenMappingItemDtoToDeliveryNotes();

        assertThat(deliveryNotes.getItems(), hasItem(instanceOf(PendingItem.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithTooMuchQty() {
        givenTestData();
        itemDto.quantityLeft = 2;

        whenMappingItemDtoToDeliveryNotes();

        assertThat(deliveryNotes.getItems(), hasItem(instanceOf(PendingItem.class)));
    }

    private void givenItemDtoForInput() {
        itemDto = new ItemDto();
        itemDto.productType = ProductType.PRODUCT;
        itemDto.id = 1L;
        itemDto.quantityLeft = 1;
    }

    private void givenMappedConfirmationItem() {
        confirmationItem = new ConfirmationItem();
        confirmationItem.setQuantity(1);

        OrderConfirmation report = new OrderConfirmation();
        report.addItem(confirmationItem);
        report.setOrderAgreementNumber("agreed");

        OrderItem orderItem = new OrderItem(new Order(), new Product(), 1);
        orderItem.addReportItem(confirmationItem);
        confirmationItem.setOrderItem(orderItem);
    }

    private void whenMappingItemDtoToDeliveryNotes() {
        service.mapItemDtos(deliveryNotes, itemDto);
    }
}
