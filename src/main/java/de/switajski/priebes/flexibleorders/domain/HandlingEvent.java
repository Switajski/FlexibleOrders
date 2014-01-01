package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.PayedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Entity
public class HandlingEvent extends GenericEntity{

	private int quantity;
	
	@NotNull
	@ManyToOne
	private Document report;
	
	@NotNull
	private HandlingEventType type;
	
	@Embedded
	private ShippedSpecification shippedSpec;
	
	@Embedded
	private ConfirmedSpecification confirmedSpec;
	
	@Embedded
	private PayedSpecification payedSpec;
	
	@ManyToOne
	private DeliveryHistory history;

	public HandlingEvent() {}
	
	public HandlingEvent(HandlingEventType type, DeliveryHistory history, int quantity, Date created) {
		this.type = type;
		this.history = history;
		this.quantity = quantity;
		setCreated(created);
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Document getReport() {
		return report;
	}

	public void setReport(Document report) {
		this.report = report;
	}

	public DeliveryHistory getDeliveryHistory() {
		return history;
	}

	public void setDeliverHistory(DeliveryHistory history) {
		this.history = history;
	}

	public HandlingEventType getType() {
		return type;
	}

	public void setType(HandlingEventType type) {
		this.type = type;
	}

	public ShippedSpecification getShippedSpec() {
		return shippedSpec;
	}

	public void setShippedSpec(ShippedSpecification shippedSpec) {
		this.shippedSpec = shippedSpec;
	}

	public ConfirmedSpecification getConfirmedSpec() {
		return confirmedSpec;
	}

	public void setConfirmedSpec(ConfirmedSpecification confirmedSpec) {
		this.confirmedSpec = confirmedSpec;
	}

	public PayedSpecification getPayedSpec() {
		return payedSpec;
	}

	public void setPayedSpec(PayedSpecification payedSpec) {
		this.payedSpec = payedSpec;
	}
	
	
}
