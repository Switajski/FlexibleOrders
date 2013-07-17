package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;

public class ProductServiceImpl extends JpaRepositoryReadService<Product> implements ProductService {

	@Autowired
	ProductServiceImpl(ProductRepository repository) {
        super(repository);
    }
	
}
