package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shippingitems")
@Controller
@RooWebScaffold(path = "shippingitems", formBackingObject = ShippingItem.class)
public class ShippingItemController extends JsonController<ShippingItem> {

	private static final String FILTER_STATUS = "confirmed";
	private static final String ID = "orderConfirmationNumber";
	private ShippingItemService shippingItemService;
	private OrderItemRepository orderItemRepository;

	@Autowired
	public ShippingItemController(
			ShippingItemService crudServiceAdapter,
			OrderItemRepository orderItemService) {
		super(crudServiceAdapter);
		this.shippingItemService = crudServiceAdapter;
		this.orderItemRepository = orderItemService;
	}

	@Override
	protected Page<ShippingItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findConfirmedItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.shippingItemService.findByOrderNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<ShippingItem> items = shippingItemService.findConfirmed(pageRequest);
				return items;
			}
		return null;
	}

	@Override
	protected void resolveDependencies(ShippingItem entity) {
		// TODO Auto-generated method stub

	}

	@Override
	void deleteStepBackward(ShippingItem item) {
		List<OrderItem> ois = orderItemRepository.findByOrderNumber(item.getOrderNumber());
		for (OrderItem oi:ois){
			if(oi.getProduct().equals(item.getProduct())){
				oi.stepBackward();
				orderItemRepository.delete(oi);
				orderItemRepository.saveAndFlush(oi);
				
			}
		}
		
	}
}
