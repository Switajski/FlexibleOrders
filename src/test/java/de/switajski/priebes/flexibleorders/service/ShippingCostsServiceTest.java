package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingCostsItemBuilder;

public class ShippingCostsServiceTest {

    private static final Amount SHIPPING_COSTS = new Amount(BigDecimal.TEN, Currency.EUR);
    private ShippingCostsService shippingCostsService = new ShippingCostsService();

    @Test
    public void shouldCalculateShippingCostsOfOneDeliveryNotes() {
        // GIVEN
        Report report = new DeliveryNotesBuilder().setItems(new HashSet<ReportItem>(Arrays.asList(
                new ShippingCostsItemBuilder().setCosts(SHIPPING_COSTS).build()
                ))).build();

        // WHEN
        Amount amount = shippingCostsService.calculate(report);

        // THEN
        assertThat(amount, is(equalTo(SHIPPING_COSTS)));
    }
}
