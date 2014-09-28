package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;

public class PurchaseAgreementServiceTest {

    private PurchaseAgreementService purchaseAgreementService = new PurchaseAgreementService();
    
    @Test
    public void contradictoryPurchaseAgreementsShouldBeDetected() {
        //GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemOtherWith(givenPurchaseAgreementOther()));
        
        // WHEN / THEN
        assertThat(purchaseAgreementService.hasEqualPurchaseAgreements(agreementItems), is(false));
    }
    
    @Test
    public void purchaseAgreementsWithDifferingTimeShouldBeEqual() {
        //GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemOtherWith(givenPurchaseAgreement()));
        
        // WHEN / THEN
        assertThat(purchaseAgreementService.hasEqualPurchaseAgreements(agreementItems), is(true));
    }
    
    private ReportItem givenAgreementItemWith(PurchaseAgreement purchaseAgreement) {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(purchaseAgreement)
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreementOther() {
        PurchaseAgreement ad = new PurchaseAgreement();
        ad.setExpectedDelivery(new LocalDate().plusDays(10));
        ad.setCustomerNumber(123L);
        return ad;
    }

    private ReportItem givenAgreementItemOtherWith(PurchaseAgreement pa) {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(pa)
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setExpectedDelivery(new LocalDate());
        pa.setCustomerNumber(123L);
        return pa;
    }
    
}
