package de.switajski.priebes.flexibleorders.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

	@JsonIgnore
	public Set<ReportItem> getItems() {
		return items;
	}
	
	@JsonIgnore
	public List<ReportItem> getItemsOrdered() {
		List<ReportItem> ris = new ArrayList<ReportItem>(getItems());
		Collections.sort(ris, new Comparator<ReportItem>(){

			@Override
			public int compare(ReportItem r1, ReportItem r2) {
				Product p1 = r1.getOrderItem().getProduct();
				Product p2 = r2.getOrderItem().getProduct();
				if (p1.hasProductNo() && p2.hasProductNo())
					return p1.getProductNumber().compareTo(p2.getProductNumber());
				else if (!p1.hasProductNo() && !p2.hasProductNo()){
					return p1.getName().compareTo(p2.getName());
				} else if (p1.hasProductNo()){
					return 1;
				}
				else if (p2.hasProductNo()){
					return -1;
				}
				
				else return 0;
				
			}
			
		});
		return ris;
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
