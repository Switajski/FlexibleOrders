package de.switajski.priebes.flexibleorders.web;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
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

	ShippingItemService shippingItemService;
	
	@Autowired
	public ShippingItemController(
			ShippingItemService crudServiceAdapter) {
		super(crudServiceAdapter);
		this.shippingItemService = crudServiceAdapter;
	}

	@Override
	protected Page<ShippingItem> findByFilterable(PageRequest pageRequest,
			JsonFilter filter) {
		if (filter.value=="") return null;
		return this.shippingItemService.findByOrderNumber(Long.parseLong(filter.value), pageRequest);
	}

	@Override
	protected void resolveDependencies(ShippingItem entity) {
		// TODO Auto-generated method stub
		
	}
}
