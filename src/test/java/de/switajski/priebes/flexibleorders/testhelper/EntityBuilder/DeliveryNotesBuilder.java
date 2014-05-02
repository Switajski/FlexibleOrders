package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.application.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;

/**
 * @author Marek Switajski
 *
 */
public class DeliveryNotesBuilder extends ReportBuilder<DeliveryNotesBuilder> implements Builder<DeliveryNotes>{

	private Address shippedAddress;
	private ShippedSpecification shippedSpecification;
	private String trackNumber;
	private String packageNumber;
	private Amount shippingCosts;
	
	@Override
	public DeliveryNotes build() {
		DeliveryNotes dn = new DeliveryNotes(super.documentNumber, shippedSpecification, shippedAddress, shippingCosts);
		super.build(dn);
		dn.setTrackNumber(trackNumber);
		dn.setPackageNumber(packageNumber);
		return dn;
	}
	
	public DeliveryNotesBuilder setShippedAddress(Address shippedAddress) {
		this.shippedAddress = shippedAddress;
		return this;
	}
	
	public DeliveryNotesBuilder setShippedSpecification(ShippedSpecification shippedSpecification) {
		this.shippedSpecification = shippedSpecification;
		return this;
	}
	
	public DeliveryNotesBuilder setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
		return this;
	}
	
	public DeliveryNotesBuilder setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
		return this;
	}
	
	public DeliveryNotesBuilder setShippingCosts(Amount shippingCosts) {
		this.shippingCosts = shippingCosts;
		return this;
	}

}
