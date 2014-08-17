package de.switajski.priebes.flexibleorders.testhelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.GenericEntity;

@Transactional
public abstract class AbstractIntegrationTest<T extends GenericEntity> extends
		AbstractSpringContextTest {

	@Rollback(true)
	public void shouldCreate() {
		T savedEntity = persistEntity();

		assertNotNull(savedEntity.getId());

	}

	@Test
	public void shouldRead() {
		T savedEntity = persistEntity();

		T retrievedEntity = getRepository().findOne(savedEntity.getId());
		assertNotNull(retrievedEntity);
	}

	@Test
	public void shouldUpdate() {
		T savedEntity = persistEntity();
		Long oldId = savedEntity.getId();

		savedEntity.setVersion(5);
		getRepository().saveAndFlush(savedEntity);

		assertEquals("Id of updated entity changed", oldId, savedEntity.getId());
	}

	@Ignore("fails because of dependencies between entities, that are not being resolved yet")
	@Test
	public void shouldDelete() {
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
