package de.switajski.priebes.flexibleorders.service.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

public class DeliveryNotesNumberGeneratorServiceTest {

    @Mock
    ReportRepository reportRepository;

    @InjectMocks
    DeliveryNotesNumberGeneratorService service = new DeliveryNotesNumberGeneratorService();

    String no = "1601001";
    String orderConfirmationNumber = "AB" + no;
    String generatedNumber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGenerateByOrderConfirmationNumber() {
        whenGeneratingNumber();

        assertThat(generatedNumber, is(equalTo("L" + no)));
    }

    @Test
    public void shouldAppendPrefixIfDeliveryNotesNumberALreadyExsists() {
        givenDeliveryNotes("L" + no);

        whenGeneratingNumber();

        assertThat(generatedNumber, is(equalTo("L" + no + DeliveryNotesNumberGeneratorService.PENDING_ITEMS_SUFFIX + 1)));
    }

    @Test
    public void shouldAppendPrefixIfDeliveryNotesNumberAndPendingNoAlreadyExsists() {
        givenDeliveryNotes("L" + no);
        givenDeliveryNotes("L" + no + DeliveryNotesNumberGeneratorService.PENDING_ITEMS_SUFFIX + 1);

        whenGeneratingNumber();

        assertThat(generatedNumber, is(equalTo("L" + no
                + DeliveryNotesNumberGeneratorService.PENDING_ITEMS_SUFFIX + 2)));
    }

    private void givenDeliveryNotes(String string) {
        when(reportRepository.findByDocumentNumber(string)).thenReturn(new OrderConfirmation());
    }

    private void whenGeneratingNumber() {
        generatedNumber = service.byOrderConfrimationNumber(orderConfirmationNumber);
    }

}
