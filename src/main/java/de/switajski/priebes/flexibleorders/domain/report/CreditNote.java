package de.switajski.priebes.flexibleorders.domain.report;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class CreditNote extends Report {

	@JsonIgnore
	public Set<AgreementItem> getAgreementItems(){
		Set<AgreementItem> shippingItems = new HashSet<AgreementItem>();
		for (ReportItem reportItem: this.items){
			shippingItems.add((AgreementItem) reportItem);
		}
		return shippingItems;
	}
}
