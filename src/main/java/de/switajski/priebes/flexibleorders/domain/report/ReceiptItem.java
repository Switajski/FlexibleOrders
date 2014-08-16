package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class ReceiptItem extends ReportItem {

	public ReceiptItem() {
	}

	public ReceiptItem(Receipt receipt,
			OrderItem orderItem, int quantity, Date receivedPaymentDate) {
		super(receipt, orderItem, quantity, receivedPaymentDate);
	}

	@Override
	public String provideStatus() {
		return "bezahlt";
	}

}
