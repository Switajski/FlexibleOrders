package de.switajski.priebes.flexibleorders.web;
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

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.service.ItemServiceImpl;

@RequestMapping("/customers")
@Controller
@RooWebScaffold(path = "customers", formBackingObject = Customer.class)
public class CustomerController {

	private ItemRepository itemRepo;
	private CustomerRepository customerRepo;
	private ItemServiceImpl itemService;

	@Autowired
	public CustomerController(CustomerRepository customerRepository, 
			ItemRepository itemRepository) {
		this.itemRepo = itemRepository;
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
		Customer customer = this.customerRepo.findOne(customerId);
		if (customer==null) throw new IllegalArgumentException("Customer with id:"+ customerId +" not found");
		itemType = itemType.toLowerCase();
		if (itemType.contains("ordered")){
			Page<Item> orderItems = itemService.retrieveAllToBeConfirmed(customer, new PageRequest(page-1, limit));
			if (!orderItems.getContent().isEmpty())
				response.setData(orderItems.getContent());
			response.setTotal(orderItems.getTotalElements());
		}
		else if (itemType.contains("confirmed")){
			Page<Item> shippingItems = itemService.retrieveAllToBeShipped(customer, new PageRequest(page-1, limit));
			if (!shippingItems.getContent().isEmpty())
				response.setData(shippingItems.getContent());
			response.setTotal(shippingItems.getTotalElements());
		}
		else if (itemType.contains("shipped")){
			Page<Item> invoiceItems = itemService.retrieveAllDue(customer, new PageRequest(page-1, limit));
			if (!invoiceItems.getContent().isEmpty())
				response.setData(invoiceItems.getContent());
			response.setTotal(invoiceItems.getTotalElements());
		}
		else if (itemType.contains("completed")){
			Page<Item> archiveItems = itemService.retrieveAllCompleted(customer, new PageRequest(page-1, limit));
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
