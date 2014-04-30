package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ItemDtoBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ReportItemServiceTest {

	private String orderNumber = "B123";
	private String confirmNumber = "AB123";
	private static final BigDecimal PRICE_NET = BigDecimal.TEN;
	private static final Integer ORDERED_QUANTITY = 10;
	private static final Integer QUANTITY_TO_CONFIRM = 8;
	private Date expectedDelivery = new Date();
	private List<ItemDto> reportItems;
	private Long CUSTOMER_ID = 41234l;
	private Long PRODUCT_NO = 80912l;
	
	@Mock
	private ReportRepository reportRepo;
	@Mock
	private OrderItemRepository orderItemRepo;
	@Mock
	private CatalogProductRepository cProductRepo;
	@Mock
	private ReportItemRepository heRepo;
	@Mock
	private CustomerRepository customerRepo;
	@Mock
	private OrderRepository orderRepo;

	private OrderServiceImpl orderService;
	
	@Before
	public void setupMocks(){
		Product product = new CatalogProductBuilder("nana", PRODUCT_NO, ProductType.PRODUCT).build().toProduct();
		Order order = new OrderBuilder()
		.setCustomer(CustomerBuilder.buildWithGeneratedAttributes(CUSTOMER_ID.intValue()))
		.build();
		order.addOrderItem(new OrderItemBuilder(order, product, ORDERED_QUANTITY).build());
		
		MockitoAnnotations.initMocks(this);
		orderService = new OrderServiceImpl(reportRepo, customerRepo, orderItemRepo, orderRepo, cProductRepo, heRepo);
		
		Mockito.when(customerRepo.findOne(CUSTOMER_ID))
		.thenReturn(CustomerBuilder.buildWithGeneratedAttributes(2));
		Mockito.when(cProductRepo.findByProductNumber(PRODUCT_NO))
		.thenReturn(CatalogProductBuilder.buildWithGeneratedAttributes(PRODUCT_NO.intValue()));
		Mockito.when(orderRepo.findByOrderNumber(orderNumber))
		.thenReturn(order);
		Mockito.when(orderItemRepo.findOne(Matchers.anyLong()))
		.thenReturn(order.getItems().iterator().next());
	}
	
	@Test
	public void confirm_quantityShouldBeConfirmedQuantity(){
		//GIVEN
		reportItems = givenItemDtos();
		
		//WHEN
		orderService.confirm(orderNumber, confirmNumber, expectedDelivery, reportItems);
		
		//THEN
		ConfirmationReport confirmationReport = captureConfirmationReportToBeSaved();
		
		assertThat(confirmationReport, notNullValue());
		assertThat(confirmationReport.getItems(), not(empty()));
		assertThat(confirmationReport.getItems().iterator().next().getQuantity(), is(QUANTITY_TO_CONFIRM));
	}
	
	private ConfirmationReport captureConfirmationReportToBeSaved() {
		ArgumentCaptor<ConfirmationReport> orderVarToBeSaved = ArgumentCaptor.forClass(ConfirmationReport.class);
		Mockito.verify(reportRepo).save(orderVarToBeSaved.capture());
		ConfirmationReport confirmationReport = orderVarToBeSaved.getValue();
		return confirmationReport;
	}

	private List<ItemDto> givenItemDtos() {
		List<ItemDto> reportItems = new ArrayList<ItemDto>();
		reportItems.add(new ItemDtoBuilder()
			.setId(142L)
			.setCustomer(CUSTOMER_ID)
			.setProduct(PRODUCT_NO)
			.setPriceNet(PRICE_NET)
			.setProductName("productName")
			.setQuantity(ORDERED_QUANTITY)
			.setQuantityLeft(QUANTITY_TO_CONFIRM)
			.build());
		return reportItems;
	}
}
