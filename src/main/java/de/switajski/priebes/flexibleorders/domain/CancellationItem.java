package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class CancellationItem extends ReportItem {

	protected CancellationItem() {}

	public CancellationItem(CancelReport cancelReport, ReportItemType cancel,
			OrderItem orderItem, int quantity, Date date) {
		super(cancelReport, cancel, orderItem, quantity, date);
	}
}
