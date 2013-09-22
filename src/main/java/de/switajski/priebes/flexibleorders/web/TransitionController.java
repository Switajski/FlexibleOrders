package de.switajski.priebes.flexibleorders.web;

import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.ProductService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;
import de.switajski.priebes.flexibleorders.service.TransitionService;

@Controller
@RequestMapping("/transitions")
public class TransitionController {
	
	private static Logger log = Logger.getLogger(TransitionController.class);
	private TransitionService transitionService;
	private ProductService productService;
	private ShippingItemService shippingItemService;
	private InvoiceItemService invoiceItemService;
	private ArchiveItemService archiveItemService;

	@Autowired
	public TransitionController(
			TransitionService transitionService,
			ProductService productService,
			ShippingItemService shippingItemService,
			InvoiceItemService invoiceItemService,
			ArchiveItemService archiveItemService) {
		this.transitionService = transitionService;
		this.productService = productService;
		this.shippingItemService = shippingItemService;
		this.invoiceItemService = invoiceItemService;
		this.archiveItemService = archiveItemService;
	}

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(
			@RequestParam(value = "orderNumber", required = true) long orderNumber,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber,
			@RequestParam(value = "toSupplier", required = false, defaultValue="false") boolean toSupplier) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: orderNumber:"+orderNumber + " product:"+ productNumber 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Product product = productService.findByProductNumber(productNumber);
		ShippingItem shippingItem = transitionService.confirm(orderNumber, product, quantity, toSupplier, orderConfirmationNumber);
			shippingItemService.save(shippingItem);
		response.setData(shippingItem);
		response.setTotal(1);
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deconfirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deconfirm(
			@RequestParam(value = "orderNumber", required = true) long orderNumber,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber) 
					throws Exception {
		
		log.debug("received json deconfirm request: orderNumber:"+orderNumber + " product:"+ productNumber 
				+ " orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Product product = productService.findByProductNumber(productNumber);
		ShippingItem shippingItem = transitionService.deconfirm(orderNumber, product, orderConfirmationNumber);
		
		response.setData(shippingItem);
		response.setTotal(1);
		response.setMessage("order item deconfirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deliver/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deliver(
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "invoiceNumber", required = true) long invoiceNumber,
			@RequestParam(value = "trackNumber", required = false) String trackNumber,
			@RequestParam(value = "packageNumber", required = false) String packageNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: orderConfirmationNumber:"+orderConfirmationNumber + " product:"+ productNumber 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+invoiceNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Product product = productService.findByProductNumber(productNumber);
		InvoiceItem invoiceItem = transitionService.deliver(orderConfirmationNumber, product, quantity, 
				invoiceNumber, trackNumber, packageNumber);
		//invoiceItemService.save(invoiceItem);
		response.setData(invoiceItem);
		response.setTotal(1);
		response.setMessage("invoice item confirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/withdraw/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse withdraw(
			@RequestParam(value = "id", required = true) long id) 
					throws Exception {
		
		log.debug("received json withdraw request:" 
				+ " invoiceItemId:"+id);
		JsonObjectResponse response = new JsonObjectResponse();
		
		InvoiceItem item = invoiceItemService.find(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		item = transitionService.withdraw(item);
		
		response.setData(item);
		response.setTotal(1);
		response.setMessage("invoice item withdrawed");
		response.setSuccess(true);

		return response;
	}

	
	@RequestMapping(value="/complete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse complete(
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "accountNumber", required = true) Long accountNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: "
				+ " id:" + id);
		JsonObjectResponse response = new JsonObjectResponse();
		
		InvoiceItem item = invoiceItemService.find(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		ArchiveItem archiveItem = transitionService.complete(item, accountNumber);
		
		response.setData(archiveItem);
		response.setTotal(1);
		response.setMessage("archiveItem iten confirmed");
		response.setSuccess(true);

		return response;
	}

	@RequestMapping(value="/decomplete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse decomplete(
			@RequestParam(value = "id", required = true) long id) 
					throws Exception {
		
		log.debug("received json decomplete request:" 
				+ " archiveItemId:"+id);
		JsonObjectResponse response = new JsonObjectResponse();
		
		ArchiveItem item = archiveItemService.find(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		item = transitionService.decomplete(item);
		
		response.setData(item);
		response.setTotal(1);
		response.setMessage("archive item decompleted");
		response.setSuccess(true);

		return response;
	}
	
}
