package de.switajski.priebes.flexibleorders.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DateUtils;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@Service
public class ExpectedDeliveryService {

    @Autowired
    PurchaseAgreementReadService purchaseAgreementService;

    public Set<LocalDate> retrieve(Set<ReportItem> items) {
        Set<LocalDate> expectedDeliveryDates = new HashSet<LocalDate>();
        for (PurchaseAgreement pa : purchaseAgreementService.withoutDeviations(items)) {
            expectedDeliveryDates.add(pa.getExpectedDelivery());
        }
        return expectedDeliveryDates;
    }

    public Set<Integer> retrieveWeekOfYear(Set<ReportItem> items) {
        return purchaseAgreementService.withoutDeviations(items)
                .stream()
                .filter(p -> p.getExpectedDelivery() != null)
                .map(s -> DateUtils.weekOf(s.getExpectedDelivery()))
                .collect(Collectors.toSet());
    }

    /**
     * 
     * @param reportItems
     * @param deliveryNotes
     * @throws ContradictoryPurchaseAgreementException
     *             if given reportItems have contradictory expected delivery
     *             dates
     */
    @Transactional(readOnly = true)
    public void validateExpectedDeliveryDates(
            Set<ReportItem> reportItems,
            Date actualDeliveryDate) throws ContradictoryPurchaseAgreementException {
        Set<Integer> expectedDeliveryDates = retrieveWeekOfYear(reportItems);
        if (expectedDeliveryDates.size() > 1) {
            StringBuilder messageBuilder = new StringBuilder("Angegebene Positionen haben ABs mit widerspr" + Unicode.U_UML + "chlichen Lieferdaten: ");
            Iterator<Integer> lItr = expectedDeliveryDates.iterator();
            while (lItr.hasNext()) {
                Integer kw = lItr.next();
                messageBuilder.append("KW ").append(kw);
                if (lItr.hasNext()) {
                    messageBuilder.append(", ");
                }
            }
            throw new ContradictoryPurchaseAgreementException(messageBuilder.toString());
        }
        else if (expectedDeliveryDates.size() == 1) {
            int expectedWeek = expectedDeliveryDates.iterator().next();
            int isWeek = DateUtils.weekOf(actualDeliveryDate);
            if (expectedWeek != isWeek) {
                throw new ContradictoryPurchaseAgreementException("Widerr" + Unicode.U_UML + "chliche Liefertermine: KW aus AB ist " + expectedWeek
                        + ", Datum des Lieferscheins liegt aber in KW " + isWeek);
            }
        }
    }

}
