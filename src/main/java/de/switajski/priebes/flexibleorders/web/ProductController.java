package de.switajski.priebes.flexibleorders.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.domain.Product;
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
	
}
