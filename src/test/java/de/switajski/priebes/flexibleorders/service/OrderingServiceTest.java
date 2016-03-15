package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderingServiceTest {

    private static final Integer ORDERED_QUANTITY = 3;
    private static final String PRODUCT_NO = "3";
    private static final Long CUSTOMER_ID = 2l;
    private static final BigDecimal PRICE_NET = new BigDecimal(5.55);

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private OrderItemRepository orderItemRepoMock;
    @Mock
    private CatalogProductRepository cProductRepoMock;
    @Mock
    private ReportItemRepository heRepoMock;
    @Mock
    private CustomerRepository customerRepo;
    @Mock
    private OrderRepository orderRepo;
    @Mock
    private CatalogProductServiceByMagento catalogProductService;
    @Mock
    private CatalogDeliveryMethodRepository catalogDeliveryMethod;
    @Mock
    private OrderItemRepository orderItemRepo;
    @InjectMocks
    private TransitionsService orderingService = new TransitionsService();

    @Test
    public void shouldOrder() {
        givenMocks();

        orderingService.order(new OrderParameter(CUSTOMER_ID, "123", new Date(), givenReportItems()));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        assertSavedAsExpected(orderCaptor.getValue());
    }

    private void givenMocks() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(customerRepo.findByCustomerNumber(CUSTOMER_ID))
                .thenReturn(CustomerBuilder.buildWithGeneratedAttributes(2));
        Mockito.when(catalogProductService.findByProductNumber(PRODUCT_NO))
                .thenReturn(CatalogProductBuilder.buildWithGeneratedAttributes(Integer.valueOf(PRODUCT_NO)));
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
        itemDto.setCustomer(CUSTOMER_ID);
        itemDto.setProduct(PRODUCT_NO);
        itemDto.setPriceNet(PRICE_NET);
        itemDto.setProductName("productName");
        itemDto.setQuantity(ORDERED_QUANTITY);
        reportItems.add(itemDto);
        return reportItems;
    }

}
