package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;

public class ShippingCostsCalculatorTest {

	private static Amount shippingCosts = new Amount(
			BigDecimal.TEN,
			Currency.EUR);
	private static Amount shippingCosts2 = new Amount(
			BigDecimal.valueOf(4.5d),
			Currency.EUR);

	@Test
	public void calculate_AmountShouldBeSummedShippingCosts() {
		// GIVEN
		Order order = OrderBuilder.B11();
		DeliveryNotes l12 = givenDeliveryNotesL12(order);
		DeliveryNotes l13 = givenDeliveryNotesL13(order);

		// WHEN
		Amount calculatedCosts = new ShippingCostsCalculator().calculate(
				new HashSet<ShippingItem>(
						Arrays.asList(
								l12.getShippingItems().iterator().next(),
								l13.getShippingItems().iterator().next()))
				);

		// THEN
		assertThat(calculatedCosts, equalTo(shippingCosts.add(shippingCosts2)));
	}

	private DeliveryNotes givenDeliveryNotesL13(Order order) {
		DeliveryNotes l13 = new DeliveryNotesBuilder()
		.setShippingCosts(shippingCosts2)
		.setDocumentNumber("L13")
		.build();
		for (OrderItem oi : order.getItems()) {
			oi.addReportItem(new ShippingItem(
					l13,
					oi,
					5,
					new Date()));
		}
		return l13;
	}

	private DeliveryNotes givenDeliveryNotesL12(Order order) {
		DeliveryNotes l12 = new DeliveryNotesBuilder()
			.setShippingCosts(shippingCosts)
			.setDocumentNumber("L12")
			.build();
		for (OrderItem oi : order.getItems()) {
			oi.addReportItem(new ShippingItem(
					l12,
					oi,
					5,
					new Date()));
		}
		return l12;
	}

}
