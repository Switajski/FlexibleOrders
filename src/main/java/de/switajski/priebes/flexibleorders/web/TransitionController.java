package de.switajski.priebes.flexibleorders.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;
import de.switajski.priebes.flexibleorders.service.ProductService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;
import de.switajski.priebes.flexibleorders.service.TransitionService;

@Controller
@RequestMapping("/transitions")
public class TransitionController {
	
	private static Logger log = Logger.getLogger(TransitionController.class);
	private TransitionService transitionService;
	private CustomerService customerService;
	private ProductService productService;
	private ShippingItemService shippingItemService;
	private InvoiceItemService invoiceItemService;
	private ArchiveItemService archiveItemService;

	@Autowired
	public TransitionController(
			TransitionService transitionService,
			CustomerService customerService,
			ProductService productService,
			ShippingItemService shippingItemService,
			InvoiceItemService invoiceItemService,
			ArchiveItemService archiveItemService) {
		this.transitionService = transitionService;
		this.customerService = customerService;
		this.productService = productService;
		this.shippingItemService = shippingItemService;
		this.invoiceItemService = invoiceItemService;
		this.archiveItemService = archiveItemService;
	}

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(
			@RequestParam(value = "customer", required = true) long customerId,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber,
			@RequestParam(value = "toSupplier", required = false, defaultValue="false") boolean toSupplier) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: customer:"+customerId + " product:"+ productNumber 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Customer customer = customerService.find(customerId);
		Product product = productService.findByProductNumber(productNumber);
		List<ShippingItem> shippingItems = transitionService.confirm(customer, product, quantity, toSupplier, orderConfirmationNumber);
		for (ShippingItem shippingItem:shippingItems)
			shippingItemService.save(shippingItem);
		response.setData(shippingItems);
		response.setTotal(shippingItems.size());
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deconfirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deconfirm(
			@RequestParam(value = "customer", required = true) long customerId,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber) 
					throws Exception {
		
		log.debug("received json deconfirm request: customer:"+customerId + " product:"+ productNumber 
				+ " orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Customer customer = customerService.find(customerId);
		Product product = productService.findByProductNumber(productNumber);
		ShippingItem shippingItem = transitionService.deconfirm(customer, product, orderConfirmationNumber);
		
		response.setData(shippingItem);
		response.setTotal(1);
		response.setMessage("order item deconfirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deliver/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deliver(
			@RequestParam(value = "customer", required = true) long customerId,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "invoiceNumber", required = true) long invoiceNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: customer:"+customerId + " product:"+ productNumber 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+invoiceNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Customer customer = customerService.find(customerId);
		Product product = productService.findByProductNumber(productNumber);
		List<InvoiceItem> shippingItems = transitionService.deliver(customer, product, quantity, invoiceNumber);
		for (InvoiceItem shippingItem:shippingItems)
			invoiceItemService.save(shippingItem);
		response.setData(shippingItems);
		response.setTotal(shippingItems.size());
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/withdraw/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse withdraw(
			@RequestParam(value = "customer", required = true) long customerId,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "invoiceNumber", required = true) long invoiceNumber,
			@RequestParam(value = "quantity", required = true) int quantity) 
					throws Exception {
		
		log.debug("received json withdraw request: customer:"+customerId + " product:"+ productNumber 
				+ " invoiceNumber:"+invoiceNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Customer customer = customerService.find(customerId);
		Product product = productService.findByProductNumber(productNumber);
		InvoiceItem invoiceItem = transitionService.withdraw(customer, product, invoiceNumber, quantity);
		
		response.setData(invoiceItem);
		response.setTotal(1);
		response.setMessage("invoice item withdrawed");
		response.setSuccess(true);

		return response;
	}

	
	@RequestMapping(value="/complete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse complete(
			@RequestParam(value = "customer", required = true) long customerId,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "accountNumber", required = true) long accountNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: customer:"+customerId + " product:"+ productNumber 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+accountNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Customer customer = customerService.find(customerId);
		Product product = productService.findByProductNumber(productNumber);
		List<ArchiveItem> shippingItems = transitionService.complete(customer, product, quantity, accountNumber);
		for (ArchiveItem shippingItem:shippingItems)
			archiveItemService.save(shippingItem);
		response.setData(shippingItems);
		response.setTotal(shippingItems.size());
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}

	
}
