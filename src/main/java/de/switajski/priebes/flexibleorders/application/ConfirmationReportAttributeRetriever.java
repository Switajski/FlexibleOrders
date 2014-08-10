package de.switajski.priebes.flexibleorders.application;

import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

public abstract class ConfirmationReportAttributeRetriever<T> {

	DeliveryHistory history;
	
	public ConfirmationReportAttributeRetriever(DeliveryHistory history) {
		this.history = history;
	}
	
	public T getInvoiceAddress() {
		Set<ConfirmationItem> sis = history.getItems(ConfirmationItem.class);
		T attribute = null;
		for (ConfirmationItem si:sis){
			T a = retrieveAttribute(si);
			if (attribute == null)
				attribute = a;
			else if (!a.equals(attribute))
				throw new IllegalStateException("Widersprechende Daten aus Auftragsbest"+Unicode.aUml+"tigung gefunden");
		}
		return attribute;
	}

	public abstract T retrieveAttribute(ConfirmationItem si);
	
}
