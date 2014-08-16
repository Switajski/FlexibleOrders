package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Receipt extends Report {

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date paymentReceived;

	protected Receipt() {
	}

	public Receipt(String receiptNumber, Date paymentReceived) {
		super(receiptNumber);
		this.paymentReceived = paymentReceived;
	}

	public Date getPaymentReceivedDate() {
		return paymentReceived;
	}

	public void setPaymentReceivedDate(Date paymentReceivedDate) {
		this.paymentReceived = paymentReceivedDate;
	}
}
