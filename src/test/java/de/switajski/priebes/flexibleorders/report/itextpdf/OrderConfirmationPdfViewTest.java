package de.switajski.priebes.flexibleorders.report.itextpdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OrderItemDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderConfirmationPdfViewTest {
		
	public static final String RESULT
    = "src/test/java/de/switajski/priebes/flexibleorders/report/itextpdf/ab1234.pdf";
	
	Order order;
	OrderConfirmation orderConfirmation;
	
	@Autowired OrderService orderService;
	@Autowired OrderItemService orderItemService;
	@Autowired ShippingItemService shippingItemService;
	
	@Before
	public void initData(){
		OrderItemDataOnDemand dod = new OrderItemDataOnDemand();
		OrderItem oi1 = dod.getRandomOrderItem();
		OrderItem oi2 = dod.getRandomOrderItem();
		
		oi2.setOrderNumber(oi1.getOrderNumber());
		OrderItem merged = (OrderItem) orderItemService.updateOrderItem(oi2);
		
		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(oi1);
		orderItems.add(merged);
		
		Order order = new Order(orderItems);
		
		List<ShippingItem> shippingItems = new ArrayList<ShippingItem>();
		for (OrderItem orderItem:order.getItems()){
			ShippingItem si = orderItem.confirm(false, orderItem.getQuantity(), 1234l);
			shippingItemService.saveShippingItem(si);
			shippingItems.add(si);
		}
		orderConfirmation = new OrderConfirmation(shippingItems);
	}
	
	@Transactional
	@Test
	public void shouldGenerateOrderConfirmation(){
		OrderConfirmationPdfView bpv = new OrderConfirmationPdfView();
		
		Document document = new Document();
        PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("OrderConfirmation", orderConfirmation);
			document.open();
			bpv.buildPdfDocument(model, document, writer, null, null);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	@Transactional
	@Test
	public void dataInitiatedShouldHaveShippingItems(){
		List<OrderItem> orderItems = order.getItems();
		assertEquals(orderItems.size(),5);
		
		List<ShippingItem> shippingItems = orderConfirmation.getItems();
		assertEquals(shippingItems.size(),5);
	}
	
}
