package de.switajski.priebes.flexibleorders.service.api;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.testconfiguration.AbstractSpringContextTestConfiguration;

public class MigrateTest extends AbstractSpringContextTestConfiguration {

    @Autowired
    ReportItemRepository reportRepo;

    @Test
    @Rollback(false)
    @Transactional
    public void execute() {
        List<ReportItem> all = reportRepo.findAll();
        for (ReportItem ri : all) {
            ri.setOverdue(0);
        }
    }

}
