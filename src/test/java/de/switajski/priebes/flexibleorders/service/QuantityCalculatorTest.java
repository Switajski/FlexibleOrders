package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;

public class QuantityCalculatorTest {

	private static final int QTY_PROCESSED = 5;
	private static final int QTY = 7;
	private OrderItem orderItem;
	private Address address = AddressBuilder.createDefault();

	@Test
	public void toBeConfirmed_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addReportItem(
				givenConfirmationItem(QTY_PROCESSED));

		// WHEN
		Integer calculatedQuantity = QuantityCalculator
				.calculateLeft(orderItem);

		// THEN
		assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
	}

	@Test
	public void toBeAgreed_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addReportItem(
				givenConfirmationItem(QTY));
		orderItem.addReportItem(
				givenAgreementItem(QTY_PROCESSED));

		// WHEN
		Integer calculatedQuantity = QuantityCalculator
				.calculateLeft(orderItem.getConfirmationItems().iterator().next());

		// THEN
		assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
	}

	
	private ConfirmationItem givenConfirmationItem(int quantityProcessed) {
		return new ConfirmationItemBuilder()
				.setReport(
						givenConfirmationReport())
				.setQuantity(quantityProcessed)
				.setItem(orderItem)
				.build();
	}

	private ReportItem givenAgreementItem(int qtyProcessed) {
		return new AgreementItemBuilder()
				.setReport(givenOrderAgreement())
				.setQuantity(qtyProcessed)
				.setItem(orderItem)
				.build();
	}

	private OrderAgreement givenOrderAgreement() {
		return new OrderAgreementBuilder().setDocumentNumber("AU-123").build();
	}

	private OrderItem givenOrderItem(int quantity) {
		return new OrderItemBuilder(
				givenOrder(),
				givenProduct(),
				quantity)
				.build();
	}

	private OrderConfirmation givenConfirmationReport() {
		return new OrderConfirmation(
				"AB-123",
				address,
				address);
	}

	private Product givenProduct() {
		return new CatalogProductBuilder(
				"pro",
				234L,
				ProductType.PRODUCT)
				.build().toProduct();
	}

	private Order givenOrder() {
		return new OrderBuilder()
				.setCustomer(
						CustomerBuilder.buildWithGeneratedAttributes(2))
				.build();
	}
}
