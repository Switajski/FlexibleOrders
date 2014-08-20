package de.switajski.priebes.flexibleorders.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.switajski.priebes.flexibleorders.application.process.WholesaleProcessSteps;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.CreditNoteItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

//TODO use on read only objects
public class DeliveryHistory {

    private Collection<ReportItem> reportItems;

    public static DeliveryHistory createWholeFrom(Report report) {
        Set<ReportItem> ris = new HashSet<ReportItem>();
        if (!report.getItems().isEmpty()) {
            for (ReportItem ri : report.getItems()) {
                ris.addAll(ri.getOrderItem().getReportItems());
            }
        }
        return new DeliveryHistory(ris);
    }
    
    public static DeliveryHistory createFrom(OrderItem orderItem) {
        return new DeliveryHistory(orderItem.getReportItems());
    }

    public static DeliveryHistory createFrom(ReportItem reportItem) {
        return new DeliveryHistory(reportItem.getOrderItem().getReportItems());
    }

    public DeliveryHistory(Collection<ReportItem> reportItems) {
        this.reportItems = Collections.unmodifiableCollection(reportItems);
    }

    public <T extends ReportItem> Set<T> getItems(Class<T> type) {
        Set<T> riToReturn = new HashSet<T>();
        for (ReportItem ri : reportItems) {
            if (type.isInstance(ri)) riToReturn.add(type.cast(ri));
        }
        return riToReturn;
    }

    public Set<ReportItem> getItems() {
        return new HashSet<ReportItem>(reportItems);
    }

    public List<ReportItem> getItemsSorted() {
        List<ReportItem> ris = new ArrayList<ReportItem>(getItems());
        Collections.sort(ris, new Comparator<ReportItem>() {
            @Override
            public int compare(ReportItem o1, ReportItem o2) {
                return o1.getCreated().compareTo(o2.getCreated());
            }
        });
        return ris;
    }

    public boolean isEmpty() {
        return reportItems.isEmpty();
    }

    public ShippingItem getShippingItemOf(InvoiceItem ii) {
        Set<ShippingItem> sis = this.getItems(ShippingItem.class);
        if (sis.size() > 1) throw new IllegalStateException("Mehr als eine zutreffende Lieferscheinposition gefunden");
        else return sis.iterator().next();
    }

    public Set<String> getOrderNumbers() {
        Set<String> orders = new HashSet<String>();
        for (ReportItem ri : this.reportItems)
            orders.add(ri.getOrderItem().getOrder().getOrderNumber());
        return orders;

    }
    
    public Set<String> getConfirmationReportNumbers() {
        return getNumbers(ConfirmationItem.class);
    }

    public Set<String> getDeliveryNotesNumbers() {
        return getNumbers(ShippingItem.class);
    }
    
    public Set<String> getInvoiceNumbers() {
        return getNumbers(InvoiceItem.class);
    }
    
    public Set<String> getOrderAgreementNumbers(){
        return getNumbers(AgreementItem.class);
    }
    
    public Collection<String> getCreditNoteNumbers() {
        return getNumbers(CreditNoteItem.class);
    }
    
    private <T extends ReportItem> Set<String> getNumbers(Class<? extends ReportItem> clazz) {
        Set<String> nos = new HashSet<String>();
        for (ReportItem ci : getItems(clazz)) {
            nos.add(ci.getReport().getDocumentNumber());
        }
        return nos;
    }


    /**
     * Differs the order's expected delivery date from order confirmation(s)?
     * 
     * @return null if dates are not set.
     */
    public Boolean hasDifferentDeliveryDates() {
        Days tolerance = Days.ONE;

        Set<Date> expectedDates = new HashSet<Date>();
        Set<Date> confirmedDates = new HashSet<Date>();
        for (ReportItem ri : getItems())
            if (ri.getOrderItem().getOrder().getAgreementDetails() != null) expectedDates.add(ri.getOrderItem().getOrder().getAgreementDetails().getExpectedDelivery());

        for (ConfirmationItem ci : getItems(ConfirmationItem.class))
            if (ci.getOrderItem().getOrder().getAgreementDetails() != null) confirmedDates.add(((OrderConfirmation) ci.getReport()).getAgreementDetails().getExpectedDelivery());

        Boolean hasDifferentDeliveryDates = null;
        if (!expectedDates.isEmpty() || !confirmedDates.isEmpty()) {
            hasDifferentDeliveryDates = false;
            for (Date ed : expectedDates) {
                for (Date cd : confirmedDates) {
                    if (Days.daysBetween(new DateTime(ed), new DateTime(cd)).isGreaterThan(tolerance)) hasDifferentDeliveryDates = true;
                }
            }
        }

        return hasDifferentDeliveryDates;
    }
    
    public String provideStatus() {
        String s = "TODO";
        return s;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        OrderItem orderItem = reportItems.iterator().next().getOrderItem();
        s.append("Orderno: " + orderItem.getOrder().getOrderNumber());
        s.append(", " + orderItem.getOrderedQuantity() + " x ");
        s.append(orderItem.getProduct().getProductNumber() + " "
                + orderItem.getProduct().getName());
        s.append("\n-------------------------------------");
        for (Class<? extends ReportItem> type : WholesaleProcessSteps.reportItemSteps()) {
            s.append("\n" + type.getSimpleName() + "s: ");
            for (ReportItem item : getItems(type)) {
                s.append("\n     " + item);
            }
        }
        return s.toString();
    }

}
