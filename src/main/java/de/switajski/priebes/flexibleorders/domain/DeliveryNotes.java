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
	
	protected DeliveryNotes(){};
	
	public DeliveryNotes(String invoiceNumber, ShippedSpecification shippedSpec,
			Address shippedAdress) {
		super(invoiceNumber);
		this.shippedSpecification = shippedSpec;
		this.shippedAddress = shippedAdress;
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
		for (HandlingEvent he: this.getEvents())
			summed.add(he.getOrderItem().getNegotiatedPriceNet());
		return summed;
	}

	public Address getShippedAddress() {
		return shippedAddress;
	}

	public void setShippedAddress(Address shippedAddress) {
		this.shippedAddress = shippedAddress;
	}

}
