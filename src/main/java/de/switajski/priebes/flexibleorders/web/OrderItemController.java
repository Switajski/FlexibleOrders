package de.switajski.priebes.flexibleorders.web;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import flexjson.JSONSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/orderitems")
@Controller
@RooWebScaffold(path = "orderitems", formBackingObject = OrderItem.class)
public class OrderItemController extends JsonController<OrderItem>{

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

}
