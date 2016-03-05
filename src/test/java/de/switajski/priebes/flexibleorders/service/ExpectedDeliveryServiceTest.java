package de.switajski.priebes.flexibleorders.service;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.application.DateUtils;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.PurchaseAgreementBuilder;

public class ExpectedDeliveryServiceTest {

    @Mock
    PurchaseAgreementReadService purchaseAgreementService;

    @InjectMocks
    ExpectedDeliveryService service = new ExpectedDeliveryService();

    Set<PurchaseAgreement> purchaseAgreements;

    Date actualDeliveryDate;

    LocalDate now = LocalDate.now();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        now = LocalDate.now();
    }

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldFailIfIfDifferentExpectedDeliveryDatesExist() throws Exception {
        givenPurchaseAgreements(now, now.plusWeeks(1));

        whenValidatingExpectedDeliveryDates();
    }

    @Test
    public void shouldPassIfActualDeliveryDateIsInSameWeekAsExpectedDeliveryDates() throws Exception {
        givenPurchaseAgreements(now, now);
        actualDeliveryDate = DateUtils.asDate(now);

        whenValidatingExpectedDeliveryDates();
    }

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldFailIfActualDeliveryDateIsNotExpectedDeliveryDates() throws Exception {
        givenPurchaseAgreements(now, now);
        actualDeliveryDate = DateUtils.asDate(now.plusWeeks(1));

        whenValidatingExpectedDeliveryDates();
    }

    @Test
    public void shouldPassIfNoExpectedDeliveryDatesAreSet() throws Exception {
        givenPurchaseAgreements(null, null);

        whenValidatingExpectedDeliveryDates();
    }

    private void whenValidatingExpectedDeliveryDates() throws Exception {
        service.validateExpectedDeliveryDates(new HashSet<ReportItem>(), actualDeliveryDate);
    }

    private void givenPurchaseAgreements(LocalDate firstDate, LocalDate secondDate) {
        purchaseAgreements = new HashSet<PurchaseAgreement>();
        purchaseAgreements.add(new PurchaseAgreementBuilder().setExpectedDelivery(firstDate).build());
        purchaseAgreements.add(new PurchaseAgreementBuilder().setExpectedDelivery(secondDate).build());
        when(purchaseAgreementService.withoutDeviations(anyCollectionOf(ReportItem.class))).thenReturn(purchaseAgreements);
    }
}
