package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.util.Date;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliverParameter {
	public String deliveryNotesNumber;
	public Long customerNumber;
	public String trackNumber;
	public String packageNumber;
	//TODO: make shipment an itemDto, in which you can choose the deliveryMethod
	public Amount shipment;
	public Date created;
	public List<ItemDto> agreementItemDtos;
    public boolean ignoreContradictoryExpectedDeliveryDates;
    public DeliveryMethod deliveryMethod;

	public DeliverParameter(String deliveryNotesNumber, String trackNumber,
			String packageNumber, Amount shipment, Date created,
			List<ItemDto> agreementItemDtos) {
		this.deliveryNotesNumber = deliveryNotesNumber;
		this.trackNumber = trackNumber;
		this.shipment = shipment;
		this.packageNumber = packageNumber;
		this.created = created;
		this.agreementItemDtos = agreementItemDtos;
	}

    public DeliverParameter() {
    }
}