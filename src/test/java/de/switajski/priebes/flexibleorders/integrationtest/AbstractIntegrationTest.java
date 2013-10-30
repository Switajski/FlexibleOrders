package de.switajski.priebes.flexibleorders.integrationtest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.Rollback;

import de.switajski.priebes.flexibleorders.domain.GenericEntity;

public abstract class AbstractIntegrationTest<T extends GenericEntity> extends AbstractTestWithSpringContext {

	//TODO: Sollte eine Name is null exception werfen
	@Test
	@Rollback(true)
	public void shouldCreate(){
		T entity = createEntity();
		T savedEntity = getRepository().save(entity);
		assertNotNull(savedEntity.getId());
		
	}

	protected abstract T createEntity();
	
	protected abstract JpaRepository<T, ?> getRepository();
}
