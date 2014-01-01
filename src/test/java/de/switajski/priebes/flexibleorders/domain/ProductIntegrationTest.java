package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CategoryBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;

public class ProductIntegrationTest extends AbstractIntegrationTest<CatalogProduct> {

	@Autowired
	private CatalogProductRepository productRepo;
	
	@Autowired
	private CategoryRepository catRepo;
	
	@Override
	protected CatalogProduct createEntity() {
		CatalogProduct product = 
				CatalogProductBuilder.buildWithGeneratedAttributes(4);
		return product;
	}

	@Override
	protected JpaRepository<CatalogProduct, Long> getRepository() {
		return productRepo;
	}
	
	@Test
	public void shouldFindByName(){
		String name = "name o name";
		Category cat = new CategoryBuilder("asfd", true).build();
		catRepo.saveAndFlush(cat);
		
		CatalogProduct p = createEntity();
		p.setName(name);
		
		productRepo.save(p);
		productRepo.flush();
		
		assertNotNull(productRepo.findByName(name));
	}

}
