package de.switajski.priebes.flexibleorders.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.HandlingEventRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.JsonDeliverRequest;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@Controller
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController{
	
	private static Logger log = Logger.getLogger(TransitionsController.class);
	private OrderItemRepository itemRepo;
	private ReportRepository reportRepo;
	private OrderServiceImpl orderService;
	private CatalogProductRepository cProductRepo;
	private CustomerRepository customerRepo;
	private HandlingEventRepository heRepo;

	@Autowired
	public TransitionsController(
			CustomerRepository customerRepo,
			OrderServiceImpl orderService,
			OrderItemRepository itemRepo,
			ReportRepository reportRepo,
			CatalogProductRepository catalogProductRepo) {
		this.orderService = orderService;
		this.itemRepo = itemRepo;
		this.reportRepo = reportRepo;
		this.cProductRepo = catalogProductRepo;
		this.customerRepo = customerRepo;
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
			 orderService.confirm(orderItemToConfirm, quantity, 
					 new Amount(priceNet, Currency.EUR), oc, null);
			
		}
		else
		orderService.confirm(orderItemToConfirm, quantity, orderConfirmationNumber, new Amount(priceNet, Currency.EUR));

		response.setData(orderItemToConfirm);
		response.setTotal(1);
		response.setMessage("order item(s) confirmed");
		response.setSuccess(true);

		return response;
	}
	
	//TODO: try RequestParam with ReportItem[].class
	@RequestMapping(value="/order", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse order(@RequestBody String json) 
					throws Exception {
		// TODO: replace if condition with Ext.data.writer.Json.allowSingle = false
		if (json.charAt(0) == '['){
			List<ReportItem> entities = JsonSerializationHelper.deserializeReportItems(json);
			entities = addAndSaveOrderItems(entities);
			return ExtJsResponseCreator.createResponse(entities);
			
		} else {
			ReportItem entity = JsonSerializationHelper.deserializeReportItem(json);
			entity = addAndSaveOrderItem(entity);
			return ExtJsResponseCreator.createResponse(entity);
		}
		
	}

	private List<ReportItem> addAndSaveOrderItems(List<ReportItem> entities) {
		List<ReportItem> reportItems = new ArrayList<ReportItem>();
		for (ReportItem reportItem:entities){
			reportItems.add(addAndSaveOrderItem(reportItem));
		}
		return reportItems;
	}

	private ReportItem addAndSaveOrderItem(ReportItem entity) {
		Product product = cProductRepo.findByProductNumber(entity.getProduct()).toProduct();
		if (orderService.existsOrderNumber(entity.getOrderNumber()))
			return orderService.order(entity.getOrderNumber(), product, entity.getQuantity(), 
					new Amount(entity.getPriceNet(), Currency.EUR))
					.toReportItem();
		else {
			Customer customer = customerRepo.findOne(entity.getCustomer());
			return orderService.order(customer, entity.getOrderNumber(), product, 
					entity.getQuantity(), new Amount(entity.getPriceNet(), Currency.EUR))
					.toReportItem();
		}
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
		
		OrderItem shippingItem = orderService.deconfirm(
				shippingItemToDeconfirm);
		
		response.setData(shippingItem);
		response.setTotal(1);
		response.setMessage("order item deconfirmed");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/deliver/json", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse deliver(@RequestBody JsonDeliverRequest deliverRequest) 
					throws Exception {
		
		log.debug("received json confirm request: invoiceNumber:"+deliverRequest.getInvoiceNumber());

		List<ReportItem> deliveredItems = orderService.deliverJsonCommitted(deliverRequest);
		return ExtJsResponseCreator.createResponse(deliveredItems);
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

		OrderItem he= orderService.withdrawInvoiceItemAndShipment(item);
		
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
		Set<HandlingEvent> hes = orderService.receivePayment(documentNumber, receivedPaymentDate);
		
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
		HandlingEvent he = orderService.withdrawPayment(item);
		
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
