package de.switajski.priebes.flexibleorders.service.api;

import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AMY;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.EDWARD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryMethodBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ShippingServiceTest {

    private static final String DN_NO = "L123";

    private static final DeliveryMethod DELIVERY_METHOD = new DeliveryMethodBuilder().dhl().build();

    @InjectMocks
    ShippingService shippingService = new ShippingService();
    @Mock
    ReportRepository reportRepo;
    @Mock
    ReportItemRepository reportItemRepo;
    @Mock
    ExpectedDeliveryService expectedDeliveryService;
    @Mock
    PurchaseAgreementReadService purchaseAgreementService;
    @Mock
    ReportItemToItemDtoConversionService reportItemToItemDtoConversionService;

    DeliverParameter deliverParameter;

    DeliveryNotes capturedDeliveryNotes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        givenRepoSaveReturnsInputParameter();
    }

    private void givenRepoSaveReturnsInputParameter() {
        when(reportRepo.save(any(DeliveryNotes.class))).thenAnswer(new Answer<DeliveryNotes>() {

            @Override
            public DeliveryNotes answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (DeliveryNotes) args[0];
            }
        });
    }

    @Test
    public void shouldCreateShippingCosts() throws Exception {
        givenConfirmationItemToBeShipped(1L, AMY.toProduct(), 1);
        BigDecimal shippingCosts = BigDecimal.TEN;

        givenDeliverParameter();
        deliverParameter.setShipment(shippingCosts);
        deliverParameter.setDeliveryMethod(DELIVERY_METHOD);
        deliverParameter.addItem(createItemDto(1L, 1, AMY));

        // WHEN
        whenDelivering();

        // THEN
        captureSavedDeliveryNotes();
        assertThat(capturedDeliveryNotes.getShippingCosts().getValue(), is(equalTo(shippingCosts)));
        assertThat(capturedDeliveryNotes.getDeliveryMethod(), is(equalTo(DELIVERY_METHOD)));

    }

    @Test
    public void shouldCreateShippingItemByItemId() throws Exception {
        long id = 123L;
        int qtyAvailable = 5;
        givenConfirmationItemToBeShipped(id, AMY.toProduct(), qtyAvailable);

        long idFromReportItemToBeShipped = id;
        int qtyToShip = 1;
        givenDeliverParameter();
        deliverParameter.addItem(createItemDto(idFromReportItemToBeShipped, qtyToShip, AMY));

        whenDelivering();

        assertSavedItemIsShippingItemAndMatches(qtyToShip, AMY.toProduct());
    }

    @Test(expected = NoItemsToShipFoundException.class)
    public void shouldNotDeliverWhenIdIsNotSetAndIsNotOffTheRecord() throws Exception {
        long idFromReportItemToBeShipped = 123L;
        int qtyToShip = 1;
        int qtyAvailable = 5;
        givenConfirmationItemToBeShipped(idFromReportItemToBeShipped, AMY.toProduct(), qtyAvailable);

        Long id = null;
        givenDeliverParameter();
        deliverParameter.addItem(createItemDto(id, qtyToShip, AMY));

        whenDelivering();
    }

    @Test
    public void shouldCreateShippingItemByMatchingCustomerAndOverdueItems() throws Exception {
        int qtyAvailable = 30;
        givenConfirmationItemsToBeShippedBySpecificationQuery(new ProductQuantityAndCreationDate(AMY.toProduct(), qtyAvailable, null));

        Long id = null;
        givenDeliverParameter();
        deliverParameter.addItem(createItemDto(id, qtyAvailable, AMY));
        deliverParameter.setCustomerId(EDWARD.getCustomerNumber());

        whenDelivering();

        assertSavedItemIsShippingItemAndMatches(qtyAvailable, AMY.toProduct());
    }

    @Test
    public void shouldCreateShippingItemByMatchingCustomerAndConcurringOverdueItems() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        givenConfirmationItemsToBeShippedBySpecificationQuery(
                new ProductQuantityAndCreationDate(AMY.toProduct(), 10, now),
                new ProductQuantityAndCreationDate(AMY.toProduct(), 12, now.plusMinutes(1L)),
                new ProductQuantityAndCreationDate(AMY.toProduct(), 10, now.plusMinutes(3L)),
                new ProductQuantityAndCreationDate(AMY.toProduct(), 11, now.plusMinutes(4L)));

        int qty = 10;
        Long id = null;
        givenDeliverParameter();
        deliverParameter.addItem(createItemDto(id, qty, AMY));
        deliverParameter.setCustomerId(EDWARD.getCustomerNumber());

        whenDelivering();

        assertSavedItemIsShippingItemAndMatches(10, AMY.toProduct());
        assertThat(capturedDeliveryNotes
                .getItems()
                .iterator()
                .next()
                .getPredecessor()
                .getCreated(), equalTo(toOldDateClass(now)));
    }

    @Ignore("future functionality")
    @Test
    public void shouldCreateShippingItemOffTheRecord() throws Exception {
        long id = 123L;
        givenConfirmationItemToBeShipped(id, AMY.toProduct(), 10);

        givenDeliverParameter();
        deliverParameter.addItem(createOffTheRecordItem());
        deliverParameter.addItem(createItemDto(id, 10, AMY));

        whenDelivering();

        captureSavedDeliveryNotes();
        assertTrue(capturedDeliveryNotes.getItems().stream().allMatch(ri -> ri instanceof ShippingItem));
        // assumeThat(capturedDeliveryNotes.getItems().iterator().next(), ri ->
        // ri instanceof ShippingItem);
    }

    private Date toOldDateClass(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void givenConfirmationItemsToBeShippedBySpecificationQuery(ProductQuantityAndCreationDate... confirmationItemsToBeShipped) {
        List<ReportItem> confirmationItemsByQuery = new ArrayList<>();

        for (ProductQuantityAndCreationDate ci : confirmationItemsToBeShipped) {
            OrderItem item = new OrderItem(null, ci.product, ci.quantity);
            ConfirmationItem confirmationItem = new ConfirmationItem();
            confirmationItem.setOrderItem(item);
            confirmationItem.setQuantity(ci.quantity);
            if (ci.creationDate != null) confirmationItem.setCreated(toOldDateClass(ci.creationDate));
            confirmationItemsByQuery.add(confirmationItem);
        }
        when(reportItemRepo.findAll(any(Specification.class))).thenReturn(confirmationItemsByQuery);
    }

    /**
     * @param qtyToShip
     * @param product
     */
    @Deprecated
    private void assertSavedItemIsShippingItemAndMatches(int qtyToShip, Product product) {
        captureSavedDeliveryNotes();
        Predicate<? super ReportItem> match = ri -> (ri instanceof ShippingItem)
                && (ri.getQuantity() == qtyToShip)
                && (ri.getOrderItem().getProduct().equals(product));

        assertThat(
                capturedDeliveryNotes.getItems()
                        .stream()
                        .anyMatch(match),
                is(true));
    }

    private ItemDto createOffTheRecordItem() {
        ItemDto offTheRecordItem = createItemDto(null, 10, AMY);
        offTheRecordItem.setOffTheRecord(true);
        return offTheRecordItem;
    }

    private void givenConfirmationItemToBeShipped(long id, Product product, int orderedQty) {
        ConfirmationItem confirmationItemToBeShipped = new ConfirmationItem();
        confirmationItemToBeShipped.setQuantity(1);
        OrderItem orderItem = new OrderItem(null, product, orderedQty);
        confirmationItemToBeShipped.setOrderItem(orderItem);
        when(reportItemRepo.findById(id)).thenReturn(Optional.of(confirmationItemToBeShipped));
    }

    private ItemDto createItemDto(Long id, int qtyLeft, CatalogProduct product) {
        ItemDto item = new ItemDto();
        item.setPriceNet(product.getRecommendedPriceNet().getValue());
        item.setProduct(product.toProduct().getProductNumber());
        item.setProductName(product.getName());
        item.setId(id);
        item.setQuantityLeft(qtyLeft);
        return item;
    }

    private void captureSavedDeliveryNotes() {
        ArgumentCaptor<DeliveryNotes> deliveryNotesCaptor = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(deliveryNotesCaptor.capture());
        this.capturedDeliveryNotes = deliveryNotesCaptor.getValue();
    }

    private void whenDelivering() throws Exception {
        shippingService.ship(deliverParameter);
    }

    private void givenDeliverParameter() {
        deliverParameter = new DeliverParameter();
        deliverParameter.setItems(new ArrayList<ItemDto>());
        deliverParameter.setDeliveryNotesNumber(DN_NO);
    }

    private class ProductQuantityAndCreationDate {
        Product product;
        Integer quantity;
        LocalDateTime creationDate;

        public ProductQuantityAndCreationDate(Product first, Integer second, LocalDateTime creationDate) {
            this.product = first;
            this.quantity = second;
            this.creationDate = creationDate;
        }
    }

}
