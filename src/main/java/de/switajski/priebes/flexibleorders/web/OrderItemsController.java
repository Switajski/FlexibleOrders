package de.switajski.priebes.flexibleorders.web;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.service.CustomerServiceImpl;
import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@RequestMapping("/orderitems")
@Controller
public class OrderItemsController extends ExceptionController{

	private static Logger log = Logger.getLogger(OrderItemsController.class);
	
	private static final String FILTER_STATUS = "ordered";
	private OrderItemRepository itemService;
	private OrderRepository orderRepo;
	private ReportItemServiceImpl reportService;

	@Autowired
	public OrderItemsController(OrderItemRepository itemRepo,
			CustomerServiceImpl customerService,
			OrderServiceImpl heService,
			CatalogProductRepository productRepository,
			OrderRepository orderRepo,
			ReportItemServiceImpl reportService) {
		this.itemService = itemRepo;
		this.orderRepo = orderRepo;
		this.reportService = reportService;
	}

	@RequestMapping(value="/json", method=RequestMethod.DELETE)
	public @ResponseBody JsonObjectResponse delete( @RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		JsonObjectResponse response = new JsonObjectResponse();

		if (json.charAt(0) == '['){
			List<OrderItem> entities = parseJsonArray(json);
			for (OrderItem entity:entities){
				itemService.delete(entity);
			}
			response.setTotal(entities.size());
		} else {
			OrderItem entity = parseJsonObject(json);
			itemService.delete(entity);					
			response.setData(entity);
			response.setTotal(1l);
		}

		response.setMessage("Entity deleted.");
		response.setSuccess(true);

		return response;
	}

	@RequestMapping(value="/json", /*headers = "Accept:application/json",*/ method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse create(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		JsonObjectResponse response = new JsonObjectResponse();

		if (json.charAt(0) == '['){
			List<OrderItem> entities = parseJsonArray(json);
			for (OrderItem entity:entities){
				itemService.save(entity);
			}
			response.setTotal(entities.size());
		} else {
			OrderItem entity = parseJsonObject(json);
			itemService.save(entity);					
			response.setData(entity);
			response.setTotal(1l);
		}

		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		response.setTotal(itemService.count());

		return response;
	}

	private OrderItem parseJsonObject(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		return (OrderItem) mapper.readValue(json, OrderItem.class); 
	}

	//TODO: move to SerializationHelper
	public List<OrderItem> parseJsonArray(String json) throws JsonParseException, JsonMappingException, IOException {
		OrderItem[] typedArray = (OrderItem[]) Array.newInstance(OrderItem.class,1);
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		OrderItem[] records = (OrderItem[]) mapper.readValue(json, typedArray.getClass());

		ArrayList<OrderItem> list = new ArrayList<OrderItem>();
		for (OrderItem record:records)
			list.add(record);

		return list;
	}
	
	
	
	@RequestMapping(value="/json", params="orderNumber", headers = "Accept=application/json")
	public @ResponseBody JsonObjectResponse listByOrderNumber(
			@RequestParam(value = "orderNumber", required = true) String orderNumber) {
		JsonObjectResponse response = new JsonObjectResponse();
		Set<OrderItem> entities =  orderRepo.findByOrderNumber(orderNumber).getItems();
		response.setMessage("All order items retrieved.");
		response.setSuccess(true);
		response.setTotal(entities.size());
		response.setData(entities);

		return response;
	}

	
	protected Page<ReportItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				//TODO: implement me!
				throw new NotImplementedException();
//				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
//				return customerService.findOpenOrderItems(customer, pageRequest);			
			}
//		TODO: implement retrieval of items by orderNumber and page
//		if (filter.get(ID)!=null && filter.get("status")==null) 
//			if (filter.get(ID)!="") 
//				return this.orderRepo.findByOrderNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				Page<ReportItem> reportItems = reportService.retrieveAllToBeShipped(null, pageRequest, true);
				return reportItems;
			}
		return null;
	}
	
	/**
	 * Site is created by Extjs and JSON
	 * @param page
	 * @param size
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "confirm", produces = "text/html")
    public String showConfirmationPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        return "orderitems/confirm";
    }
	

}