package de.switajski.priebes.flexibleorders.web;

import java.util.Date;

import javassist.NotFoundException;

import org.apache.log4j.Logger;
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
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.JsonDeliverRequest;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController{
	
	private static Logger log = Logger.getLogger(TransitionsController.class);
	private OrderServiceImpl orderService;

	@Autowired
	public TransitionsController(
			OrderServiceImpl orderService) {
		this.orderService = orderService;
	}
	

	@RequestMapping(value="/confirm/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse confirm(
			@RequestParam(value = "orderNumber", required = true) String orderNumber) 
					throws Exception {
		ConfirmationReport confirmationReport = orderService.confirm(orderNumber);
		return ExtJsResponseCreator.createResponse(confirmationReport);
	}
	
	@RequestMapping(value="/order", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse order(@RequestBody JsonDeliverRequest deliverRequest) 
					throws Exception {
		log.debug("received json order request: orderNumber:"+deliverRequest.getOrderNumber());
		
		FlexibleOrder order = orderService.order(deliverRequest.getCustomerId(), 
				deliverRequest.getOrderNumber(), deliverRequest.getItems());
		
		return ExtJsResponseCreator.createResponse(order);
	}

	@RequestMapping(value="/deliver/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deliver(@RequestBody JsonDeliverRequest deliverRequest) 
			throws Exception {
		log.debug("received json deliver request: invoiceNumber:"+deliverRequest.getInvoiceNumber());
		
		DeliveryNotes dn = orderService.deliver(deliverRequest.getInvoiceNumber(), 
				deliverRequest.getTrackNumber(), deliverRequest.getPackageNumber(),
				deliverRequest.createAddress(), 
				new Amount(deliverRequest.getShipment(), Currency.EUR),
				deliverRequest.getItems());
		return ExtJsResponseCreator.createResponse(dn);
	}
	
	@RequestMapping(value="/deleteOrder", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deleteOrder(
			@RequestBody ReportItem reportItem){
		orderService.deleteOrder(reportItem.getOrderNumber());
		return ExtJsResponseCreator.createResponse(reportItem.getOrderNumber());
	}
	
	@RequestMapping(value="/cancelDeliveryNotes", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse cancelDeliveryNotes(
			@RequestParam(value = "invoiceNumber", required = true) String invoiceNumber) 
			throws Exception {
		CancelReport cr = orderService.cancelDeliveryNotes(invoiceNumber);
//		orderService.deleteReport(invoiceNumber);
		return ExtJsResponseCreator.createResponse(cr);
	}
	
	@RequestMapping(value="/cancelConfirmationReport", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse cancelConfirmationReport(
			@RequestParam(value = "confirmationNumber", required = true) String orderConfirmationNumber) 
			throws Exception {
		//TODO: implement cancellation instead of delete
		CancelReport cr = orderService.cancelConfirmationReport(orderConfirmationNumber);
//		orderService.deleteReport(orderConfirmationNumber);
		return ExtJsResponseCreator.createResponse(cr);
	}
	
//	private ReportItem addAndSaveOrderItem(ReportItem entity) {
//		Product product = cProductRepo.findByProductNumber(entity.getProduct()).toProduct();
//		if (orderService.existsOrderNumber(entity.getOrderNumber()))
//			return orderService.addToOrder(entity.getOrderNumber(), product, entity.getQuantity(), 
//					new Amount(entity.getPriceNet(), Currency.EUR))
//					.toReportItem();
//		else {
//			Customer customer = customerRepo.findOne(entity.getCustomer());
//			return orderService.order(customer, entity.getOrderNumber(), product, 
//					entity.getQuantity(), new Amount(entity.getPriceNet(), Currency.EUR))
//					.toReportItem();
//		}
//	}

	
//	@RequestMapping(value="/deconfirm/json", method=RequestMethod.POST)
//	public @ResponseBody JsonObjectResponse deconfirm(
//			@RequestParam(value = "id", required = true) long shippingItemId,
////			@RequestParam(value = "orderNumber", required = true) long orderNumber,
////			@RequestParam(value = "productNumber", required = true) long productNumber, 
//			@RequestParam(value = "orderConfirmationNumber", required = true) long orderConfirmationNumber) 
//					throws Exception {
//		
//		log.debug("received json deconfirm request: " 
//				+ " orderConfirmationNumber:" + orderConfirmationNumber);
//		JsonObjectResponse response = new JsonObjectResponse();
//		
//		if (!itemRepo.exists(shippingItemId))
//			throw new NotFoundException("Shipping Item with given Id not found!");
//		
//		OrderItem shippingItemToDeconfirm =
//				itemRepo.findOne(shippingItemId);
//		
//		OrderItem shippingItem = orderService.deconfirm(
//				shippingItemToDeconfirm);
//		
//		response.setData(shippingItem);
//		response.setTotal(1);
//		response.setMessage("order item deconfirmed");
//		response.setSuccess(true);
//
//		return response;
//	}

	
//	@RequestMapping(value="/withdraw/json", method=RequestMethod.POST)
//	public @ResponseBody JsonObjectResponse withdraw(
//			@RequestParam(value = "id", required = true) long id) 
//					throws Exception {
//		
//		log.debug("received json withdraw request:" 
//				+ " invoiceItemId:"+id);
//		JsonObjectResponse response = new JsonObjectResponse();
//		
//		OrderItem item = itemRepo.findOne(id);
//		if (item == null){
//			throw new NotFoundException("Item with given id not found");
//		}
//
//		OrderItem he= orderService.withdrawInvoiceItemAndShipment(item);
//		
//		response.setData(he);
//		response.setTotal(1);
//		response.setMessage("invoice item withdrawed");
//		response.setSuccess(true);
//
//		return response;
//	}

	
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
