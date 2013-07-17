package de.switajski.priebes.flexibleorders.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.EntityReadService;

public abstract class JsonController<T> {

	EntityReadService<T> entityReadService;

	public JsonController(EntityReadService<T> readService) {
		this.entityReadService = readService;
	}
	
	@RequestMapping(value="/json", headers = "Accept=application/json")
	public @ResponseBody JsonObjectResponse listAllEntitiesInJson(
			@RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "filter", required = false) String filters) {
 	
		JsonObjectResponse response = new JsonObjectResponse();
		Page<T> entities =  entityReadService.findAll(new PageRequest(page-1, limit-1));
		response.setMessage("All orders retrived.");
		response.setSuccess(true);
		response.setTotal(entityReadService.countAll());
		response.setData(entities.getContent());
		
		return response;
	}
	
}
