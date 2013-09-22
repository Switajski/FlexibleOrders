package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;

@RequestMapping("/archiveitems")
@Controller
@RooWebScaffold(path = "archiveitems", formBackingObject = ArchiveItem.class)
public class ArchiveItemController extends JsonController<ArchiveItem> {

	private static final String FILTER_STATUS = "completed";
	private static final String ID = "accountNumber";
	private ArchiveItemService archiveItemService;
	private CustomerService customerService;
	private InvoiceItemRepository invoiceItemRepository;

	@Autowired
	public ArchiveItemController(
			ArchiveItemService crudServiceAdapter,
			CustomerService customerService,
			InvoiceItemRepository invoiceItemRepository) {
		super(crudServiceAdapter);
		this.archiveItemService = crudServiceAdapter;
		this.customerService = customerService;
		this.invoiceItemRepository = invoiceItemRepository;
	}

	@Override
	protected Page<ArchiveItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findOpenArchiveItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.archiveItemService.findByAccountNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<ArchiveItem> items = archiveItemService.findOpen(pageRequest);
				return items;
			}
		return null;	}

	@Override
	protected void resolveDependencies(ArchiveItem entity) {
		// TODO Auto-generated method stub

	}

	@Override
	void deleteStepBackward(ArchiveItem item) {
		List<InvoiceItem> ois = invoiceItemRepository.findByOrderNumber(item.getOrderNumber());
		for (InvoiceItem oi:ois){
			if(oi.getProduct().equals(item.getProduct())){
				oi.stepBackward();
				invoiceItemRepository.delete(oi);
				invoiceItemRepository.saveAndFlush(oi);
				
			}

		}
	}
}
