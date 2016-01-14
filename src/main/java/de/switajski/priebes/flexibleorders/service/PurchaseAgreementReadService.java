package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@Service
public class PurchaseAgreementReadService {

    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> withoutDeviations(Collection<ReportItem> reportItems) {
        boolean agreedOnly = false;
        return retrieveWithoutDeviations(reportItems, agreedOnly);
    }

    /**
     * retrieve only purchase agreements that are agreed by an order agreement
     * 
     * @param reportItems
     *            works with confirmation items only.
     * @return
     */
    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> retrieveLegalWithoutDeviations(
            Collection<ReportItem> reportItems) {
        boolean agreedOnly = true;
        return retrieveWithoutDeviations(reportItems, agreedOnly);
    }

    @Transactional(readOnly = true)
    public Set<Address> shippingAddressesWithoutDeviations(Collection<ReportItem> reportItems) {
        return withoutDeviations(reportItems)
                .stream()
                .map(s -> s.getShippingAddress())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<Address> shippingAddresses(Collection<ReportItem> reportItems) {
        Collection<PurchaseAgreement> actual = actual(reportItems);
        return actual
                .stream()
                .map(s -> s.getShippingAddress())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Collection<PurchaseAgreement> actual(Collection<ReportItem> reportItems) {
        Set<ReportItem> withPredecessors = new HashSet<ReportItem>();
        for (ReportItem ri : reportItems) {
            withPredecessors.addAll(ri.predecessors());
            withPredecessors.add(ri);
        }
        Set<OrderConfirmation> ocs = withPredecessors.stream()
                .map(ri -> ri.getReport())
                .filter(r -> r instanceof OrderConfirmation)
                .map(OrderConfirmation.class::cast)
                .collect(Collectors.toSet());
        return ocs.stream()
                .filter(oc -> oc.isAgreed())
                .map(oc -> oc.actualPurchaseAgreement())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<Address> invoiceAddressesWithoutDeviation(Collection<ReportItem> reportItems) {
        return withoutDeviations(reportItems)
                .stream()
                .map(s -> s.getInvoiceAddress())
                .collect(Collectors.toSet());
    }

    /**
     * 
     * @param reportItems
     * @return
     */
    @Transactional(readOnly = true)
    public Address retrieveShippingAddressOrFail(Set<ReportItem> reportItems) {
        Set<Address> ias = shippingAddresses(reportItems);
        if (ias.size() > 1) {
            throw new ContradictoryPurchaseAgreementException(
                    "Verschiedene Lieferadressen in Auftr" + Unicode.A_UML + "gen gefunden: #CPA-DA<br />"
                            + BeanUtil.createStringOfDifferingAttributes(ias));
        }
        else if (ias.size() == 0) throw new NotFoundException("Keine Lieferaddresse aus Kaufvertr" + Unicode.A_UML + "gen gefunden");
        Address shippingAddress = ias.iterator().next();
        return shippingAddress;
    }

    /**
     * 
     * @param reportItems
     * @param agreedOnly
     * @return
     */
    private Set<PurchaseAgreement> retrieveWithoutDeviations(
            Collection<ReportItem> reportItems,
            boolean agreedOnly) {

        Set<ReportItem> allRis = new HashSet<ReportItem>();
        for (ReportItem reportItem : reportItems) {
            allRis.add(reportItem);
            allRis.addAll(reportItem.predecessors());
        }

        Set<PurchaseAgreement> pas = new HashSet<PurchaseAgreement>();
        for (ReportItem reportItem : allRis) {
            if (reportItem instanceof ConfirmationItem) {
                OrderConfirmation orderConfirmation = (OrderConfirmation) reportItem.getReport();
                if (agreedOnly && !orderConfirmation.isAgreed()) continue;
                pas.add(orderConfirmation.getPurchaseAgreement());
            }
        }

        return pas;
    }

    /**
     * 
     * @param reportItems
     * @return
     * @throws IllegalStateException
     *             if retrieved purchase agreement is not one
     */
    public PurchaseAgreement retrieveSingle(Collection<ReportItem> reportItems) {
        Set<PurchaseAgreement> pas = withoutDeviations(reportItems);
        return extractOneOrFail(pas);
    }

    private PurchaseAgreement extractOneOrFail(Set<PurchaseAgreement> pas) {
        if (pas.isEmpty()) {
            throw new IllegalStateException("No purchase agreement found");
        }
        else if (pas.size() == 1) {
            return pas.iterator().next();
        }
        // FIXME: #104 workaround
        else if (pas.size() > 1) {
            return pas.iterator().next();
        }
        // END Workaround
        else {
            throw new IllegalStateException("Mehere AB#s vorhanden - kann keinen einzelnen Kaufvertrag ausmachen");
        }
    }

}
