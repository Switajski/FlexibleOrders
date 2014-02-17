package de.switajski.priebes.flexibleorders.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;

@RequestMapping("/products")
@Controller
public class CatalogProductController extends ExceptionController{

	private CatalogProductRepository cProductRepo;
	
	private static Logger log = Logger.getLogger(CatalogProductController.class);
	
	@Autowired
	public CatalogProductController (CatalogProductRepository catalogProductRepo){
		this.cProductRepo = catalogProductRepo;
	}
	
	@RequestMapping(value = "/json", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts){
		JsonObjectResponse response = new JsonObjectResponse();
		Page<CatalogProduct> customer = cProductRepo.findAll(new PageRequest(page-1, limit));
		response.setData(customer.getContent());
		response.setTotal(customer.getTotalElements());
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}
	
}
