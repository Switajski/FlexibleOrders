package de.switajski.priebes.flexibleorders.web;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.ProductService;

@RequestMapping("/orderitems")
@Controller
@RooWebScaffold(path = "orderitems", formBackingObject = OrderItem.class)
public class OrderItemController extends JsonController<OrderItem>{

	@Autowired ProductService productService;
	
	@Autowired
	public OrderItemController(OrderItemService readService) {
		super(readService);
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
		entity.setProduct(p);
		
		customerService.find(entity.getCustomer().getId());
	}

}
