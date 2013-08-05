package de.switajski.priebes.flexibleorders.web;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.json.JsonQueryFilter;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
import de.switajski.priebes.flexibleorders.service.PriebesJoomlaImporterService;

public abstract class JsonController<T> {

	private static Logger log = Logger.getLogger(JsonController.class);
	CrudServiceAdapter<T> crudServiceAdapter;

	/* Code for Strong typing (checked)
	 */ 
	private Class< T > type;

	@SuppressWarnings("unchecked")
	public JsonController(CrudServiceAdapter<T> crudServiceAdapter) {
		this.crudServiceAdapter = crudServiceAdapter;
		/* Code for Strong typing (checked)
		 */
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class) pt.getActualTypeArguments()[0];
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
		Page<T> entities = null;
		if (filters == null|| isRequestForEmptingFilter(filters)){ 
			entities =  crudServiceAdapter.findAll(new PageRequest(page-1, limit));
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
	
	@RequestMapping(value="/json/delete", method=RequestMethod.DELETE)
	public @ResponseBody JsonObjectResponse deleteEntity(
			@RequestParam(value = "id", required = true) Long id) 
					throws Exception {
		JsonObjectResponse response = new JsonObjectResponse();
		if (crudServiceAdapter.find(id)!=null){
			deleteStepBackward(crudServiceAdapter.find(id));
			crudServiceAdapter.delete(crudServiceAdapter.find(id));
			
			response.setMessage("Entity deleted.");
			response.setSuccess(true);
		}
		else{
			response.setSuccess(false);
			response.setMessage("Entity not found");
		}
		response.setTotal(0l);	

		return response;
	}

	abstract void deleteStepBackward(T item) ;

	abstract void delete(Long id) ;

	protected abstract Page<T> findByFilterable(PageRequest pageRequest, HashMap<String, String> filterList);

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
				List<T> entities = parseJsonArray(json);
				for (T entity:entities){
					resolveDependencies(entity);
					crudServiceAdapter.save(entity);
				}
				response.setTotal(entities.size());
		} else {
				T entity = parseJsonObject(json);
				resolveDependencies(entity);
				crudServiceAdapter.save(entity);					
				response.setData(entity);
				response.setTotal(1l);
		}

		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		response.setTotal(crudServiceAdapter.countAll());

		return response;
	}

	private T parseJsonObject(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(json, type); 
	}

	@SuppressWarnings({"unchecked"})
	public List<T> parseJsonArray(String json) throws JsonParseException, JsonMappingException, IOException {
		T[] typedArray = (T[]) Array.newInstance(type,1);
		ObjectMapper mapper = new ObjectMapper();
		T[] records = (T[]) mapper.readValue(json, typedArray.getClass());

		ArrayList<T> list = new ArrayList<T>();
		for (T record:records)
			list.add(record);

		return list;
	}

	/**
	 * Resolves dependencies between domain entities. E.g. orderItem's productNumber into 
	 * the object product. This is done for technical reasons of O/R-Mapping.
	 * </br>
	 * TODO: this technical matter should be implemented by Jackson's deserializer
	 *  
	 * @param entity
	 */
	protected abstract void resolveDependencies(T entity);

}
