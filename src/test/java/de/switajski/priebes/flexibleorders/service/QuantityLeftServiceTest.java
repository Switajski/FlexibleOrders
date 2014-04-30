package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.application.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.application.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.InvoiceItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ReceiptItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingItemBuilder;

public class QuantityLeftServiceTest {

	private static final int QTY_PROCESSED = 5;
	private static final int QTY = 7;
	private static Address address = AddressBuilder
			.buildWithGeneratedAttributes(1);
	private OrderItem orderItem;

	@Test
	public void orderItemToItemDto_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem
				.addHandlingEvent(
				givenConfirmationItem(QTY_PROCESSED));

		// WHEN
		// TODO: Integer calulatedQuantity =
		// quantityLeftService.retrieveQuantityLeft();
		Integer calculatedQuantity = orderItem.toItemDto().getQuantityLeft();

		// THEN
		assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
	}

	private ConfirmationItem givenConfirmationItem(int quantityProcessed) {
		return new ConfirmationItemBuilder()
				.setReport(
						givenConfirmationReport())
				.setQuantity(quantityProcessed)
				.setType(ReportItemType.CONFIRM)
				.setItem(orderItem)
				.build();
	}

	@Test
	public void calculateQuantityLeftConfirm_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addHandlingEvent(
				givenConfirmationItem(QTY));
		orderItem.addHandlingEvent(
				givenShippingItem(QTY_PROCESSED));

		// WHEN
		// TODO: Integer calulatedQuantity =
		// quantityLeftService.retrieveQuantityLeft();
		Integer calculatedQuantity = orderItem
				.calculateQuantityLeft(ReportItemType.CONFIRM);

		// THEN
		assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
	}

	private ShippingItem givenShippingItem(int qtyProcessed) {
		return new ShippingItemBuilder()
				.setReport(
						givenDeliveryNotes())
				.setQuantity(qtyProcessed)
				.setType(ReportItemType.SHIP)
				.setItem(orderItem)
				.build();
	}

	private Report givenDeliveryNotes() {
		return new DeliveryNotes(
				"L-123",
				new ShippedSpecification(false, false),
				address,
				new Amount(BigDecimal.TEN, Currency.EUR));
	}

	@Test
	public void calculateQuantityLeftShip_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addHandlingEvent(
				givenConfirmationItem(QTY));
		orderItem.addHandlingEvent(
				givenShippingItem(QTY));
		orderItem.addHandlingEvent(
				givenInvoiceItem(QTY_PROCESSED));

		// WHEN
		// TODO: Integer calulatedQuantity =
		// quantityLeftService.retrieveQuantityLeft();
		Integer calculatedQuantity = orderItem
				.calculateQuantityLeft(ReportItemType.SHIP);

		// THEN
		assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
	}

	@Test
	public void calculateQuantityLeftInvoice_qtyLeftShouldBeQtyMinusQtyProcessed() {
		// GIVEN
		orderItem = givenOrderItem(QTY);
		orderItem.addHandlingEvent(
				givenConfirmationItem(QTY));
		orderItem.addHandlingEvent(
				givenShippingItem(QTY));
		orderItem.addHandlingEvent(
				givenInvoiceItem(QTY));
		orderItem.addHandlingEvent(
				givenReceiptItem(QTY_PROCESSED));

		// WHEN
		// TODO: Integer calulatedQuantity =
		// quantityLeftService.retrieveQuantityLeft();
		Integer calculatedQuantity = orderItem
				.calculateQuantityLeft(ReportItemType.INVOICE);

		// THEN
		assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
	}

	private ReportItem givenReceiptItem(int qtyProcessed) {
		return new ReceiptItemBuilder()
				.setReport(
						givenReceipt())
				.setQuantity(qtyProcessed)
				.setType(ReportItemType.PAID)
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
				.setType(ReportItemType.INVOICE)
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

	private ConfirmationReport givenConfirmationReport() {
		return new ConfirmationReport(
				"AB-123",
				address,
				address,
				new ConfirmedSpecification(false, false));
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
