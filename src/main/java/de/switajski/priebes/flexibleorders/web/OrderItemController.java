package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Customer;
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

	private static final String ID = "ordernumber";
	private static final String FILTER_STATUS = "ordered";
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
		
		if (hasNoPrice(entity)) 
			throw new IllegalArgumentException("Price of product and order item is not set!");
		entity.setProduct(p);

		if (hasConflictingCustomer(entity))
			throw new IllegalArgumentException("An order item with same order number exists, but has a different customer!");
		
		Customer customer = customerService.find(entity.getCustomer().getId());
		entity.setCustomer(customer);
		
		entity.setQuantityLeft(entity.getQuantity());
	}

	private boolean hasNoPrice(OrderItem entity) {
		return (entity.getPriceNet()==null && 
				productService.findByProductNumber(entity.getProductNumber()).getPriceNet()==null);
	}

	@Override
	protected Page<OrderItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findOpenOrderItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.orderItemService.findByOrderNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<OrderItem> orderItems = orderItemService.findOpen(pageRequest);
				return orderItems;
			}
		return null;
	}
	
	/**
	 * Site is created by Extjs and JSON
	 * @param page
	 * @param size
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "confirm", produces = "text/html")
    public String showConfirmationPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        return "orderitems/confirm";
    }
	
	/**
	 * verifies, that an order has not different customers
	 * @param orderItem
	 */
	private boolean hasConflictingCustomer(OrderItem orderItem) {
		List<OrderItem> orderItems = orderItemService.findByOrderNumber(orderItem.getOrderNumber());
		if (!orderItems.isEmpty()){
			for (OrderItem oi: orderItems)
				if (oi.getOrderNumber()!=orderItem.getOrderNumber()){
					return true;
				}
		}
		return false;
	}


}
