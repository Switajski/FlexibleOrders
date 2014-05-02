package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Report extends GenericEntity {

	@Transient
	private static final double VAT_RATE = Order.VAT_RATE;

	/**
	 * Natural id of a Report.
	 */
	@NotNull
	@Column(unique = true)
	private String documentNumber;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	protected Set<ReportItem> items = new HashSet<ReportItem>();

	private Long customerNumber;

	protected Report() {
	}

	public Report(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public Set<ReportItem> getItems() {
		return items;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public void addItem(ReportItem item) {
		if (items.contains(item))
			return;
		items.add(item);
		item.setReport(this);
	}

	public Long getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
	}

	public void removeItem(ReportItem item) {
		this.items.remove(item);
	}

	public double getVatRate() {
		return Order.VAT_RATE;
	}

}
