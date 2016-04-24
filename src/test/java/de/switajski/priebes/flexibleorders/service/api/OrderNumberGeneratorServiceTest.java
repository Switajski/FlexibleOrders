package de.switajski.priebes.flexibleorders.service.api;

import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;

public class OrderNumberGeneratorServiceTest {

    @InjectMocks
    OrderNumberGeneratorService orderNumberGeneratorService = new OrderNumberGeneratorService();

    @Mock
    OrderRepository orderRepo;
    @Mock
    ReportRepository reportRepo;

    String year = "15", month = "01", randomDay = "12";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGenerateOrderNumberInGivenFormat() {
        // GIVEN
        givenLastSavedOrder("065");

        // WHEN
        String orderNumber = whenGeneratingOrderNumber();

        // THEN
        assertThat(orderNumber, is(equalTo("B" + year + month + "066")));
    }

    /**
     * An order number is usually already given by the ordering customer.
     * Nevertheless the user of FlexibleOrders wants to have its own numbers.
     */
    @Test
    public void shouldGenerateDocumentNumberIndependentlyFromOrderNumber() {
        givenLastSavedOrder("065");
        givenLastSavedReport("064");

        // WHEN
        String orderNumber = orderNumberGeneratorService.yymmggg(LocalDate.of(parseInt("20" + year), parseInt(month), parseInt(randomDay)), "123456");

        // THEN
        assertThat(orderNumber, is(equalTo(year + month + "066")));
    }

    private String whenGeneratingOrderNumber() {
        String orderNumber = orderNumberGeneratorService.byymmggg(LocalDate.of(parseInt("20" + year), parseInt(month), parseInt(randomDay)));
        return orderNumber;
    }

    private void givenLastSavedOrder(String sequence) {
        when(orderRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(
                new PageImpl<Order>(Arrays.asList(
                        new OrderBuilder().setOrderNumber(sequence).build())));
    }

    private void givenLastSavedReport(String sequence) {
        when(reportRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(
                new PageImpl<Report>(Arrays.asList(
                        new DeliveryNotesBuilder().setDocumentNumber("L" + year + month + sequence).build())));
    }
}
