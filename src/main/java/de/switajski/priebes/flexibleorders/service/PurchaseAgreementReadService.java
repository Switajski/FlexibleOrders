package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

/**
 * A purchase agreement can also come from external services like SalesForce.
 * Therefore this class should never return {@link OrderConfirmation}s!
 * 
 * @author switajski
 *
 */
@Service
public class PurchaseAgreementReadService {
    @Autowired
    ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> withoutDeviations(Collection<ReportItem> reportItems) {
        boolean agreedOnly = false;
        return retrieveWithoutDeviations(reportItems, agreedOnly).stream()
                .map(p -> p.getPurchaseAgreement())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Map<String, PurchaseAgreement> idToPurchaseAgreement(Collection<ReportItem> reportItems) {
        boolean agreedOnly = false;
        Map<String, PurchaseAgreement> idToPurchaseAgreement = new HashMap<>();
        for (OrderConfirmation ri : retrieveWithoutDeviations(reportItems, agreedOnly)) {
            idToPurchaseAgreement.put(ri.getDocumentNumber(), ri.getPurchaseAgreement());
        }
        return idToPurchaseAgreement;
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
        Collection<PurchaseAgreement> actual = withDeviation(reportItems);
        return actual
                .stream()
                .map(s -> s.getShippingAddress())
                .collect(Collectors.toSet());
    }

    /**
     * PurchaseAgreements can be modified after they have been created: If the
     * user wants to ship items from purchase agreements with deviating shipping
     * addresses, he has the possibility to change the address using
     * {@link PurchaseAgreementWriteService#changeShippingAddress}.
     * 
     * @param reportItems
     * @return
     */
    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> withDeviation(Collection<ReportItem> reportItems) {
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

    @Transactional(readOnly = true)
    public Set<Address> invoiceAddresses(Collection<ReportItem> reportItems) {
        return withDeviation(reportItems)
                .stream()
                .map(s -> s.getInvoiceAddress())
                .collect(Collectors.toSet());
    }

    /**
     * 
     * @param reportItems
     * @return
     * @throws ContradictoryAddressException
     */
    @Transactional(readOnly = true)
    public Address retrieveShippingAddressOrFail(Set<ReportItem> reportItems) throws ContradictoryAddressException {
        Set<Address> ias = shippingAddresses(reportItems);
        if (ias.size() > 1) {
            throw new ContradictoryAddressException(
                    "Verschiedene Lieferadressen in Auftr" + Unicode.A_UML + "gen gefunden:<br />"
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
    private Set<OrderConfirmation> retrieveWithoutDeviations(
            Collection<ReportItem> reportItems,
            boolean agreedOnly) {

        Set<ReportItem> allRis = new HashSet<ReportItem>();
        for (ReportItem reportItem : reportItems) {
            allRis.add(reportItem);
            allRis.addAll(reportItem.predecessors());
        }

        Set<OrderConfirmation> pas = new HashSet<OrderConfirmation>();
        for (ReportItem reportItem : allRis) {
            if (reportItem instanceof ConfirmationItem) {
                OrderConfirmation orderConfirmation = (OrderConfirmation) reportItem.getReport();
                if (agreedOnly && !orderConfirmation.isAgreed()) continue;
                pas.add(orderConfirmation);
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
