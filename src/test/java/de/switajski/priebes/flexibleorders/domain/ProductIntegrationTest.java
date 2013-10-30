package de.switajski.priebes.flexibleorders.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ProductBuilder;

public class ProductIntegrationTest extends AbstractIntegrationTest<Product> {

	@Autowired
	private ProductRepository productRepo;
	
	@Override
	protected Product createEntity() {
		Product product = 
				new ProductBuilder(
						new Category(), 12345L, ProductType.PRODUCT)
			.build();
		return product;
	}

	@Override
	protected JpaRepository<Product, ?> getRepository() {
		return productRepo;
	}

}
