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
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.AccountParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ShippingParameter;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;
import de.switajski.priebes.flexibleorders.service.TransitionService;

@Controller
@RequestMapping("/transitions")
public class TransitionController {
	
	private static Logger log = Logger.getLogger(TransitionController.class);
	private TransitionService transitionService;
	private ShippingItemService shippingItemService;
	private InvoiceItemService invoiceItemService;
	private ArchiveItemService archiveItemService;
	private OrderItemRepository orderItemRepository;
	private ShippingItemRepository shippingItemRepository;

	@Autowired
	public TransitionController(
			TransitionService transitionService,
			ShippingItemService shippingItemService,
			InvoiceItemService invoiceItemService,
			ArchiveItemService archiveItemService,
			OrderItemRepository orderItemRepository,
			ShippingItemRepository shippingItemRepository) {
		this.transitionService = transitionService;
		this.shippingItemService = shippingItemService;
		this.invoiceItemService = invoiceItemService;
		this.archiveItemService = archiveItemService;
		this.orderItemRepository = orderItemRepository;
		this.shippingItemRepository = shippingItemRepository;
	}

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(
			@RequestParam(value = "id", required = true) long orderItemId,
//			@RequestParam(value = "orderNumber", required = true) long orderNumber,
//			@RequestParam(value = "productNumber", required = true) long productNumber, 
//			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber,
			@RequestParam(value = "toSupplier", required = false, defaultValue="false") boolean toSupplier) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request:  orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		OrderItem orderItemToConfirm = orderItemRepository.findOne(orderItemId);
		
		ShippingItem shippingItem = transitionService.confirm(
				orderItemToConfirm, new ConfirmationParameter(toSupplier, orderConfirmationNumber));

		response.setData(shippingItem);
		response.setTotal(1);
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deconfirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deconfirm(
			@RequestParam(value = "id", required = true) long shippingItemId,
//			@RequestParam(value = "orderNumber", required = true) long orderNumber,
//			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber) 
					throws Exception {
		
		log.debug("received json deconfirm request: " 
				+ " orderConfirmationNumber:" + orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		if (!shippingItemRepository.exists(shippingItemId))
			throw new NotFoundException("Shipping Item with given Id not found!");
		
		ShippingItem shippingItemToDeconfirm =
				shippingItemService.find(shippingItemId);
		
		ShippingItem shippingItem = transitionService.deconfirm(
				shippingItemToDeconfirm);
		
		response.setData(shippingItem);
		response.setTotal(1);
		response.setMessage("order item deconfirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deliver/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deliver(
			@RequestParam(value = "id", required = true) long shippingItemId,
//			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber,
//			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "invoiceNumber", required = true) long invoiceNumber,
			@RequestParam(value = "trackNumber", required = false) String trackNumber,
			@RequestParam(value = "packageNumber", required = false) String packageNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: orderConfirmationNumber:" 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+invoiceNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		if (!shippingItemRepository.exists(shippingItemId))
			throw new NotFoundException("ShippingItem with given id not found!");
		ShippingItem shippingItemToDeliver = shippingItemRepository.findOne(shippingItemId);
		InvoiceItem invoiceItem = transitionService.deliver(
				shippingItemToDeliver, new ShippingParameter(
						quantity, invoiceNumber, 
						shippingItemToDeliver.getCustomer().getShippingAddress()));
		
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
		ArchiveItem archiveItem = transitionService.complete(item, new AccountParameter(accountNumber));
		
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
	
	@RequestMapping(value="/delete/json", method=RequestMethod.DELETE)
	public @ResponseBody JsonObjectResponse delete(
			@RequestParam(value = "id", required = true) long id) 
					throws NotFoundException {
		JsonObjectResponse response = new JsonObjectResponse();
		
		if (orderItemRepository.exists(id))
			orderItemRepository.delete(orderItemRepository.findOne(id));
		else 
			throw new NotFoundException("order item with given id not found!");
		
		response.setTotal(1);
		response.setMessage("order item decompleted");
		response.setSuccess(true);
		
		return response;
	}
	
}
