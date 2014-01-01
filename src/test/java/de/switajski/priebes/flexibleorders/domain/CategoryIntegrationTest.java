package de.switajski.priebes.flexibleorders.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CategoryBuilder;

public class CategoryIntegrationTest extends AbstractIntegrationTest<Category>{

	@Autowired private CategoryRepository catRepo;
	
	@Override
	protected Category createEntity() {
		return CategoryBuilder.buildWithGeneratedAttributes(3146);
	}

	@Override
	protected JpaRepository<Category, Long> getRepository() {
		return catRepo;
	}
}
