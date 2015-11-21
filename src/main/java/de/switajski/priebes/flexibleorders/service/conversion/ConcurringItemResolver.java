package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ConcurringItemResolver {

    private Map<ReportItem, ReportItem> shippingItemsToInvoiceItems;

    public ConcurringItemResolver(Set<ReportItem> matchingInvoiceItems, Set<ReportItem> concurringShippingItems) {
        if (concurringShippingItems.size() < matchingInvoiceItems.size()) {
            throw new IllegalArgumentException("left items size should be larger than right");
        }
        shippingItemsToInvoiceItems = new HashMap<ReportItem, ReportItem>();

        List<ReportItem> invoiceItems = createListAndSort(matchingInvoiceItems);
        List<ReportItem> shippingItems = createListAndSort(concurringShippingItems);

        for (int i = 0; i < shippingItems.size(); i++) {
            ReportItem invoiceItem = null;
            if (i < invoiceItems.size()) {
                invoiceItem = invoiceItems.get(i);
            }
            shippingItemsToInvoiceItems.put(shippingItems.get(i), invoiceItem);
        }

    }

    private List<ReportItem> createListAndSort(Set<ReportItem> matchingInvoiceItems) {
        List<ReportItem> amatchingInvoiceItems = new ArrayList<ReportItem>();
        amatchingInvoiceItems.addAll(matchingInvoiceItems);
        Collections.sort(amatchingInvoiceItems);
        return amatchingInvoiceItems;
    }

    public ReportItem resolve(ReportItem shippingItem) {
        return shippingItemsToInvoiceItems.get(shippingItem);
    }

}
