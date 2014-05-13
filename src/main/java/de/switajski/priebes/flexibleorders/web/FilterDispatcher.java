package de.switajski.priebes.flexibleorders.web;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.specification.ConfirmationItemToBeShippedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.HasCustomerSpec;
import de.switajski.priebes.flexibleorders.repository.specification.InvoiceItemToBePaidSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ReceiptItemsToBeCompletedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ShippingItemToBeInvoicedSpec;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@Controller
public class FilterDispatcher {

	private static final String CUSTOMER_NO = "customer";

	private static final String STATUS = "status";

	@Autowired
	private CustomerRepository customerRepo;

	public Specifications<ReportItem> dispatchToSpecifications(String filters)
			throws Exception {
		HashMap<String, String> filterMap = JsonSerializationHelper
				.deserializeFiltersJson(filters);
		validate(filterMap);

		Specifications<ReportItem> spec = dispatchStatus(filterMap.get(STATUS));

		if (containsCustomer(filterMap))
			spec = spec.and(new HasCustomerSpec(
					retrieveCustomerSafely(filterMap.get(CUSTOMER_NO))));

		return spec;
	}

	private void validate(HashMap<String, String> filterMap) {
		if (filterMap.get(STATUS) == null)
			throw new IllegalArgumentException("Keinen Statusfilter angegeben");
	}

	private Customer retrieveCustomerSafely(String customerNo) {
		Customer customer = customerRepo.findOne(
				Long.parseLong(customerNo));
		if (customer == null)
			throw new IllegalArgumentException(
					"Kunde mit gegebener Id nicht gefunden");
		return customer;
	}

	private boolean containsCustomer(HashMap<String, String> filterMap) {
		return filterMap != null && filterMap.containsKey(CUSTOMER_NO)
				&& filterMap.get(CUSTOMER_NO) != null;
	}

	private Specifications<ReportItem> dispatchStatus(String stateFilter) {
		Specifications<ReportItem> spec = null;
		if (stateFilter.equals("ordered"))
			throw new IllegalArgumentException(
					"Filter 'ordered' cannot be applied to Reports, but Orders");
		if (stateFilter.equals("ship") || stateFilter.equals("confirmed"))
			spec = where(new ConfirmationItemToBeShippedSpec());
		if (stateFilter.equals("shipped"))
			spec = where(new ShippingItemToBeInvoicedSpec());
		if (stateFilter.equals("invoiced"))
			spec = where(new InvoiceItemToBePaidSpec());
		if (stateFilter.equals("completed"))
			spec = where(new ReceiptItemsToBeCompletedSpec());
		if (spec == null)
			throw new IllegalArgumentException("Status nicht gefunden");
		return where(spec);
	}

}
