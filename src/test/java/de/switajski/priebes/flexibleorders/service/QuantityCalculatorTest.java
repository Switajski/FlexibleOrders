package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.InvoiceItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ReceiptItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingItemBuilder;

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
				.toBeConfirmed(orderItem);

		// THEN
		assertThatQtyIsQtyMinusQtyProcessed(calculatedQuantity);
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
				.toBeAgreed(DeliveryHistory.createFrom(orderItem));

		// THEN
		assertThatQtyIsQtyMinusQtyProcessed(calculatedQuantity);
	}

	@Test
	public void toBeShipped_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addReportItem(
				givenAgreementItem(QTY));
		orderItem.addReportItem(
				givenShippingItem(QTY_PROCESSED));
		
		// WHEN
		Integer calculatedQuantity = QuantityCalculator
				.toBeShipped(DeliveryHistory.createFrom(orderItem));
		
		// THEN
		assertThatQtyIsQtyMinusQtyProcessed(calculatedQuantity);
	}

	@Test
	public void toBeInvoiced_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addReportItem(
				givenShippingItem(QTY));
		orderItem.addReportItem(
				givenInvoiceItem(QTY_PROCESSED));

		// WHEN
		Integer calculatedQuantity = QuantityCalculator.toBeInvoiced(
				DeliveryHistory.createFrom(orderItem));

		// THEN
		assertThatQtyIsQtyMinusQtyProcessed(calculatedQuantity);
	}

	@Test
	public void toBePaid_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addReportItem(
				givenInvoiceItem(QTY));
		orderItem.addReportItem(
				givenReceiptItem(QTY_PROCESSED));

		// WHEN
		Integer calculatedQuantity = QuantityCalculator.toBePaid(
				DeliveryHistory.createFrom(orderItem));

		// THEN
		assertThatQtyIsQtyMinusQtyProcessed(calculatedQuantity);
	}
	
	private void assertThatQtyIsQtyMinusQtyProcessed(Integer calculatedQuantity) {
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

	private ShippingItem givenShippingItem(int qtyProcessed) {
		return new ShippingItemBuilder()
				.setReport(
						givenDeliveryNotes())
				.setQuantity(qtyProcessed)
				.setItem(orderItem)
				.build();
	}

	private Report givenDeliveryNotes() {
		return new DeliveryNotes(
				"L-123",
				address,
				new Amount(BigDecimal.TEN, Currency.EUR));
	}

	private ReportItem givenReceiptItem(int qtyProcessed) {
		return new ReceiptItemBuilder()
				.setReport(
						givenReceipt())
				.setQuantity(qtyProcessed)
				.setItem(orderItem)
				.build();
	}

	private Receipt givenReceipt() {
		return new Receipt("Q-123", new Date());
	}

	private InvoiceItem givenInvoiceItem(int qtyProcessed) {
		return new InvoiceItemBuilder()
				.setReport(
						givenInvoice())
				.setQuantity(qtyProcessed)
				.setItem(orderItem)
				.build();
	}

	private Report givenInvoice() {
		return new Invoice("R-123", "pay fast", address);
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
