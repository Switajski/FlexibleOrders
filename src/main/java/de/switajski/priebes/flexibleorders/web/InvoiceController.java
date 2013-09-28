package de.switajski.priebes.flexibleorders.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.report.Invoice;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.InvoiceService;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {
	
	private static Logger log = Logger.getLogger(InvoiceController.class);
	@Autowired InvoiceItemService invoiceItemService;
	@Autowired InvoiceService invoiceService;
	@Autowired CustomerService customerService;

	@RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    /*	http://static.springsource.org/spring/docs/3.0.x/reference/mvc.html says, that
     *  @ResponseBody is for direct responses without a view
     */
    public ModelAndView showPdf(@PathVariable("id") Long id) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        Invoice record = new Invoice(invoiceItemService.findByInvoiceNumber(id));
            return new ModelAndView("InvoicePdfView","Invoice",record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return new ModelAndView("InvoicePdfView","Invoice",null);
    }
	
	@RequestMapping(value="/listInvoiceNumbers", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listOrderNumbers(
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
				list.addAll(invoiceService.getInvoiceNumbersByCustomer(customer, new PageRequest(1,20)).getContent());
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
	
	
}
