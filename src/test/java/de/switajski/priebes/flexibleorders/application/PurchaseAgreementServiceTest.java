package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;

public class PurchaseAgreementServiceTest {

    private PurchaseAgreementService purchaseAgreementService = new PurchaseAgreementService();

    @Test
    public void shouldRetrieveContradictoryPurchaseAgreementsFromSimilarItems() {
        // GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemWith(changeExpectedDeliveryDate(givenPurchaseAgreement())));

        // WHEN 
        Set<PurchaseAgreement> purchaseAgreements = purchaseAgreementService.retrieve(agreementItems);
        
        // THEN
        assertThat(purchaseAgreements.size(), is(greaterThan(1)));
    }
    
    @Test
    public void shouldLegalPurchaseAgreementsOnly() {
        // GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemWith(changeExpectedDeliveryDate(givenPurchaseAgreement())));

        // WHEN 
        Set<PurchaseAgreement> purchaseAgreements = purchaseAgreementService.retrieveLegal(agreementItems);
        
        // THEN
        assertThat(purchaseAgreements.size(), is(0));
    }

    private ReportItem givenAgreementItemWith(PurchaseAgreement purchaseAgreement) {
        return new ConfirmationItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderConfirmationBuilder()
                                .setAgreementDetails(purchaseAgreement)
                                .build())
                .build();
    }

    private PurchaseAgreement changeExpectedDeliveryDate(PurchaseAgreement ad) {
        ad.setExpectedDelivery(ad.getExpectedDelivery().plusDays(10));
        return ad;
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setExpectedDelivery(new LocalDate());
        pa.setCustomerNumber(123L);
        return pa;
    }

}
