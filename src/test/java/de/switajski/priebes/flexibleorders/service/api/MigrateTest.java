package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.testconfiguration.AbstractSpringContextTestConfiguration;

public class MigrateTest extends AbstractSpringContextTestConfiguration {

    @Autowired
    ReportItemRepository reportRepo;

    @Test
    @Transactional
    public void execute() {
        reportRepo.findAll().stream().forEach(ri -> {
            ri.setCreated(new Date());
            reportRepo.save(ri);
        });
    }
}
