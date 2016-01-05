package de.switajski.priebes.flexibleorders.service.api;

import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;

public class OrderNumberGeneratorServiceTest {

    @InjectMocks
    OrderNumberGeneratorService orderNumberGeneratorService = new OrderNumberGeneratorService();

    @Mock
    OrderRepository orderRepo;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGenerateOrderNumberInGivenFormat() {
        // GIVEN
        String year = "15", month = "01";
        String randomDay = "12";
        String lastSavedOrder = "065";
        String consecutiveNo = "066";
        MockitoAnnotations.initMocks(this);
        when(orderRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(
                new PageImpl<Order>(Arrays.asList(
                        new OrderBuilder().setOrderNumber(lastSavedOrder).build())));

        // WHEN
        String orderNumber = orderNumberGeneratorService.yymmggg(LocalDate.of(parseInt("20" + year), parseInt(month), parseInt(randomDay)));

        // THEN
        assertThat(orderNumber, is(equalTo("B" + year + month + consecutiveNo)));
    }
}
