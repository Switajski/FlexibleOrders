package de.switajski.priebes.flexibleorders.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.service.ItemServiceImpl;

@Controller
@RequestMapping("/orderconfirmations")
public class OrderConfirmationController {
	
	@Autowired ItemServiceImpl itemService;

	@RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    /*	http://static.springsource.org/spring/docs/3.0.x/reference/mvc.html says, that
     *  @ResponseBody is for direct responses without a view
     */
    public ModelAndView showPdf(@PathVariable("id") Long id) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        OrderConfirmation record = new OrderConfirmation(itemService.findByOrderConfirmationNumber(id));
            return new ModelAndView("OrderConfirmationPdfView","OrderConfirmation",record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return new ModelAndView("OrderConfirmationPdfView","OrderConfirmation",null);
    }
	
	
}
