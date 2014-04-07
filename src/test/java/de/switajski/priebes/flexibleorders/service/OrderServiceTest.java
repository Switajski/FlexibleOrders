package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ItemDtoBuilder;
import de.switajski.priebes.flexibleorders.web.entities.ItemDto;

public class OrderServiceTest {
	
	private static final int GENERATE_NUMBER = 2;
	private static final Integer ORDERED_QUANTITY = 3;
	private static final Long PRODUCT_NO = 3l;
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
	
	private OrderServiceImpl orderService;
	
	@Before
	public void setupMocks(){
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(customerRepoMock.findOne(CUSTOMER_ID))
			.thenReturn(CustomerBuilder.buildWithGeneratedAttributes(2));
		Mockito.when(cProductRepoMock.findByProductNumber(PRODUCT_NO))
			.thenReturn(CatalogProductBuilder.buildWithGeneratedAttributes(PRODUCT_NO.intValue()));
		
		orderService = new OrderServiceImpl(reportRepoMock, customerRepoMock, orderItemRepoMock, 
				orderRepoMock, cProductRepoMock, heRepoMock);
	}
	
	@Test
	public void shouldOrder(){
		orderService.order(CUSTOMER_ID, "123", givenReportItems());
		
		ArgumentCaptor<Order> orderVarToBeSaved = ArgumentCaptor.forClass(Order.class);
		Mockito.verify(orderRepoMock).save(orderVarToBeSaved.capture());
		
		assertSavedAsExpected(orderVarToBeSaved.getValue());
	}

	private void assertSavedAsExpected(Order orderToBeSaved) {
		assertNotNull(orderToBeSaved.getItems());
		
		OrderItem orderItem = orderToBeSaved.getItems().iterator().next();
		assertEquals(ORDERED_QUANTITY, orderItem.getOrderedQuantity());
		assert(orderItem.getDeliveryHistory().isEmpty());
		assertEquals(new Amount((PRICE_NET), Currency.EUR), orderItem.getNegotiatedPriceNet());
		assert(new OrderedSpecification().isSatisfiedBy(orderItem));
	}

	private List<ItemDto> givenReportItems() {
		List<ItemDto> reportItems = new ArrayList<ItemDto>();
		reportItems.add(new ItemDtoBuilder()
			.setCustomer(CUSTOMER_ID)
			.setProduct(PRODUCT_NO)
			.setPriceNet(PRICE_NET)
			.setProductName("productName")
			.setQuantity(ORDERED_QUANTITY)
			.build());
		return reportItems;
	}
	
}
