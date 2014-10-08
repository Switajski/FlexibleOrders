package de.switajski.priebes.flexibleorders.domain.report;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class CreditNote extends Report {

	@JsonIgnore
	public Set<ConfirmationItem> getAgreementItems(){
		Set<ConfirmationItem> shippingItems = new HashSet<ConfirmationItem>();
		for (ReportItem reportItem: this.items){
			shippingItems.add((ConfirmationItem) reportItem);
		}
		return shippingItems;
	}
}
