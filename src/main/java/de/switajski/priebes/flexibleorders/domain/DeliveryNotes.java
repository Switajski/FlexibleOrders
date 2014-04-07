package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;

@Entity
public class DeliveryNotes extends Report{
	
	@AttributeOverrides({
	    @AttributeOverride(name="name1",column=@Column(name="shipped_name1")),
	    @AttributeOverride(name="name2",column=@Column(name="shipped_name2")),
	    @AttributeOverride(name="street",column=@Column(name="shipped_street")),
	    @AttributeOverride(name="postalCode",column=@Column(name="shipped_postal_code")),
	    @AttributeOverride(name="city",column=@Column(name="shipped_city")),
	    @AttributeOverride(name="country",column=@Column(name="shipped_country"))
	  })
	private Address shippedAddress;
	
	/**
	 * Date on which due date is calculated.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date evaluationDate;
	
	@Embedded
	private ShippedSpecification shippedSpecification;

	private String trackNumber;
	
	private String packageNumber;

	private Amount shippingCosts;
	
	protected DeliveryNotes(){};
	
	public DeliveryNotes(String deliveryNotesNumber, ShippedSpecification shippedSpec,
			Address shippedAdress, Amount shippingCosts) {
		super(deliveryNotesNumber);
		this.shippedSpecification = shippedSpec;
		this.shippedAddress = shippedAdress;
		this.shippingCosts = shippingCosts;
	}
	
	public ShippedSpecification getShippedSpecification() {
		return shippedSpecification;
	}

	public void setShippedSpecification(ShippedSpecification shippedSpecification) {
		this.shippedSpecification = shippedSpecification;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	public Amount getNetAmount() {
		Amount summed = new Amount();
		for (ReportItem he: this.getItems())
			summed.add(he.getOrderItem().getNegotiatedPriceNet());
		return summed;
	}

	public Address getShippedAddress() {
		return shippedAddress;
	}

	public void setShippedAddress(Address shippedAddress) {
		this.shippedAddress = shippedAddress;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public Amount getShippingCosts() {
		return shippingCosts;
	}

	public void setShippingCosts(Amount shippingCosts) {
		this.shippingCosts = shippingCosts;
	}

}
