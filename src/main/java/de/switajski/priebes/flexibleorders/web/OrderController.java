package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController extends JsonController<Order> {
	
	@Autowired OrderItemService orderItemService;

	@Autowired
	public OrderController(OrderService crudServiceAdapter) {
		super(crudServiceAdapter);
	}

	@Override
	protected void resolveDependencies(Order entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Page<Order> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    /*	http://static.springsource.org/spring/docs/3.0.x/reference/mvc.html says, that
     *  @ResponseBody is for direct responses without a view
     */
    public ModelAndView showPdf(@PathVariable("id") Long id) {
        
    	try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/pdf; charset=utf-8");
	        Order record = new Order(orderItemService.findByOrderNumber(id));
            return new ModelAndView("OrderPdfView","Order",record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return new ModelAndView("OrderPdfView","Order",null);
    }
	
	
}
