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
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.ItemServiceImpl;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	ItemServiceImpl itemService;
	CustomerService customerService;
	
	private static Logger log = Logger.getLogger(OrderController.class);

	@Autowired
	public OrderController(ItemServiceImpl itemService,
			CustomerService customerService) {
		this.itemService = itemService;
		this.customerService = customerService;
	}
	
	@RequestMapping(value="/listOrderNumbers", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listOrderNumbers(
			@RequestParam(value = "query", required = false) Long orderNumberObject) 
					throws Exception {

		log.debug("listOrderNumbers request:"+orderNumberObject);
		JsonObjectResponse response = new JsonObjectResponse();	

		if (orderNumberObject != null){
			List<Long> orderNumbers = itemService.findOrderNumbersLike(orderNumberObject);
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
			ArrayList<Long> list = new ArrayList<Long>();
			for (Customer customer:customers)
				list.addAll(itemService.getOrderNumbersByCustomer(customer, new PageRequest(1,20)).getContent());
			response.setTotal(list.size());
			response.setData(formatOrderNumbers(list));
		}
			
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}
	
	private orderNumber[] formatOrderNumbers(List<Long> orderNumberList){
		List<orderNumber> entities = new ArrayList<orderNumber>();
		for (Long on:orderNumberList)
			entities.add(new orderNumber(on));
		orderNumber[] orderNumberArray = entities.toArray(new orderNumber[0]);
		return orderNumberArray;
	}

	@RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    /*	http://static.springsource.org/spring/docs/3.0.x/reference/mvc.html says, that
     *  @ResponseBody is for direct responses without a view
     */
    public ModelAndView showPdf(@PathVariable("id") Long id) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        Order record = new Order(itemService.findByOrderNumber(id));
            return new ModelAndView("OrderPdfView","Order",record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return new ModelAndView("OrderPdfView","Order",null);
    }
	
	public class orderNumber {

		  public long orderNumber;

		  public orderNumber(long orderNumber) {
		    this.orderNumber = orderNumber;
		  }

		}
	
}
