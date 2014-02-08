package de.switajski.priebes.flexibleorders.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@Controller
@RequestMapping("/transitions")
public class TransitionsController {
	
	private static Logger log = Logger.getLogger(TransitionsController.class);
	private OrderItemRepository itemRepo;
	private ReportRepository reportRepo;
	private OrderServiceImpl heService;

	@Autowired
	public TransitionsController(
			OrderServiceImpl transitionService,
			OrderItemRepository itemRepo,
			ReportRepository reportRepo) {
		this.heService = transitionService;
		this.itemRepo = itemRepo;
		this.reportRepo = reportRepo;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleIllegalArgumentException(IllegalArgumentException ex) {
		//TODO: Exception handling
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Funktion mit falschen Parameter aufgerufen";
		return ex.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleException(IllegalArgumentException ex) {
		//TODO: Exception handling
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Fehler beim Server";
		return ex.getMessage();
	}

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(
			@RequestParam(value = "id", required = true) long orderItemId,
			@RequestParam(value = "productNumber", required = true) long productNumber, 
			@RequestParam(value = "priceNet", required = false) BigDecimal priceNet,
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "expectedDelivery", required = false) Date expectedDelivery,
			@RequestParam(value = "orderConfirmationNumber", required = true) 
				String orderConfirmationNumber) 
					throws Exception {
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request:  orderConfirmationNumber:"+orderConfirmationNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		OrderItem orderItemToConfirm = itemRepo.findOne(orderItemId);
		Address address = orderItemToConfirm.getOrder().getCustomer().getAddress();
		if (orderItemToConfirm.getReport(orderConfirmationNumber) == null){
			// document does not exist - create new one
			 ConfirmationReport oc = new ConfirmationReport(orderConfirmationNumber, address, 
					 address, new ConfirmedSpecification(false, false));
			 heService.confirm(orderItemToConfirm, quantity, 
					 new Amount(priceNet, Currency.EUR), oc, null);
			
		}
		else
		heService.confirm(orderItemToConfirm, quantity, orderConfirmationNumber, new Amount(priceNet, Currency.EUR));

		response.setData(orderItemToConfirm);
		response.setTotal(1);
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/order", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse order(@RequestParam(required = false) Map map) 
					throws Exception {
		
		map.toString();
		JsonObjectResponse response = new JsonObjectResponse();
		response.setTotal(1);
		response.setMessage("order item(s) confirmed");
		response.setSuccess(false);

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
		
		OrderItem shippingItemToDeconfirm =
				itemRepo.findOne(shippingItemId);
		
		OrderItem shippingItem = heService.deconfirm(
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
			@RequestParam(value = "quantity", required = true) int quantity,
			@RequestParam(value = "invoiceNumber", required = true) String invoiceNumber,
			@RequestParam(value = "trackNumber", required = false) String trackNumber,
			@RequestParam(value = "packageNumber", required = false) String packageNumber) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json confirm request: orderConfirmationNumber:" 
				+ " quantity:" + quantity + " orderConfirmationNumber:"+invoiceNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		OrderItem shippingItemToDeliver = itemRepo.findOne(shippingItemId);
		if (shippingItemToDeliver == null)
			handleException(new NotFoundException("ShippingItem with given id not found!"));
		
		if (shippingItemToDeliver.getReport(invoiceNumber) == null)
			// Document with given invoiceNumber does not exist
			shippingItemToDeliver = heService.shipAndInvoice(shippingItemToDeliver, quantity, invoiceNumber, 
					null, null);
		else // Document with given invoiceNumber exists
			shippingItemToDeliver = heService.shipAndInvoice(shippingItemToDeliver, quantity, invoiceNumber);
		
		response.setData(shippingItemToDeliver);
		response.setTotal(1);
		response.setMessage("shipped and invoiced");
		response.setSuccess(true);

		return response;
	}
	
	private void handleException(Throwable e){
		JsonObjectResponse response = new JsonObjectResponse();
		response.setData(e.getMessage());
		response.setTotal(1);
		response.setMessage(e.toString());
		response.setSuccess(true);
		log.error(e.toString());
	}
	
	@RequestMapping(value="/withdraw/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse withdraw(
			@RequestParam(value = "id", required = true) long id) 
					throws Exception {
		
		log.debug("received json withdraw request:" 
				+ " invoiceItemId:"+id);
		JsonObjectResponse response = new JsonObjectResponse();
		
		OrderItem item = itemRepo.findOne(id);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}

		OrderItem he= heService.withdrawInvoiceItemAndShipment(item);
		
		response.setData(he);
		response.setTotal(1);
		response.setMessage("invoice item withdrawed");
		response.setSuccess(true);

		return response;
	}

	
	@RequestMapping(value="/complete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse complete(
			@RequestParam(value = "documentNumber", required = true) String documentNumber,
			@RequestParam(value = "receivedPaymentDate", required = false) Date receivedPaymentDate) 
					throws Exception {
		
		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json complete request: "
				+ " documentNumber:" + documentNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Report item = reportRepo.findByDocumentNumber(documentNumber);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		Set<HandlingEvent> hes = heService.receivePayment(documentNumber, receivedPaymentDate);
		
		response.setData(hes);
		response.setTotal(1);
		response.setMessage("document paid");
		response.setSuccess(true);

		return response;
	}

	@RequestMapping(value="/decomplete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse decomplete(
			@RequestParam(value = "documentNumber", required = true) String documentNumber) 
					throws Exception {
		
		log.debug("received json decomplete request:" 
				+ " documentNumber:"+documentNumber);
		JsonObjectResponse response = new JsonObjectResponse();
		
		Report item = reportRepo.findByDocumentNumber(documentNumber);
		if (item == null){
			throw new NotFoundException("Item with given id not found");
		}
		HandlingEvent he = heService.withdrawPayment(item);
		
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
		//TODO: implement me!
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
