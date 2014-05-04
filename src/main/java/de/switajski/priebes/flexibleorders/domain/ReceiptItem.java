package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ReceiptItem extends ReportItem {

	protected ReceiptItem() {
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
