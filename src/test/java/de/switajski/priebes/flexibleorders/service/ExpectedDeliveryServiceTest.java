package de.switajski.priebes.flexibleorders.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.mockito.InjectMocks;

import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;

public class ExpectedDeliveryServiceTest {

    @InjectMocks
    ExpectedDeliveryService service;
    private Set<ReportItem> reportItems;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldNotDeliverIfContradictoryExpectedDeliveryDatesExist() {
        Date date = new Date();
        reportItems = new HashSet<ReportItem>();
        reportItems.add(ConfirmationItemBuilder.create(DateUtils.addWeeks(date, 1)));
        reportItems.add(ConfirmationItemBuilder.create(date));

        service.validateExpectedDeliveryDates(reportItems, new DeliveryNotes());
    }
}
