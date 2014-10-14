package de.switajski.priebes.flexibleorders.web.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.json.JsonQueryFilter;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryMethodDto;

/**
 * NOT USED - Backup for filters implementation. Method from former controller-filter-method:
 * <pre> {@code
 * 	{@literal @}RequestMapping(value="/json", method=RequestMethod.GET)
	public {@literal @}ResponseBody JsonObjectResponse listAllPageable(
			{@literal @}RequestParam(value = "page", required = true) Integer page,
			{@literal @}RequestParam(value = "start", required = false) Integer start,
			{@literal @}RequestParam(value = "limit", required = true) Integer limit,
			{@literal @}RequestParam(value = "sort", required = false) String sorts,
			{@literal @}RequestParam(value = "filter", required = false) String filters) 
					throws Exception {

		// filters = [{"type":"string","value":"13","field":"orderNumber"}]
		log.debug("received json filter request:"+filters);
		JsonObjectResponse response = new JsonObjectResponse();
		Page<ReportItem> entities = null;
		if (filters == null|| isRequestForEmptingFilter(filters)){ 
			entities =  reportService.retrieveAllToBeShipped(null, new PageRequest(page-1, limit));
		} else {
			HashMap<String, String> filterList;

			filterList = SerializationHelper.deserializeFiltersJson(filters);
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
 * }
 * </pre>
 * 
 * @author Marek Switajski
 *
 */
public class JsonSerializationHelper {
	
	public static HashMap<String, String> deserializeFiltersJson(String filters) throws Exception {
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
	
	public static List<DeliveryMethodDto> convertToJsonDeliveryMethodDtos(
			Collection<DeliveryMethod> deliveryMethods) {
		List<DeliveryMethodDto> jsonCustomers = new ArrayList<DeliveryMethodDto>();
		for (DeliveryMethod c : deliveryMethods){
			DeliveryMethodDto dto = new DeliveryMethodDto();
			dto.setId(c.getId());
			dto.setDeliveryMethodNo(c.getId());
			dto.setName(c.getName());
			dto.setName1(c.getAddress().getName1());
			dto.setName2(c.getAddress().getName2());
			dto.setStreet(c.getAddress().getStreet());
			dto.setPostalCode(c.getAddress().getPostalCode());
			dto.setCity(c.getAddress().getCity());
			dto.setCountry(c.getAddress().getCountry().toString());
			
			jsonCustomers.add(dto);
		}
		return jsonCustomers;
	}

}
