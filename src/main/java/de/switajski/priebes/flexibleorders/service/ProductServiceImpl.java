package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;

public class ProductServiceImpl extends JpaRepositoryToServiceAdapter<Product> implements ProductService {

	@Autowired
	ProductRepository productRepository2;
	
	@Autowired
	ProductServiceImpl(ProductRepository repository) {
        super(repository);
    }

	@Override
	public Product findByProductNumber(Long productNumber) {
		return this.productRepository2.findByProductNumber(productNumber);
	}
	
}
