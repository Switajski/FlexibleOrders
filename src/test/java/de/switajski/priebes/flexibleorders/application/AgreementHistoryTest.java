package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;

public class AgreementHistoryTest {

    @Test(expected = IllegalStateException.class)
    public void retrieveOnePurchaseAgreementOrFail_shouldThrowExceptionOnContradictingPurchaseAgreements() {
        // GIVEN
        AgreementHistory agreementHistory = new AgreementHistory(
                Arrays.asList(
                        givenAgreementItem(),
                        givenAgreementItemWithOtherPurchaseAgreement()));

        // WHEN
        agreementHistory.retrieveOnePurchaseAgreementOrFail();

        // THEN expect Exception
    }
    
    @Test
    public void retrieveOnePurchaseAgreementOrFail_shouldReturnOneIfPurchaseAgreementsAreEqual() {
        // GIVEN
        AgreementHistory agreementHistory = new AgreementHistory(
                Arrays.asList(
                        givenAgreementItem(),
                        givenAgreementItem()));

        // WHEN
        PurchaseAgreement purchaseAgreement = agreementHistory.retrieveOnePurchaseAgreementOrFail();

        // THEN 
        assertThat(purchaseAgreement, is(not(nullValue())));
    }
    
    private AgreementItem givenAgreementItem() {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(givenPurchaseAgreement())
                                .build())
                .build();
    }

    private PurchaseAgreement givenOtherPurchaseAgreement() {
        PurchaseAgreement ad = new PurchaseAgreement();
        ad.setExpectedDelivery(new DateTime(new Date()).plusDays(10).toDate());
        ad.setCustomerNumber(123L);
        return ad;
    }

    private AgreementItem givenAgreementItemWithOtherPurchaseAgreement() {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(givenOtherPurchaseAgreement())
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setExpectedDelivery(new Date());
        pa.setCustomerNumber(123L);
        return pa;
    }

}
