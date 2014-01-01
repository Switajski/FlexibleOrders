package de.switajski.priebes.flexibleorders.web;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.json.JsonQueryFilter;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.HandlingEventService;

@RequestMapping("/orderitems")
@Controller
@RooWebScaffold(path = "orderitems", formBackingObject = Item.class)
public class JsonController {

	private static Logger log = Logger.getLogger(JsonController.class);
	
	private static final String ID = "ordernumber";
	private static final String FILTER_STATUS = "ordered";
	private ItemRepository orderItemService;
	private CustomerService customerService;
	private HandlingEventService heService;
	private CatalogProductRepository productRepository;

	@Autowired
	public JsonController(ItemRepository itemRepo,
			CustomerService customerService,
			HandlingEventService heService,
			CatalogProductRepository productRepository) {
		this.orderItemService = itemRepo;
		this.customerService = customerService;
		this.heService = heService;
		this.productRepository = productRepository;
	}

	@RequestMapping(value="/json", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllPageable(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) 
					throws Exception {

		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json filter request:"+filters);
		JsonObjectResponse response = new JsonObjectResponse();
		Page<Item> entities = null;
		if (filters == null|| isRequestForEmptingFilter(filters)){ 
			entities =  orderItemService.findAll(new PageRequest(page-1, limit));
		} else {
			HashMap<String, String> filterList;

			filterList = deserializeFiltersJson(filters);
			entities = findByFilterable(new PageRequest(page-1, limit), filterList);
		}
		if (entities!=null){
			response.setTotal(entities.getTotalElements());
			response.setData(entities.getContent());			
		}
		else {
			response.setTotal(0l);
		}
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}

	private boolean isRequestForEmptingFilter(String filters) {
		return (filters.contains("property") && filters.contains("null"));
	}

	@RequestMapping(value="/json", method=RequestMethod.DELETE)
	public @ResponseBody JsonObjectResponse delete( @RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		JsonObjectResponse response = new JsonObjectResponse();

		if (json.charAt(0) == '['){
			List<Item> entities = parseJsonArray(json);
			for (Item entity:entities){
				orderItemService.delete(entity);
			}
			response.setTotal(entities.size());
		} else {
			Item entity = parseJsonObject(json);
			orderItemService.delete(entity);					
			response.setData(entity);
			response.setTotal(1l);
		}

		response.setMessage("Entity deleted.");
		response.setSuccess(true);

		return response;
	}

	private HashMap<String, String> deserializeFiltersJson(String filters) throws Exception {
		ObjectMapper mapper = new ObjectMapper();  
		HashMap<String, String> jsonFilters = new HashMap<String, String>();
		//	Extjs - Json for adding a filter: 	[{"property":"orderNumber","value":1}]
		if (filters.contains("property")){
			JsonQueryFilter[] typedArray = (JsonQueryFilter[]) Array.newInstance(JsonQueryFilter.class, 1);
			JsonQueryFilter[] qFilter;
			qFilter = mapper.readValue(filters, typedArray.getClass());

			for (JsonQueryFilter f:qFilter)
				jsonFilters.put(f.getProperty(), f.getValue());
		}else{

			// filters = [{"type":"string","value":"13","field":"orderNumber"}]
			JsonFilter[] typedArray = (JsonFilter[]) Array.newInstance(JsonFilter.class,1);

			JsonFilter[] filterArray;
			try {
				filterArray = mapper.readValue(filters, typedArray.getClass());
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}   
			//TODO: implement logic for multiple filters
			for (JsonFilter jsonFilter:filterArray){
				jsonFilters.put(jsonFilter.field, jsonFilter.getValue());
			}
		}
		return jsonFilters;
	}

	@RequestMapping(value="/json", /*headers = "Accept:application/json",*/ method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse create(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		JsonObjectResponse response = new JsonObjectResponse();

		if (json.charAt(0) == '['){
			List<Item> entities = parseJsonArray(json);
			for (Item entity:entities){
				orderItemService.save(entity);
			}
			response.setTotal(entities.size());
		} else {
			Item entity = parseJsonObject(json);
			orderItemService.save(entity);					
			response.setData(entity);
			response.setTotal(1l);
		}

		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		response.setTotal(orderItemService.count());

		return response;
	}

	private Item parseJsonObject(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		return (Item) mapper.readValue(json, Item.class); 
	}

	public List<Item> parseJsonArray(String json) throws JsonParseException, JsonMappingException, IOException {
		Item[] typedArray = (Item[]) Array.newInstance(Item.class,1);
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		Item[] records = (Item[]) mapper.readValue(json, typedArray.getClass());

		ArrayList<Item> list = new ArrayList<Item>();
		for (Item record:records)
			list.add(record);

		return list;
	}
	
	
	
	@RequestMapping(value="/json", params="orderNumber", headers = "Accept=application/json")
	public @ResponseBody JsonObjectResponse listByOrderNumber(
			@RequestParam(value = "orderNumber", required = true) Long orderNumber) {
		JsonObjectResponse response = new JsonObjectResponse();
		List<Item> entities =  orderItemService.findByOrderNumber(orderNumber);
		response.setMessage("All order items retrieved.");
		response.setSuccess(true);
		response.setTotal(entities.size());
		response.setData(entities);

		return response;
	}

	
	protected Page<Item> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		if (filter.get("status") != null)
			if (filter.get("status").equals(FILTER_STATUS) && filter.get("customer")!=null){
				//TODO: implement me!
				throw new NotImplementedException();
//				Customer customer = customerService.find(Long.parseLong(filter.get("customer")));
//				return customerService.findOpenOrderItems(customer, pageRequest);			
			}
		if (filter.get(ID)!=null && filter.get("status")==null) 
			if (filter.get(ID)!="") 
				return this.orderItemService.findByOrderNumber(Long.parseLong(filter.get(ID)), pageRequest);
		if (filter.get("status")!=null)
			if (filter.get("status").toLowerCase().equals(FILTER_STATUS)){
				throw new NotImplementedException();
//				Page<Item> orderItems = orderItemService.findOpen(pageRequest);
//				return orderItems;
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
