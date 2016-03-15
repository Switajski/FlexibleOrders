package de.switajski.priebes.flexibleorders.service.conversion;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
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
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ShippingServiceConversionTest {

    @Mock
    ReportItemRepository repoItemRepo;

    @InjectMocks
    ShippingService service = new ShippingService();

    ConfirmationItem confirmationItem;

    ItemDto input;

    ReportItem createdReportItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldMapConfirmationItem() {
        givenTestData();

        whenCreatingReportItemOutOfItemDto();

        assertThat(createdReportItem, is(instanceOf(ShippingItem.class)));
    }

    private void givenTestData() {
        givenItemDtoForInput();
        givenMappedConfirmationItem();
        when(repoItemRepo.findOne(1L)).thenReturn(confirmationItem);
    }

    @Test
    public void shouldMapPendingItem() {
        givenTestData();
        input.setPending(true);

        whenCreatingReportItemOutOfItemDto();

        assertThat(createdReportItem, is(instanceOf(PendingItem.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithTooMuchQty() {
        givenTestData();
        input.setQuantityLeft(2);

        whenCreatingReportItemOutOfItemDto();

        assertThat(createdReportItem, is(instanceOf(PendingItem.class)));
    }

    private void givenItemDtoForInput() {
        input = new ItemDto();
        input.setProductType(ProductType.PRODUCT);
        input.setId(1L);
        input.setQuantityLeft(1);
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

    private void whenCreatingReportItemOutOfItemDto() {
        createdReportItem = service.createReportItemByItemToBeShipped(input, confirmationItem);
    }
}
