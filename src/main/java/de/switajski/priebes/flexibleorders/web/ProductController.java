package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.service.ProductService;

@RequestMapping("/products")
@Controller
@RooWebScaffold(path = "products", formBackingObject = Product.class)
public class ProductController extends JsonController<Product> {
	
	@Autowired
	public ProductController(ProductService productService) {
		super(productService);
	}

	@Override
	protected void resolveDependencies(Product entity) {
				
	}

	@Override
	protected Page<Product> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void deleteStepBackward(Product item) {
		// TODO Auto-generated method stub
		
	}
	
}
