package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;

/**
 * @author Marek Switajski
 *
 */
public class DeliveryNotesBuilder extends ReportBuilder<DeliveryNotes, Builder<DeliveryNotes>>{

	private Address shippedAddress;
	private String trackNumber;
	private String packageNumber;
	private Amount shippingCosts;
	
	@Override
	public DeliveryNotes build() {
		DeliveryNotes dn = new DeliveryNotes();
		super.build(dn);
		dn.setShippedAddress(shippedAddress);
		dn.setTrackNumber(trackNumber);
		dn.setPackageNumber(packageNumber);
		dn.setShippingCosts(shippingCosts);
		return dn;
	}
	
	public DeliveryNotesBuilder setShippedAddress(Address shippedAddress) {
		this.shippedAddress = shippedAddress;
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
