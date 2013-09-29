package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.CustomerService;

@RequestMapping("/customers")
@Controller
@RooWebScaffold(path = "customers", formBackingObject = Customer.class)
public class CustomerController extends JsonController<Customer>{

	@Autowired
	public CustomerController(CustomerService crudServiceAdapter) {
		super(crudServiceAdapter);
	}

	@Override
	protected void resolveDependencies(Customer entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Page<Customer> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping(value = "/json/getItems", method=RequestMethod.GET)
    public @ResponseBody JsonObjectResponse findOrderedItems( 
    		@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "customer", required = true) Long customerId,
			@RequestParam(value = "itemType", required = true) String itemType) {
		JsonObjectResponse response = new JsonObjectResponse();
		Customer customer = customerService.find(customerId);
		if (customer==null) throw new IllegalArgumentException("Customer with id:"+ customerId +" not found");
		itemType = itemType.toLowerCase();
		if (itemType.contains("ordered")){
			Page<OrderItem> orderItems = customerService.findOpenOrderItems(customer, new PageRequest(page-1, limit));
			if (!orderItems.getContent().isEmpty())
				response.setData(orderItems.getContent());
			response.setTotal(orderItems.getTotalElements());
		}
		else if (itemType.contains("confirmed")){
			Page<ShippingItem> shippingItems = customerService.findOpenShippingItems(customer, new PageRequest(page-1, limit));
			if (!shippingItems.getContent().isEmpty())
				response.setData(shippingItems.getContent());
			response.setTotal(shippingItems.getTotalElements());
		}
		else if (itemType.contains("shipped")){
			Page<InvoiceItem> invoiceItems = customerService.findOpenInvoiceItems(customer, new PageRequest(page-1, limit));
			if (!invoiceItems.getContent().isEmpty())
				response.setData(invoiceItems.getContent());
			response.setTotal(invoiceItems.getTotalElements());
		}
		else if (itemType.contains("completed")){
			Page<ArchiveItem> archiveItems = customerService.findOpenArchiveItems(customer, new PageRequest(page-1, limit));
			if (!archiveItems.getContent().isEmpty())
				response.setData(archiveItems.getContent());
			response.setTotal(archiveItems.getTotalElements());
		}
		else 
			throw new IllegalArgumentException("No valid item type found");
		
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
    }
	
	@RequestMapping(value = "listitems", produces = "text/html")
    public String confirm(Model uiModel) {
        return "customers/listitems";
    }
	
}
