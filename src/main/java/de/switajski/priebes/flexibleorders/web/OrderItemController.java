package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.ProductService;

@RequestMapping("/orderitems")
@Controller
@RooWebScaffold(path = "orderitems", formBackingObject = OrderItem.class)
public class OrderItemController extends JsonController<OrderItem>{

	private ProductService productService;
	private OrderItemService orderItemService;
	private CustomerService customerService;
	
	@Autowired
	public OrderItemController(OrderItemService readService,
			ProductService productService,
			OrderItemService orderItemService,
			CustomerService customerService) {
		super(readService);
		this.productService = productService;
		this.orderItemService = orderItemService;
		this.customerService = customerService;
	}
	
	@RequestMapping(value="/json", params="orderNumber", headers = "Accept=application/json")
	public @ResponseBody JsonObjectResponse listByOrderNumber(
            @RequestParam(value = "orderNumber", required = true) Long orderNumber) {
		JsonObjectResponse response = new JsonObjectResponse();
		List<OrderItem> entities =  orderItemService.findByOrderNumber(orderNumber);
		response.setMessage("All order items retrieved.");
		response.setSuccess(true);
		response.setTotal(entities.size());
		response.setData(entities);
		
		return response;
	}

	@Override
	protected void resolveDependencies(OrderItem entity) {
		long productNumber = entity.getProduct().getProductNumber();
		Product p = productService.findByProductNumber(productNumber);
		if (entity.getPriceNet()==null && p.getPriceNet()==null) 
			throw new IllegalArgumentException("Price of product and order item is not set!");
		entity.setProduct(p);
		
		Customer customer = customerService.find(entity.getCustomer().getId());
		entity.setCustomer(customer);
	}

	@Override
	protected Page<OrderItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("OrderNumber")!=null) 
		 	if (filter.get("OrderNumber")=="") 
		 		return null;
		 	else
		 		return this.orderItemService.findByOrderNumber(Long.parseLong(filter.get("OrderNumber")), pageRequest);
		else if (filter.get("Status")=="ORDERED")
			return orderItemService.findOrdered(pageRequest);
		else return null;
	}

}
