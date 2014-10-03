package de.switajski.priebes.flexibleorders.service.helper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.web.helper.ProcessStep;

public class StatusFilterDispatcherTest {

    @Test
    public void shouldDispatchStatusToSpecification() throws Exception{
        // GIVEN 
        HashMap<String, String> existingFilter = createFilter(ProcessStep.values()[0].mappedString);
        
        // WHEN
        Specification<ReportItem> spec = new StatusFilterDispatcher().dispatchToSpecification(existingFilter);
        
        // THEN
        assertThat(spec, is(not(nullValue())));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailIfStatusDoesNotExist(){
        // GIVEN / WHEN
        new StatusFilterDispatcher().dispatchToSpecification(createFilter("asdfjhiuh"));
        
        // THEN Exception expected
    }
    
    @Test
    public void shouldDispatchAllAvailableProcessSteps(){
        for (ProcessStep step:ProcessStep.values()){
            //GIVEN / WHEN
            new StatusFilterDispatcher().dispatchToSpecification(createFilter(step.mappedString));
        }
        
        // THEN expect no exception
    }

    private HashMap<String, String> createFilter(String filter) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(StatusFilterDispatcher.STATUS_STRING, filter);
        return map;
    }
    
}
