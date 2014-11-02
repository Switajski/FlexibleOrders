package de.switajski.priebes.flexibleorders.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.service.CatalogProductServiceImpl;

/**
 * 
 * @author Marek Switajski
 *
 */
@RequestMapping("/products")
@Controller
public class CatalogProductController extends ExceptionController{

    @Autowired
	private CatalogProductRepository cProductRepo;
    @Autowired
    private CatalogProductServiceImpl catalogProductService;
	
	@RequestMapping(value = "/json", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts){
		JsonObjectResponse response = new JsonObjectResponse();
		Page<CatalogProduct> catalogProduct = cProductRepo.findAll(new PageRequest(page-1, limit));
		response.setData(catalogProduct.getContent());
		response.setTotal(catalogProduct.getTotalElements());
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}
	
	@RequestMapping(value = "/listFromMagento", method=RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listInJson(@RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "query", required = false) String query){
	    JsonObjectResponse response = new JsonObjectResponse();
        Page<CatalogProduct> catalogProduct = catalogProductService.findByKeyword(new PageRequest(page-1, limit), query);
        response.setData(catalogProduct.getContent());
        response.setTotal(catalogProduct.getTotalElements());
        response.setMessage("All entities retrieved.");
        response.setSuccess(true);

        return response;
	    
    }
	
}
