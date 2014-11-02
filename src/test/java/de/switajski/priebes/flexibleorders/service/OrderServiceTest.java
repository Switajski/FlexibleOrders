package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.api.OrderService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderServiceTest {

    private static final Integer ORDERED_QUANTITY = 3;
    private static final String PRODUCT_NO = "3";
    private static final Long CUSTOMER_ID = 2l;
    private static final BigDecimal PRICE_NET = new BigDecimal(5.55);

    @Mock
    private ReportRepository reportRepoMock;
    @Mock
    private OrderItemRepository orderItemRepoMock;
    @Mock
    private CatalogProductRepository cProductRepoMock;
    @Mock
    private ReportItemRepository heRepoMock;
    @Mock
    private CustomerRepository customerRepoMock;
    @Mock
    private OrderRepository orderRepoMock;
    @Mock
    private ItemDtoConverterService itemDtoConverterService;
    @Mock
    private CatalogProductServiceImpl catalogProductService;
    @InjectMocks
    private OrderService orderService = new OrderService();

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(customerRepoMock.findByCustomerNumber(CUSTOMER_ID))
                .thenReturn(CustomerBuilder.buildWithGeneratedAttributes(2));
        Mockito.when(catalogProductService.findByProductNumber(PRODUCT_NO))
                .thenReturn(CatalogProductBuilder.buildWithGeneratedAttributes(Integer.valueOf(PRODUCT_NO)));

    }

    @Test
    public void shouldOrder() {
        orderService.order(new OrderParameter(CUSTOMER_ID, "123", new Date(), givenReportItems()));

        ArgumentCaptor<Order> orderVarToBeSaved = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepoMock).save(orderVarToBeSaved.capture());

        assertSavedAsExpected(orderVarToBeSaved.getValue());
    }

    private void assertSavedAsExpected(Order orderToBeSaved) {
        assertNotNull(orderToBeSaved.getItems());

        OrderItem orderItem = orderToBeSaved.getItems().iterator().next();
        assertEquals(ORDERED_QUANTITY, orderItem.getOrderedQuantity());
        assert (orderItem.getReportItems().isEmpty());
        assertEquals(new Amount((PRICE_NET), Currency.EUR), orderItem.getNegotiatedPriceNet());
    }

    private List<ItemDto> givenReportItems() {
        List<ItemDto> reportItems = new ArrayList<ItemDto>();
        ItemDto itemDto = new ItemDto();
        itemDto.customer = CUSTOMER_ID;
        itemDto.product = PRODUCT_NO;
        itemDto.priceNet = PRICE_NET;
        itemDto.productName = "productName";
        itemDto.quantity = ORDERED_QUANTITY;
        reportItems.add(itemDto);
        return reportItems;
    }

}
