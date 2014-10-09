package de.switajski.priebes.flexibleorders.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.switajski.priebes.flexibleorders.application.process.WholesaleProcessSteps;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
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

    public DeliveryHistory(Collection<ReportItem> reportItems) {
        this.reportItems = Collections.unmodifiableCollection(reportItems);
    }

    public static DeliveryHistory of(Report report) {
        Set<ReportItem> ris = new HashSet<ReportItem>();
        if (!report.getItems().isEmpty()) {
            for (ReportItem ri : report.getItems()) {
                ris.addAll(ri.getOrderItem().getReportItems());
            }
        }
        return new DeliveryHistory(ris);
    }

    public static DeliveryHistory of(ReportItem reportItem) {
        return new DeliveryHistory(reportItem.getOrderItem().getReportItems());
    }

    public static DeliveryHistory of(OrderItem orderItem) {
        return new DeliveryHistory(orderItem.getReportItems());
    }

    public Set<ConfirmationItem> getAgreedConfirmationItems() {
        Set<ConfirmationItem> cis = new HashSet<ConfirmationItem>();
        for (ConfirmationItem ci : getReportItems(ConfirmationItem.class))
            if (ci.isAgreed()) cis.add(ci);
        return cis;
    }
    
    public Set<ConfirmationItem> getNonAgreedConfirmationItems() {
        Set<ConfirmationItem> cis = new HashSet<ConfirmationItem>();
        for (ConfirmationItem ci : getReportItems(ConfirmationItem.class))
            if (!ci.isAgreed()) cis.add(ci);
        return cis;
    }

    public <T extends ReportItem> Set<T> getReportItems(Class<T> type) {
        Set<T> riToReturn = new HashSet<T>();
        for (ReportItem ri : reportItems) {
            if (type.isInstance(ri)) riToReturn.add(type.cast(ri));
        }
        return riToReturn;
    }

    public <T extends Report> Set<T> getReports(Class<T> clazz) {
        Set<T> reports = new HashSet<T>();
        for (ReportItem ri : reportItems) {
            if (clazz.isInstance(ri.getReport())) reports.add(clazz.cast(ri.getReport()));
        }
        return reports;
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
        Set<ShippingItem> sis = this.getReportItems(ShippingItem.class);
        if (sis.size() > 1) throw new IllegalStateException("Mehr als eine zutreffende Lieferscheinposition gefunden");
        else return sis.iterator().next();
    }

    public Set<String> getOrderNumbers() {
        Set<String> orders = new HashSet<String>();
        for (ReportItem ri : this.reportItems)
            orders.add(ri.getOrderItem().getOrder().getOrderNumber());
        return orders;

    }

    public Set<String> getOrderConfirmationNumbers() {
        return getReportNumbers(ConfirmationItem.class);
    }

    public Set<String> getDeliveryNotesNumbers() {
        return getReportNumbers(ShippingItem.class);
    }

    public Set<String> getInvoiceNumbers() {
        return getReportNumbers(InvoiceItem.class);
    }

    public Set<String> getOrderAgreementNumbers() {
        Set<String> nos = new HashSet<String>();
        for (ReportItem ci : getReportItems(ConfirmationItem.class)) {
            ConfirmationItem cis = (ConfirmationItem) ci;
            OrderConfirmation oc = (OrderConfirmation) ci.getReport();
            if (oc.isAgreed()) nos.add(oc.getOrderAgreementNumber());
        }
        return nos;
    }

    public Set<String> getCreditNoteNumbers() {
        return getReportNumbers(CreditNoteItem.class);
    }

    public <T extends ReportItem> Set<String> getReportNumbers(Class<? extends ReportItem> clazz) {
        Set<String> nos = new HashSet<String>();
        for (ReportItem ci : getReportItems(clazz)) {
            nos.add(ci.getReport().getDocumentNumber());
        }
        return nos;
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
            for (ReportItem item : getReportItems(type)) {
                if (item instanceof ConfirmationItem){
                    if (((ConfirmationItem) item).isAgreed()){
                        s.append(" - agreed - ");
                    } else{
                        s.append(" - not agreed - ");
                    }
                }
                s.append("\n     " + item);
            }
        }
        return s.toString();
    }

}
