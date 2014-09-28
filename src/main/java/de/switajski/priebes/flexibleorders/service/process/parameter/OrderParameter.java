package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderParameter {
	public Long customerNumber;
	public String orderNumber;
	public Date created;
	public LocalDate expectedDelivery;
	public List<ItemDto> reportItems;

	public OrderParameter() {
		created = new Date();
	}
	
	public OrderParameter(Long customerNumber, String orderNumber,
			Date created, List<ItemDto> reportItems) {
		this.customerNumber = customerNumber;
		this.orderNumber = orderNumber;
		this.created = created;
		this.reportItems = reportItems;
	}

}