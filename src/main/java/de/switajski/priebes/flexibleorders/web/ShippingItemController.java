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

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.service.ProductService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

@RequestMapping("/shippingitems")
@Controller
@RooWebScaffold(path = "shippingitems", formBackingObject = ShippingItem.class)
public class ShippingItemController extends JsonController<ShippingItem> {

	private static final String FILTER_STATUS = "confirmed";
	private static final String ID = "orderConfirmationNumber";
	private ShippingItemService shippingItemService;
	private OrderItemRepository orderItemRepository;
	private ProductService productService;

	@Autowired
	public ShippingItemController(
			ShippingItemService crudServiceAdapter,
			OrderItemRepository orderItemService,
			ProductService productService) {
		super(crudServiceAdapter);
		this.shippingItemService = crudServiceAdapter;
		this.orderItemRepository = orderItemService;
		this.productService = productService;
	}

	@Override
	protected Page<ShippingItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findOpenShippingItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.shippingItemService.findByOrderNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<ShippingItem> items = shippingItemService.findOpen(pageRequest);
				return items;
			}
		return null;
	}

	@Override
	protected void resolveDependencies(ShippingItem entity) {
		long productNumber = entity.getProduct().getProductNumber();
		entity.setProduct(productService.findByProductNumber(productNumber));
		
		Customer customer = customerService.find(entity.getCustomer().getId());
		entity.setCustomer(customer);

		entity.setQuantityLeft(entity.getQuantity());
	}

	/**
	 * Site is created by Extjs and JSON
	 * @param page
	 * @param size
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "deliver", produces = "text/html")
    public String showDeliverPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        return "shippingitems/deliver";
    }
}
