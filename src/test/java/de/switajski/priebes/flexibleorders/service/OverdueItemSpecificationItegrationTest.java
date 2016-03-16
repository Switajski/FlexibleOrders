package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.specification.IsConfirmationItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.OverdueItemSpecification;
import de.switajski.priebes.flexibleorders.testconfiguration.AbstractSpringContextTestConfiguration;
import de.switajski.priebes.flexibleorders.testdata.StandardTestDataRule;

public class OverdueItemSpecificationItegrationTest extends AbstractSpringContextTestConfiguration {

    @Rule
    @Autowired
    public StandardTestDataRule rule;

    @Autowired
    ReportItemRepository reportItemRepo;

    @Test
    public void retrievedOverdueItemsFromDBShouldBeTheSameAsOverdueItemsByPredicate() {
        List<ReportItem> overdueItems = reportItemRepo.findAll(
                where(new OverdueItemSpecification())
                        .and(new IsConfirmationItemSpecification()));

        assertFalse(overdueItems.isEmpty());
        assertTrue(overdueItems.stream().allMatch(new OverdueItemSpecification()));
    }

}
