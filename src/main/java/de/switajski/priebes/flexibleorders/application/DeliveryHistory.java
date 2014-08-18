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
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

//TODO use on read only objects
public class DeliveryHistory {

    private Collection<ReportItem> reportItems;

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

    public String getOrderNumber() {
        return this.reportItems
                .iterator()
                .next()
                .getOrderItem()
                .getOrder()
                .getOrderNumber();
    }

    public ShippingItem getShippingItemOf(InvoiceItem ii) {
        Set<ShippingItem> sis = this.getItems(ShippingItem.class);
        if (sis.size() > 1) throw new IllegalStateException("Mehr als eine zutreffende Lieferscheinposition gefunden");
        else return sis.iterator().next();
    }

    public String getConfirmationReportNumbers() {
        String s = "";
        Set<String> nos = new HashSet<String>();
        for (ConfirmationItem ci : getItems(ConfirmationItem.class)) {
            nos.add(ci.getReport().getDocumentNumber());
        }
        for (String no : nos) {
            s += no + " ";
        }
        return s;
    }

    public String getDeliveryNotesNumbers() {
        String s = "";
        Set<String> nos = new HashSet<String>();
        for (ShippingItem ci : getItems(ShippingItem.class)) {
            nos.add(ci.getReport().getDocumentNumber());
        }
        for (String no : nos) {
            s += no + " ";
        }
        return s;
    }

    public Set<String> getOrderNumbers() {
        Set<String> orders = new HashSet<String>();
        for (ReportItem ri : this.reportItems)
            orders.add(ri.getOrderItem().getOrder().getOrderNumber());
        return orders;

    }

    public String provideStatus() {
        String s = "TODO";
        return s;
    }

    public static DeliveryHistory createWholeFrom(Report report) {
        Set<ReportItem> ris = new HashSet<ReportItem>();
        if (!report.getItems().isEmpty()) {
            for (ReportItem ri : report.getItems()) {
                ris.addAll(ri.getOrderItem().getReportItems());
            }
        }
        return new DeliveryHistory(ris);
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

    public String toString() {
        String s = "";
        OrderItem orderItem = reportItems.iterator().next().getOrderItem();
        s += "Orderno: " + orderItem.getOrder().getOrderNumber();
        s += ", " + orderItem.getOrderedQuantity() + " x ";
        s += +orderItem.getProduct().getProductNumber() + " "
                + orderItem.getProduct().getName();
        s += "\n-------------------------------------";
        for (Class<? extends ReportItem> type : WholesaleProcessSteps.reportItemSteps()) {
            s += "\n" + type.getSimpleName() + "s: ";
            for (ReportItem item : getItems(type)) {
                s += "\n     " + item;
            }
        }
        return s;
    }

}
