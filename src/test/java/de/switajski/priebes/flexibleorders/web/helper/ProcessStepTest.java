package de.switajski.priebes.flexibleorders.web.helper;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProcessStepTest {

    @Test
    public void shouldMapEnumToString(){
        assertThat(ProcessStep.INVOICED.mappedString, isA(String.class));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailWhenMappingNonExistentStringToEnum(){
        ProcessStep.mapFromString("non existing");
    }
    
    @Test
    public void shouldMapStringToEnum(){
        for (ProcessStep step : ProcessStep.values())
            assertThat(ProcessStep.mapFromString(step.mappedString), isA(ProcessStep.class));
    }
}
