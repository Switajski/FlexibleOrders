package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.ProductService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

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

	private static final String FILTER_STATUS = "shipped";
	private static final String ID = "invoiceNumber";
	private InvoiceItemService invoiceItemService;
	private CustomerService customerService;
	private ShippingItemRepository shippingItemRepository;
	private ProductService productService;

	@Autowired
	public InvoiceItemController(InvoiceItemService readService,
			CustomerService customerService,
			ShippingItemRepository shippingItemRepository,
			ProductService productService) {
		super(readService);
		this.invoiceItemService = readService;
		this.customerService = customerService;
		this.shippingItemRepository = shippingItemRepository;
		this.productService = productService;
	}

	@Override
	protected void resolveDependencies(InvoiceItem entity) {
		long productNumber = entity.getProduct().getProductNumber();
		Product p = productService.findByProductNumber(productNumber);
		
		if (hasConflictingCustomer(entity))
			throw new IllegalArgumentException("An order item with same order number exists, but has a different customer!");
		
		Customer customer = customerService.find(entity.getCustomer().getId());
		entity.setCustomer(customer);

	}

	@Override
	protected Page<InvoiceItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
				return customerService.findOpenInvoiceItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.invoiceItemService.findByInvoiceNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<InvoiceItem> items = invoiceItemService.findOpen(pageRequest);
				return items;
			}
		return null;
	}

	@Override
	void deleteStepBackward(InvoiceItem item) {
		List<ShippingItem> ois = shippingItemRepository.findByOrderNumber(item.getOrderNumber());
		for (ShippingItem oi:ois){
			if(oi.getProduct().equals(item.getProduct())){
				oi.stepBackward();
				shippingItemRepository.delete(oi);
				shippingItemRepository.saveAndFlush(oi);
			}

		}
	}

	//TODO: DRY in OrderItemController - same Method, but different Entity
	/**
	 * verifies, that an order has not different customers
	 * @param orderItem
	 */
	private boolean hasConflictingCustomer(InvoiceItem invoiceItem) {
		List<InvoiceItem> invoiceItems = invoiceItemService.findByInvoiceNumber(invoiceItem.getInvoiceNumber());
		if (!invoiceItems.isEmpty()){
			for (InvoiceItem oi: invoiceItems)
				if (oi.getOrderNumber()!=invoiceItem.getInvoiceNumber()){
					return true;
				}
		}
		return false;
	}

}
