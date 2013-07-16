package de.switajski.priebes.flexibleorders.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;
import flexjson.JSONSerializer;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired OrderItemService orderItemService;
	@Autowired OrderService orderService;
	
	/**
	 * params for Extjs PageRequests http://oajamfibia.wordpress.com/2011/06/25/spring-roo-controller-for-extjs4-data-store-with-pagination-sorting-filtering-and-server-side-validation/ implementiert
	 * @param page
	 * @param start
	 * @param limit
	 * @param sorts
	 * @param filters
	 * @return
	 */
	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "filter", required = false) String filters) {
		HttpStatus returnStatus = HttpStatus.OK;
		JsonObjectResponse response = new JsonObjectResponse();
		    
            try {
    	        HttpHeaders headers = new HttpHeaders();
    	        headers.add("Content-Type", "application/json; charset=utf-8");
    	        Pageable pageable = new PageRequest(page-1, limit);
    	        Page<OrderItem> orders = orderItemService.findByOrderItemNumber(1l, pageable);
                returnStatus = HttpStatus.OK;
    			response.setMessage("All orders retrived.");
    			response.setSuccess(true);
    			response.setTotal(orderService.countAll());
    			response.setData(orders);
    		} catch(Exception e) {
    			e.printStackTrace();
    			
    			StringWriter errors = new StringWriter();
    			e.printStackTrace(new PrintWriter(errors));
    			response.setMessage(errors.toString());
    			response.setSuccess(false);
    			response.setTotal(0L);
    		}
        
		JSONSerializer jsonser = new JSONSerializer();
		String returnString = jsonser.serialize(response);
		// Return list of retrieved performance areas
        return new ResponseEntity<String>(returnString, returnStatus);
    }
	
	
}
