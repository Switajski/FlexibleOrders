package de.switajski.priebes.flexibleorders.testhelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.Rollback;

import de.switajski.priebes.flexibleorders.domain.GenericEntity;

public abstract class AbstractIntegrationTest<T extends GenericEntity> extends AbstractTestWithTestSpringContext {

	//TODO: Sollte eine Name is null exception werfen
	@Test
	@Rollback(true)
	public void shouldCreate(){
		T savedEntity = persistEntity();
		
		assertNotNull(savedEntity.getId());
		
	}
	
	@Test
	public void shouldRead(){
		T savedEntity = persistEntity();
		
		T retrievedEntity = getRepository().findOne(savedEntity.getId());
		assertNotNull(retrievedEntity);
	}
	
	@Test
	public void shouldUpdate(){
		T savedEntity = persistEntity();
		Long oldId = savedEntity.getId();
		
		savedEntity.setVersion(5);
		getRepository().saveAndFlush(savedEntity);
		
		assertEquals("Id of updated entity changed", oldId, savedEntity.getId());
	}

	@Test
	public void shouldDelete(){
		T savedEntity = persistEntity();
		Long oldId = savedEntity.getId();
		
		getRepository().delete(oldId);
		assertNull(getRepository().findOne(oldId));
	}


	private T persistEntity() {
		T entity = createEntity();
		T savedEntity = getRepository().saveAndFlush(entity);
		return savedEntity;
	}

	protected abstract T createEntity();
	
	protected abstract JpaRepository<T, Long> getRepository();
}
