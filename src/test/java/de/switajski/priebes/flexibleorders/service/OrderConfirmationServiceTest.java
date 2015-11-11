package de.switajski.priebes.flexibleorders.service;

import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AB11;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AMY;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B11;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B12;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.DHL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.JUREK;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.MILADKA;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.PAUL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.SALOME;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.YVONNE;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.api.OrderConfirmationService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderConfirmationServiceTest {
	
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
    private ItemDtoConverterService itemDtoConverterService;
    @Mock
    private CatalogProductServiceByMagento catalogProductService;
    @Mock
    private CatalogDeliveryMethodRepository catalogDeliveryMethod;
    @Mock
    private OrderItemRepository orderItemRepo;
    @InjectMocks
    private OrderConfirmationService orderConfirmationService = new OrderConfirmationService();


	@Test
    public void shouldConfirmWithNoId() {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(customerRepo.findByCustomerNumber(YVONNE.getCustomerNumber())).thenReturn(YVONNE);
        when(catalogDeliveryMethod.findOne(AB11.deliveryMethodNo)).thenReturn(new CatalogDeliveryMethod(DHL));

        Order b11 = new OrderBuilder().withB11().build();
        Order b12 = new OrderBuilder().withB12().build();
        when(orderRepo.findByOrderNumber(B11.orderNumber)).thenReturn(b11);
        when(orderRepo.findByOrderNumber(B11.orderNumber)).thenReturn(b12);
        when(orderItemRepo.findByOrderNumber(B11.orderNumber)).thenReturn(new ArrayList<OrderItem>(b11.getItems()));
        when(orderItemRepo.findByOrderNumber(B12.orderNumber)).thenReturn(new ArrayList<OrderItem>(b12.getItems()));
        
        setupProducts(MILADKA, SALOME, PAUL, JUREK, AMY);

        // WHEN
        orderConfirmationService.confirm(AB11);

        // THEN
        verify(reportRepository).save(argThat(hasSameProductNumbersAs(AB11.orderItems)));
    }
	
    private void setupProducts(CatalogProduct... products) {
        for (CatalogProduct product : products)
            when(catalogProductService.findByProductNumber(product.getProductNumber())).thenReturn(product);
    }
    
	public ArgumentMatcher<Report> hasSameProductNumbersAs(Collection<ItemDto> items){
        return new ProductNumbersMatcher(items);
    }

    class ProductNumbersMatcher extends ArgumentMatcher<Report> {

        private HashSet<String> productNumbersShouldBe;

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof OrderConfirmation)) return false;

            OrderConfirmation oc = (OrderConfirmation) argument;
            HashSet<String> productNumbersIs = createProductNumbers(oc);
            
            return productNumbersIs.equals(productNumbersShouldBe);
        }
        

        public ProductNumbersMatcher(Collection<ItemDto> items) {
            productNumbersShouldBe = new HashSet<String>();
            for (ItemDto item:items)
                productNumbersShouldBe.add(item.product);
        }


        private HashSet<String> createProductNumbers(OrderConfirmation oc) {
            HashSet<String> productNumbersIs = new HashSet<String>();
            for (ReportItem i : oc.getItems()){
                String productNumber = i.getOrderItem().getProduct().getProductNumber();
                productNumbersIs.add(productNumber);
            }
            return productNumbersIs;
        }
        
    }


}
