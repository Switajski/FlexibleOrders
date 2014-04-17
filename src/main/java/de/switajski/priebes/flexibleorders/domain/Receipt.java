package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Receipt extends Report {

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date paymentReceivedDate;

	public Receipt(String receiptNumber, Date paymentReceivedDate) {
		super(receiptNumber);
		this.paymentReceivedDate = paymentReceivedDate;
	}

	public Date getPaymentReceivedDate() {
		return paymentReceivedDate;
	}

	public void setPaymentReceivedDate(Date paymentReceivedDate) {
		this.paymentReceivedDate = paymentReceivedDate;
	}
}
