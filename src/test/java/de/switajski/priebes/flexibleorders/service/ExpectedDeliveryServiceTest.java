package de.switajski.priebes.flexibleorders.service;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.PurchaseAgreementBuilder;

public class ExpectedDeliveryServiceTest {

    @Mock
    PurchaseAgreementService purchaseAgreementService;

    @InjectMocks
    ExpectedDeliveryService service = new ExpectedDeliveryService();

    Set<PurchaseAgreement> purchaseAgreements;

    Date actualDeliveryDate;

    LocalDate now = new LocalDate();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        now = new LocalDate();
    }

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldFailIfIfDifferentExpectedDeliveryDatesExist() {
        givenPurchaseAgreements(now, now.plusWeeks(1));

        whenValidatingExpectedDeliveryDates();
    }

    @Test
    public void shouldPassIfActualDeliveryDateIsInSameWeekAsExpectedDeliveryDates() {
        givenPurchaseAgreements(now, now);
        actualDeliveryDate = now.toDateTimeAtStartOfDay().toDate();

        whenValidatingExpectedDeliveryDates();
    }

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldFailIfActualDeliveryDateIsNotExpectedDeliveryDates() {
        givenPurchaseAgreements(now, now);
        actualDeliveryDate = now.plusWeeks(1).toDateTimeAtStartOfDay().toDate();

        whenValidatingExpectedDeliveryDates();
    }

    @Test
    public void shouldPassIfNoExpectedDeliveryDatesAreSet() {
        givenPurchaseAgreements(null, null);

        whenValidatingExpectedDeliveryDates();
    }

    private void whenValidatingExpectedDeliveryDates() {
        service.validateExpectedDeliveryDates(new HashSet<ReportItem>(), actualDeliveryDate);
    }

    private void givenPurchaseAgreements(LocalDate firstDate, LocalDate secondDate) {
        purchaseAgreements = new HashSet<PurchaseAgreement>();
        purchaseAgreements.add(new PurchaseAgreementBuilder().setExpectedDelivery(firstDate).build());
        purchaseAgreements.add(new PurchaseAgreementBuilder().setExpectedDelivery(secondDate).build());
        when(purchaseAgreementService.retrieve(anyCollectionOf(ReportItem.class))).thenReturn(purchaseAgreements);
    }
}
