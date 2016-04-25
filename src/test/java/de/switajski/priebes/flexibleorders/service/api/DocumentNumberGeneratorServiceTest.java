package de.switajski.priebes.flexibleorders.service.api;

import static java.lang.Integer.parseInt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;

public class DocumentNumberGeneratorServiceTest {

    @InjectMocks
    DocumentNumberGeneratorService service = new DocumentNumberGeneratorService();

    @Mock
    OrderRepository orderRepository;
    @Mock
    ReportRepository reportRepository;

    String ramdomNoFromOrderingCustomer = "123456";
    String noFromFlexibleOrders = "1601001";
    String orderConfirmationNumber = "AB" + noFromFlexibleOrders;
    String year = "15", month = "01", randomDay = "12";

    /**
     * result
     */
    String generated;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldIterateOrderNumberInGivenFormat() {
        givenLastSavedOrder("065");

        whenGeneratingOrderNumber();

        assertThat(generated, is(equalTo("B" + formattedDocumentNumber("066"))));
    }

    /**
     * An order number is usually already given by the ordering customer.
     * Nevertheless the user of FlexibleOrders wants to have its own numbers.
     */
    @Test
    public void shouldGenerateOwnNumberIfOrderNumberIsFromCustomer() {
        givenLastSavedOrder("065");
        givenLastSavedReport("062");

        whenGeneratingNumberFromOrderNumber(ramdomNoFromOrderingCustomer);

        assertThat(generated, is(equalTo(formattedDocumentNumber("066"))));
    }

    @Test
    public void shouldConsiderLastSavedDocumentNumberWhenGeneratingNextNumberAndOrderNumberIsFromCustomer() {
        givenLastSavedOrder("065");
        givenLastSavedReport("067");

        whenGeneratingNumberFromOrderNumber(ramdomNoFromOrderingCustomer);

        assertThat(generated, is(equalTo(formattedDocumentNumber("068"))));
    }

    @Test
    public void shouldRecognizeNumbersFromFlexibleOrders() {
        givenLastSavedOrder("065");
        givenLastSavedReport("062");

        String number = whenGeneratingNumberFromOrderNumber("B" + formattedDocumentNumber("066"));

        assertThat(number, is(equalTo(formattedDocumentNumber("066"))));
    }

    @Test
    public void shouldGenerateByOrderConfirmationNumber() {
        whenGeneratingNumber();

        assertThat(generated, is(equalTo("L" + noFromFlexibleOrders)));
    }

    @Test
    public void shouldAppendPrefixIfDeliveryNotesNumberALreadyExsists() {
        givenDeliveryNotes("L" + noFromFlexibleOrders);

        whenGeneratingNumber();

        assertThat(generated, is(equalTo("L" + noFromFlexibleOrders + DocumentNumberGeneratorService.PENDING_ITEMS_SUFFIX + 1)));
    }

    @Test
    public void shouldAppendPrefixIfDeliveryNotesNumberAndPendingNoAlreadyExsists() {
        givenDeliveryNotes("L" + noFromFlexibleOrders);
        givenDeliveryNotes("L" + noFromFlexibleOrders + DocumentNumberGeneratorService.PENDING_ITEMS_SUFFIX + 1);

        whenGeneratingNumber();

        assertThat(generated, is(equalTo("L" + noFromFlexibleOrders
                + DocumentNumberGeneratorService.PENDING_ITEMS_SUFFIX + 2)));
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private void givenDeliveryNotes(String string) {
        when(reportRepository.findByDocumentNumber(string)).thenReturn(new OrderConfirmation());
    }

    private void whenGeneratingNumber() {
        generated = service.byOrderConfrimationNumber(orderConfirmationNumber);
    }

    private String whenGeneratingNumberFromOrderNumber(String oNo) {
        generated = service.yymmggg(LocalDate.of(parseInt("20" + year), parseInt(month), parseInt(randomDay)), oNo);
        return generated;
    }

    private String whenGeneratingOrderNumber() {
        generated = service.byymmggg(LocalDate.of(parseInt("20" + year), parseInt(month), parseInt(randomDay)));
        return generated;
    }

    private void givenLastSavedOrder(String sequence) {
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(
                new PageImpl<Order>(Arrays.asList(
                        new OrderBuilder().setOrderNumber(sequence).build())));
    }

    private void givenLastSavedReport(String sequence) {
        when(reportRepository.findAll(any(Specification.class))).thenReturn(
                Arrays.asList(new DeliveryNotesBuilder().setDocumentNumber("L" + year + month + sequence).build()));
    }

    private String formattedDocumentNumber(String sequence) {
        return year + month + sequence;
    }
}
