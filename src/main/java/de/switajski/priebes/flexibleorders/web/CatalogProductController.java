package de.switajski.priebes.flexibleorders.web;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.service.conversion.ProductConversionService;
import de.switajski.priebes.flexibleorders.web.dto.MagentoApiProductResponseObject;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

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
    private ProductConversionService productConversionService;
	
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
	
	@RequestMapping(value = "/retrieveRecommededPriceNet", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse retrieveRecommededPriceNet(
			@RequestParam(value = "productNumber", required = false) Long productNumber){
		CatalogProduct cProduct = cProductRepo.findByProductNumber(productNumber);
		if (cProduct == null)
			throw new IllegalArgumentException("Produktnummer nicht gefunden");
		Amount recommendedPriceNet = cProduct.getRecommendedPriceNet();
		return ExtJsResponseCreator.createResponse(recommendedPriceNet);
	}
	
	@RequestMapping(value = "/listFromMagento", method=RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listInJson(@RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "query", required = false) String query){
	    
	    String url = "http://switajski.de/api/rest/products";
	    StringBuilder urlBuilder = new StringBuilder(url);
	    urlBuilder.append("?limit=").append(limit);
	    urlBuilder.append("&page=").append(page);
	    if (query != null){
	        if (StringUtils.containsWhitespace(query))
	            throw new IllegalArgumentException("Kann keine Leerzeichen parsen");
	        urlBuilder.append("&filter[1][attribute]=name&filter[1][like]=%").append(query).append("%");
	    }
	    
        try {
            RestTemplate restTemplate = new RestTemplate();
            MagentoApiProductResponseObject productMap = restTemplate.getForObject(urlBuilder.toString(), MagentoApiProductResponseObject.class);
            Set<CatalogProduct> products = productConversionService.convert(productMap.values());
            return ExtJsResponseCreator.createResponse(products);
        }
        catch (RestClientException e) {
            return ExtJsResponseCreator.createFailedReponse(e);
        }
    }
	
}
