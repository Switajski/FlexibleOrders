package de.switajski.priebes.flexibleorders.web.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ReportDto {

	public String documentNumber;

	public Set<ReportItem> items = new HashSet<ReportItem>();

	public Long customerNumber;

	public CustomerDetails customerDetails;

	public PurchaseAgreement purchaseAgreement;

	public Date created;
	
	public LocalDate expectedDelivery;
	

	// shipping specific
	public Address shippedAddress;

	public String trackNumber;

	public String packageNumber;

	public Amount shippingCosts;

	public DeliveryHistory deliveryHistory;

	
	// invoice specific
	public boolean hasItemsWithDifferentCreationDates;

	public Address invoiceAddress;
	
	public Amount netGoods, vatRate;

	public String paymentConditions;

	public String billing;
	
	@JsonIgnore
	public Collection<ReportItem> getItemsByOrder() {
		List<ReportItem> ris = new ArrayList<ReportItem>(items);
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
		return Collections.unmodifiableCollection(ris);
	}


}
