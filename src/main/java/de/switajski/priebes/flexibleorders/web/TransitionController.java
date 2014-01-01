package de.switajski.priebes.flexibleorders.web;

import java.math.BigDecimal;

import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.PayedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.service.HandlingEventService;

@Controller
@RequestMapping("/transitions")
public class TransitionController {
	
	private static Logger log = Logger.getLogger(TransitionController.class);
	private ItemRepository itemRepo;
	private HandlingEventService heService;

	@Autowired
	public TransitionController(
			HandlingEventService transitionService,
			ItemRepository itemRepo) {
		this.heService = transitionService;
		this.itemRepo = itemRepo;
	}

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(
			@RequestParam(value = "id", required = true) long orderItemId,
//			@RequestParam(value = "orderNumber", required = true) long orderNumber,
//			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber,
			@RequestParam(value = "toSupplier", required = false, defaultValue="false") boolean toSupplier) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request:  orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Item orderItemToConfirm = itemRepo.findOne(orderItemId);
		
		HandlingEvent he = heService.confirm(
				orderItemToConfirm, quantity, 
				new ConfirmedSpecification(orderConfirmationNumber, null, new Amount(BigDecimal.TEN, Currency.EUR), null));

		response.setData(he);
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
		
		if (!itemRepo.exists(shippingItemId))
			throw new NotFoundException("Shipping Item with given Id not found!");
		
		Item shippingItemToDeconfirm =
				itemRepo.findOne(shippingItemId);
		
		HandlingEvent shippingItem = heService.deconfirm(
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
		
		if (!itemRepo.exists(shippingItemId))
			throw new NotFoundException("ShippingItem with given id not found!");
		Item shippingItemToDeliver = itemRepo.findOne(shippingItemId);
		HandlingEvent invoiceItem = heService.deliver(
				shippingItemToDeliver, quantity, new ShippedSpecification(
						invoiceNumber));
		
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
		
		Item item = itemRepo.findOne(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		HandlingEvent he= heService.withdraw(item);
		
		response.setData(he);
		response.setTotal(1);
		response.setMessage("invoice item withdrawed");
		response.setSuccess(true);

		return response;
	}

	
	@RequestMapping(value="/complete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse complete(
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "accountNumber", required = true) Long accountNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: "
				+ " id:" + id);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Item item = itemRepo.findOne(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		HandlingEvent archiveItem = heService.complete(item, quantity, new PayedSpecification(accountNumber));
		
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
		
		Item item = itemRepo.findOne(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		HandlingEvent he = heService.decomplete(item);
		
		response.setData(he);
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
		
		if (itemRepo.exists(id))
			itemRepo.delete(itemRepo.findOne(id));
		else 
			throw new NotFoundException("order item with given id not found!");
		
		response.setTotal(1);
		response.setMessage("order item decompleted");
		response.setSuccess(true);
		
		return response;
	}
	
}
