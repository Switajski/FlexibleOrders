package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.switajski.priebes.flexibleorders.report.Invoice;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {
	
	@Autowired InvoiceItemService invoiceItemService;

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
	
	
}
