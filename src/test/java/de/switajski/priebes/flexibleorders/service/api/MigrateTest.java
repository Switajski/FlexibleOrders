package de.switajski.priebes.flexibleorders.service.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.testconfiguration.AbstractSpringContextTestConfiguration;

public class MigrateTest extends AbstractSpringContextTestConfiguration {

    @Autowired
    ReportItemRepository reportRepo;

    @PersistenceContext
    EntityManager em;

    /**
     * <i> Could also be done by an other arbitrary attributes, that marks the
     * JPA entity in the entity manager dirty</i>
     */
    @Test
    @Rollback(false)
    @Transactional
    public void updateAllOverdues() {
        reportRepo.findAll().stream().forEach(ri -> {
            ri.forceCacheUpdate();
        });
    }

}
