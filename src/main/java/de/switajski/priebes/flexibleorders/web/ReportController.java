package de.switajski.priebes.flexibleorders.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.report.itextpdf.DeliveryNotesPdfView;
import de.switajski.priebes.flexibleorders.report.itextpdf.InvoicePdfView;
import de.switajski.priebes.flexibleorders.report.itextpdf.ConfirmationReportPdfView;
import de.switajski.priebes.flexibleorders.report.itextpdf.OrderPdfView;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.DeliveryNotesServiceImpl;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;

/**
 * Controller for handling http requests on reports. E.g. PDFs 
 * @author Marek Switajski
 *
 */
@Controller
@RequestMapping("/reports")
public class ReportController extends ExceptionController{
	
	private static Logger log = Logger.getLogger(ReportController.class);
	@Autowired OrderItemRepository invoiceItemService;
	@Autowired ReportRepository reportRepo;
	@Autowired OrderRepository orderRepo;
	@Autowired ReportItemServiceImpl itemService;
	@Autowired DeliveryNotesServiceImpl invoiceService;
	@Autowired CustomerRepository customerService;
	
	/*	http://static.springsource.org/spring/docs/3.0.x/reference/mvc.html says, that
     *  @ResponseBody is for direct responses without a view
     */
	@RequestMapping(value = "/orderConfirmations/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showConfirmationReportPdf(@PathVariable("id") String id) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        ConfirmationReport record = (ConfirmationReport) reportRepo.findByDocumentNumber(id); 
            return new ModelAndView(ConfirmationReportPdfView.class.getSimpleName(),
            		ConfirmationReport.class.getSimpleName(),
            		record);
			
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		
        return new ModelAndView(ConfirmationReportPdfView.class.getName(),ConfirmationReport.class.getSimpleName(),null);
    }
	
	@RequestMapping(value = "/deliveryNotes/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showDeliveryNotesPdf(@PathVariable("id") String id) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        DeliveryNotes record = (DeliveryNotes) reportRepo.findByDocumentNumber(id);
            return new ModelAndView(DeliveryNotesPdfView.class.getSimpleName(),
            		DeliveryNotes.class.getSimpleName(),
            		record);
			
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		
        return new ModelAndView(DeliveryNotesPdfView.class.getSimpleName(),DeliveryNotes.class.getSimpleName(),null);
    }
	
	@RequestMapping(value = "/invoices/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showInvoicePdf(@PathVariable("id") String id) {
        //TODO: duplicate code
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        Invoice record = (Invoice) reportRepo.findByDocumentNumber(id);
            return new ModelAndView(InvoicePdfView.class.getSimpleName(),
            		Invoice.class.getSimpleName(),
            		record);
			
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		
        return new ModelAndView(InvoicePdfView.class.getSimpleName(),Invoice.class.getSimpleName(),null);
    }
	
	
	@RequestMapping(value = "/orders/{orderNumber}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showOrderPdf(@PathVariable("orderNumber") String orderNumber) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        FlexibleOrder record = itemService.retrieveOrder(orderNumber);
            return new ModelAndView(OrderPdfView.class.getSimpleName(),
            		FlexibleOrder.class.getSimpleName(),record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return new ModelAndView(OrderPdfView.class.getSimpleName(),FlexibleOrder.class.getSimpleName(),null);
    }
	
	
	@RequestMapping(value="/deliveryNotes/listDeliveryNotesNumbers", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listInvoiceNumbers(
			@RequestParam(value = "query", required = false) Long orderNumberObject) 
					throws Exception {

		log.debug("listOrderNumbers request:"+orderNumberObject);
		JsonObjectResponse response = new JsonObjectResponse();	

		if (orderNumberObject != null){
			List<Long> orderNumbers = invoiceService.findInvoiceNumbersLike(orderNumberObject);
			if (!orderNumbers.isEmpty()){
				response.setTotal(orderNumbers.size());
				response.setData(formatInvoiceNumbers(orderNumbers));			
			}
			else {
				response.setTotal(0l);
			}
		} 
		else { //orderNumber == null
			List<Customer> customers = customerService.findAll();
			ArrayList<Long> list = new ArrayList<Long>();
			for (Customer customer:customers)
				list.addAll(invoiceService.getInvoiceNumbersByCustomer(customer, new PageRequest(1,20)));
			response.setTotal(list.size());
			response.setData(formatInvoiceNumbers(list));
		}
			
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}
	
	private invoiceNumber[] formatInvoiceNumbers(List<Long> invoiceNumberList){
		List<invoiceNumber> entities = new ArrayList<invoiceNumber>();
		for (Long on:invoiceNumberList)
			entities.add(new invoiceNumber(on));
		invoiceNumber[] invoiceNumberArray = entities.toArray(new invoiceNumber[0]);
		return invoiceNumberArray;
	}
	
	public class invoiceNumber {

		  public long invoiceNumber;

		  public invoiceNumber(long invoiceNumber) {
		    this.invoiceNumber = invoiceNumber;
		  }

		}
	
	@RequestMapping(value = "/json", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts){
		JsonObjectResponse response = new JsonObjectResponse();
		Page<FlexibleOrder> customer = orderRepo.findAll(new PageRequest(page-1, limit));
		response.setData(customer.getContent());
		response.setTotal(customer.getTotalElements());
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value="/orders/listOrderNumbers", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listOrderNumbers(
			@RequestParam(value = "orderNumber", required = false) String orderNumber,
			@RequestParam(value = "customerId", required = false) Long customerId) 
					throws Exception {
		//FIXME: find by customer and orderNumber
		log.debug("listOrderNumbers request:"+orderNumber);
		JsonObjectResponse response = new JsonObjectResponse();	

		if (orderNumber != null){
			List<String> orderNumbers = itemService.retrieveOrderNumbersLike(orderNumber);
			if (!orderNumbers.isEmpty()){
				response.setTotal(orderNumbers.size());
				response.setData(formatOrderNumbers(orderNumbers));			
			}
			else {
				response.setTotal(0l);
			}
		} 
		else { //orderNumber == null
			List<Customer> customers = customerService.findAll();
			ArrayList<String> list = new ArrayList<String>();
			for (Customer customer:customers)
				list.addAll(itemService.retrieveOrderNumbersByCustomer(customer, new PageRequest(0,20)).getContent());
			response.setTotal(list.size());
			response.setData(formatOrderNumbers(list));
		}
			
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}
	
	private orderNumber[] formatOrderNumbers(List<String> orderNumbers){
		List<orderNumber> entities = new ArrayList<orderNumber>();
		for (String on:orderNumbers)
			entities.add(new orderNumber(on));
		orderNumber[] orderNumberArray = entities.toArray(new orderNumber[0]);
		return orderNumberArray;
	}

	public class orderNumber {

		  public String orderNumber;

		  public orderNumber(String orderNumber) {
		    this.orderNumber = orderNumber;
		  }

		}
}
