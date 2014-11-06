package de.switajski.priebes.flexibleorders.service.helper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.web.helper.ProductionState;

public class StatusFilterDispatcherTest {

    @Test
    public void shouldDispatchStatusToSpecification() throws Exception{
        // WHEN
        Specification<ReportItem> spec = new StatusFilterDispatcher().dispatchStatus(ProductionState.values()[0]);
        
        // THEN
        assertThat(spec, is(not(nullValue())));
    }
    
}
