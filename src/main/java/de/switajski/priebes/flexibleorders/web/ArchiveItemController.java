package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.CustomerService;

@RequestMapping("/archiveitems")
@Controller
@RooWebScaffold(path = "archiveitems", formBackingObject = ArchiveItem.class)
public class ArchiveItemController extends JsonController<ArchiveItem> {

	private static final String FILTER_STATUS = "completed";
	private static final String ID = "accountNumber";
	private ArchiveItemService archiveItemService;
	private CustomerService customerService;

	@Autowired
	public ArchiveItemController(
			ArchiveItemService crudServiceAdapter,
			CustomerService customerService) {
		super(crudServiceAdapter);
		this.archiveItemService = crudServiceAdapter;
		this.customerService = customerService;
	}

	@Override
	protected Page<ArchiveItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findCompletedItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.archiveItemService.findByAccountNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<ArchiveItem> items = archiveItemService.findCompleted(pageRequest);
				return items;
			}
		return null;	}

	@Override
	protected void resolveDependencies(ArchiveItem entity) {
		// TODO Auto-generated method stub

	}
}
