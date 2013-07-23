package de.switajski.priebes.flexibleorders.web;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;

public abstract class JsonController<T> {

	CrudServiceAdapter<T> crudServiceAdapter;
	private Class< T > type;

	public JsonController(CrudServiceAdapter<T> crudServiceAdapter) {
		this.crudServiceAdapter = crudServiceAdapter;
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
            @RequestParam(value = "filter", required = false) String filters) {
 	
		JsonObjectResponse response = new JsonObjectResponse();
		Page<T> entities =  crudServiceAdapter.findAll(new PageRequest(page-1, limit-1));
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		response.setTotal(crudServiceAdapter.countAll());
		response.setData(entities.getContent());
		
		return response;
	}
	
	@RequestMapping(value="/json", /*headers = "Accept:application/json",*/ method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse create(@RequestBody String json) {
 		ObjectMapper mapper = new ObjectMapper();
 		T myObject = null;
		try {
			myObject = mapper.readValue(json, new TypeReference<Map<String, T>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JsonObjectResponse response = new JsonObjectResponse();
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		response.setTotal(crudServiceAdapter.countAll());
		response.setData(myObject);
		
		return response;
	}
	
}
