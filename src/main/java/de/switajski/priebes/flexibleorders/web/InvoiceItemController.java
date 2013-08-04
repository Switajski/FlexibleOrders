package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/invoiceitems")
@Controller
@RooWebScaffold(path = "invoiceitems", formBackingObject = InvoiceItem.class)
public class InvoiceItemController extends JsonController<InvoiceItem>{

	private static final String FILTER_STATUS = "SHIPPED";
	private static final String ID = "invoiceNumber";
	private InvoiceItemService invoiceItemService;
	private CustomerService customerService;

	@Autowired
	public InvoiceItemController(InvoiceItemService readService,
			CustomerService customerService) {
		super(readService);
		this.invoiceItemService = readService;
		this.customerService = customerService;
	}

	@Override
	protected void resolveDependencies(InvoiceItem entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Page<InvoiceItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findShippedItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.invoiceItemService.findByInvoiceNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<InvoiceItem> items = invoiceItemService.findShipped(pageRequest);
				return items;
			}
		return null;
	}
}
