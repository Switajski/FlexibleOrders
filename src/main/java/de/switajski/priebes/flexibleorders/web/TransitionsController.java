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
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.CancelReport;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.service.process.DeliveryService;
import de.switajski.priebes.flexibleorders.service.process.InvoicingService;
import de.switajski.priebes.flexibleorders.service.process.OrderService;
import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
// TODO: distribute methods on business controllers
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private InvoicingService invoicingService;
	@Autowired
	private DeliveryService deliveryService;

	@RequestMapping(value = "/confirm/json", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse confirm(
			@RequestBody JsonCreateReportRequest confirmRequest)
			throws Exception {

		OrderConfirmation confirmationReport = orderService.confirm(
				extractOrderNumber(confirmRequest),
				confirmRequest.getOrderConfirmationNumber(),
				confirmRequest.getExpectedDelivery(),
				confirmRequest.getCarrierNumber(), 
				confirmRequest.createDeliveryAddress(),
				confirmRequest.createInvoiceAddress(),
				confirmRequest.getItems());
		return ExtJsResponseCreator.createResponse(confirmationReport);
	}

	private String extractOrderNumber(JsonCreateReportRequest confirmRequest) {
		if (confirmRequest.getItems().isEmpty())
			throw new IllegalArgumentException(
					"Liste der Auftragspositionen ist leer");
		return confirmRequest.getItems().iterator().next().getOrderNumber();
	}

	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse order(@RequestBody JsonCreateReportRequest deliverRequest)
			throws Exception {
		deliverRequest.validate();

		Order order = orderService.order(
				deliverRequest.getCustomerId(),
				deliverRequest.getOrderNumber(),
				deliverRequest.getCreated(),
				deliverRequest.getItems());

		return ExtJsResponseCreator.createResponse(order);
	}

	@RequestMapping(value = "/invoice/json", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse invoice(
			@RequestBody JsonCreateReportRequest deliverRequest)
			throws Exception {
		deliverRequest.validate();

		Invoice invoice = invoicingService.invoice(
				deliverRequest.getInvoiceNumber(),
				deliverRequest.getPaymentConditions(),
				deliverRequest.getCreated(),
				deliverRequest.getItems(),
				deliverRequest.getBilling());
		return ExtJsResponseCreator.createResponse(invoice);
	}

	@RequestMapping(value = "/deliver/json", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse deliver(
			@RequestBody JsonCreateReportRequest deliverRequest)
			throws Exception {
		deliverRequest.validate();

		DeliveryNotes dn = deliveryService.deliver(
				deliverRequest.getDeliveryNotesNumber(),
				deliverRequest.getTrackNumber(),
				deliverRequest.getPackageNumber(),
				new Amount(deliverRequest.getShipment(), Currency.EUR),
				deliverRequest.getCreated(),
				deliverRequest.getItems());
		return ExtJsResponseCreator.createResponse(dn);
	}

	@RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse deleteOrder(
			@RequestParam(value = "orderNumber", required = true) String orderNumber) {
		orderService.deleteOrder(orderNumber);
		return ExtJsResponseCreator.createResponse(orderNumber);
	}

	@RequestMapping(value = "/deleteReport", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse deleteReport(
			@RequestParam(value = "documentNumber", required = true) String documentNumber)
			throws Exception {
		orderService.deleteReport(documentNumber);
		return ExtJsResponseCreator.createResponse(documentNumber);
	}

	@RequestMapping(value = "/cancelReport", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse cancelConfirmationReport(
			@RequestParam(value = "confirmationNumber", required = true) String orderConfirmationNumber)
			throws Exception {
		CancelReport cr = orderService
				.cancelReport(orderConfirmationNumber);
		return ExtJsResponseCreator.createResponse(cr);
	}

	@RequestMapping(value = "/complete/json", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse complete(
			@RequestParam(value = "invoiceNumber", required = true) String invoiceNumber)
			throws Exception {
		Receipt receipt = orderService.markAsPayed(invoiceNumber, "B"
				+ invoiceNumber, new Date());
		return ExtJsResponseCreator.createResponse(receipt);
	}

	@RequestMapping(value = "/decomplete/json", method = RequestMethod.POST)
	public @ResponseBody
	JsonObjectResponse decomplete(
			@RequestParam(value = "documentNumber", required = true) String documentNumber)
			throws Exception {
		// TODO Implement
		throw new NotImplementedException();
	}

	@RequestMapping(value = "/delete/json", method = RequestMethod.DELETE)
	public @ResponseBody
	JsonObjectResponse delete(
			@RequestParam(value = "id", required = true) long id)
			throws NotFoundException {
		// TODO Implement
		throw new NotImplementedException();
	}

}
