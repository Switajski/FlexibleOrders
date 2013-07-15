package de.switajski.priebes.flexibleorders.web;

import java.util.List;

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
import de.switajski.priebes.flexibleorders.domain.OrderItemService;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.report.Order;
import flexjson.JSONSerializer;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired OrderItemService orderItemService;
	
	/**
	 * Nach http://oajamfibia.wordpress.com/2011/06/25/spring-roo-controller-for-extjs4-data-store-with-pagination-sorting-filtering-and-server-side-validation/ implementiert
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
    	        Pageable pageable = new PageRequest(2, 20);
    	        Page<OrderItem> orders = orderItemService.findByOrderNumber(1336413278l, pageable);
                returnStatus = HttpStatus.OK;
    			response.setMessage("Alle Bestellungen erhalten.");
    			response.setSuccess(true);
//    			response.setTotal(orderItemService.countOrders());
    			response.setData(orders);
    		} catch(Exception e) {
    			response.setMessage(e.getMessage());
    			response.setSuccess(false);
    			response.setTotal(0L);
    		}
        
		JSONSerializer jsonser = new JSONSerializer();
		String returnString = jsonser.serialize(response);
		// Return list of retrieved performance areas
        return new ResponseEntity<String>(returnString, returnStatus);
    }
	
}
