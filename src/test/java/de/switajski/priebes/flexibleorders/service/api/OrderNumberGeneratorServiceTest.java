package de.switajski.priebes.flexibleorders.service.api;

import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static java.lang.Long.parseLong;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.service.YyMmGggOrderNoGeneratorService;

public class OrderNumberGeneratorServiceTest {
    
    @InjectMocks    
    OrderNumberGeneratorService orderNumberGeneratorService = new YyMmGggOrderNoGeneratorService();
    
    @Mock
    OrderRepository orderRepo;
    
    @Test
    @SuppressWarnings("unchecked")
    public void shouldGenerateOrderNumberInGivenFormat(){
        // GIVEN
        String year = "15", month = "01"; 
        String randomDay = "12";
        String noOfSavedOrders = "065";
        String consecutiveNo = "066";
        MockitoAnnotations.initMocks(this);
        when(orderRepo.count(any(Specification.class))).thenReturn(parseLong(noOfSavedOrders));
        
        // WHEN
        String orderNumber = orderNumberGeneratorService.generate(new LocalDate(parseInt("20" + year), parseInt(month), parseInt(randomDay)));
        
        // THEN
        assertThat(orderNumber, is(equalTo("B" + year + month + consecutiveNo)));
    }

}
