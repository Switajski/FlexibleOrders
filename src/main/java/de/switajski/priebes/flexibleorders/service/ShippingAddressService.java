package de.switajski.priebes.flexibleorders.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@Service
public class ShippingAddressService extends AddressFromPurchaseAgreementRetriever {

    @Override
    Address getAddress(PurchaseAgreement purchaseAgreement) {
        return purchaseAgreement.getShippingAddress();
    }

    /**
     * @return one invoicing Address
     * @throws ContradictoryPurchaseAgreementException
     *             if no single invoicing address could be determined
     */
    @Transactional(readOnly = true)
    public Address retrieveShippingAddressOrFail(Set<ReportItem> reportItems) {
        Set<Address> ias = retrieve(reportItems);
        if (ias.size() > 1) throw new ContradictoryPurchaseAgreementException(
                "Verschiedene Lieferadressen in Auftr" + Unicode.A_UML + "gen gefunden: "
                        + BeanUtil.createStringOfDifferingAttributes(ias));
        else if (ias.size() == 0) throw new NotFoundException("Keine Lieferaddresse aus Kaufvertr" + Unicode.A_UML + "gen gefunden");
        Address shippingAddress = ias.iterator().next();
        return shippingAddress;
    }

}
