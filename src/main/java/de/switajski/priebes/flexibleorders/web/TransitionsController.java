package de.switajski.priebes.flexibleorders.web;

import java.util.Date;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CancelReport;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.JsonDeliverRequest;
import de.switajski.priebes.flexibleorders.web.entities.ItemDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController{
	
	private OrderServiceImpl orderService;

	@Autowired
	public TransitionsController(
			OrderServiceImpl orderService) {
		this.orderService = orderService;
	}
	

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(@RequestBody JsonDeliverRequest confirmRequest) 
					throws Exception {
		
		ConfirmationReport confirmationReport = orderService.confirm(extractOrderNumber(confirmRequest),
				confirmRequest.getOrderConfirmationNumber(), confirmRequest.getExpectedDelivery(), 
				confirmRequest.getItems());
		return ExtJsResponseCreator.createResponse(confirmationReport);
	}


	private String extractOrderNumber(JsonDeliverRequest confirmRequest) {
		if (confirmRequest.getItems().isEmpty())
			throw new IllegalArgumentException("Liste der Auftragspositionen ist leer");
		return confirmRequest.getItems().iterator().next().getOrderNumber();
	}
	
	@RequestMapping(value="/order", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse order(@RequestBody JsonDeliverRequest deliverRequest) 
					throws Exception {
		deliverRequest.validate();
		
		Order order = orderService.order(deliverRequest.getCustomerId(), 
				deliverRequest.getOrderNumber(), deliverRequest.getItems());
		
		return ExtJsResponseCreator.createResponse(order);
	}
	
	@RequestMapping(value="/invoice/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse invoice(
			@RequestBody JsonDeliverRequest deliverRequest) 
					throws Exception {
		deliverRequest.validate();
		
		Invoice invoice = orderService.invoice(deliverRequest.getInvoiceNumber(), 
				deliverRequest.getPaymentConditions(), deliverRequest.createAddress(),
				deliverRequest.getItems());
		return ExtJsResponseCreator.createResponse(invoice);
	}


	@RequestMapping(value="/deliver/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deliver(@RequestBody JsonDeliverRequest deliverRequest) 
			throws Exception {
		deliverRequest.validate();
		
		DeliveryNotes dn = orderService.deliver(deliverRequest.getDeliveryNotesNumber(), 
				deliverRequest.getTrackNumber(), deliverRequest.getPackageNumber(),
				deliverRequest.createAddress(), 
				new Amount(deliverRequest.getShipment(), Currency.EUR),
				deliverRequest.getItems());
		return ExtJsResponseCreator.createResponse(dn);
	}
	
	@RequestMapping(value="/deleteOrder", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deleteOrder(
			@RequestBody ItemDto reportItem){
		orderService.deleteOrder(reportItem.getOrderNumber());
		return ExtJsResponseCreator.createResponse(reportItem.getOrderNumber());
	}
	
	@RequestMapping(value="/cancelDeliveryNotes", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse cancelDeliveryNotes(
			@RequestParam(value = "deliveryNotesNumber", required = true) String deliveryNotesNumber) 
			throws Exception {
		CancelReport cr = orderService.cancelDeliveryNotes(deliveryNotesNumber);
		return ExtJsResponseCreator.createResponse(cr);
	}
	
	@RequestMapping(value="/deleteReport", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deleteReport(
			@RequestParam(value = "documentNumber", required = true) String documentNumber) 
			throws Exception {
		orderService.deleteReport(documentNumber);
		return ExtJsResponseCreator.createResponse(documentNumber);
	}
	
	@RequestMapping(value="/cancelConfirmationReport", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse cancelConfirmationReport(
			@RequestParam(value = "confirmationNumber", required = true) String orderConfirmationNumber) 
			throws Exception {
		CancelReport cr = orderService.cancelConfirmationReport(orderConfirmationNumber);
		return ExtJsResponseCreator.createResponse(cr);
	}
	
	
	@RequestMapping(value="/complete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse complete(
			@RequestParam(value="invoiceNumber", required = true) String invoiceNumber) 
					throws Exception {
		Receipt receipt = orderService.markAsPayed(invoiceNumber, "B" + invoiceNumber, new Date());
		return ExtJsResponseCreator.createResponse(receipt);
	}

	@RequestMapping(value="/decomplete/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse decomplete(
			@RequestParam(value = "documentNumber", required = true) String documentNumber) 
					throws Exception {
		//TODO Implement
		throw new NotImplementedException();
	}
	
	@RequestMapping(value="/delete/json", method=RequestMethod.DELETE)
	public @ResponseBody JsonObjectResponse delete(
			@RequestParam(value = "id", required = true) long id) 
					throws NotFoundException {
		//TODO Implement
		throw new NotImplementedException();
	}
	
}
