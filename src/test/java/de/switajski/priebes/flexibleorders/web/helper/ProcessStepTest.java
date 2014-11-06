package de.switajski.priebes.flexibleorders.web.helper;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProcessStepTest {

    @Test
    public void shouldMapEnumToString(){
        assertThat(ProductionState.INVOICED.mappedString, isA(String.class));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailWhenMappingNonExistentStringToEnum(){
        ProductionState.mapFromString("non existing");
    }
    
    @Test
    public void shouldMapStringToEnum(){
        for (ProductionState step : ProductionState.values())
            assertThat(ProductionState.mapFromString(step.mappedString), isA(ProductionState.class));
    }
}
