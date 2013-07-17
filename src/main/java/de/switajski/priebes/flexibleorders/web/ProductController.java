package de.switajski.priebes.flexibleorders.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.ProductService;

@RequestMapping("/products")
@Controller
@RooWebScaffold(path = "products", formBackingObject = Product.class)
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="/json", headers = "Accept=application/json")
	public @ResponseBody JsonObjectResponse listAllInJson(
			@RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "filter", required = false) String filters) {
 	
		JsonObjectResponse response = new JsonObjectResponse();
		Page<Product> orders = productService.findAll(new PageRequest(page, limit));
		response.setMessage("All orders retrived.");
		response.setSuccess(true);
		response.setTotal(productService.countAll());
		response.setData(orders.getContent());
		
		return response;
	}
}
