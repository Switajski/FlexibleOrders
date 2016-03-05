package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;

public class PurchaseAgreementServiceTest {

    private PurchaseAgreementReadService purchaseAgreementService = new PurchaseAgreementReadService();

    @Test
    public void shouldRetrieveContradictoryPurchaseAgreementsFromSimilarItems() {
        // GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemWith(changeExpectedDeliveryDate(givenPurchaseAgreement())));

        // WHEN
        Set<PurchaseAgreement> purchaseAgreements = whenRetrievingPurchaseAgreements(agreementItems);

        // THEN
        assertThat(purchaseAgreements.size(), is(greaterThan(1)));
    }

    private Set<PurchaseAgreement> whenRetrievingPurchaseAgreements(List<ReportItem> agreementItems) {
        Set<PurchaseAgreement> purchaseAgreements = purchaseAgreementService.withoutDeviations(agreementItems);
        return purchaseAgreements;
    }

    @Test
    public void shouldRetrieveTwoShippingAddress() {
        // GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemWith(changeShippingAddressDate(givenPurchaseAgreement())));

        Set<Address> shippingAddresses = purchaseAgreementService.shippingAddressesWithoutDeviations(agreementItems);

        assertThat(shippingAddresses.size(), is(2));
    }

    @Test
    public void shouldRetrieveOneShippingAddress() {
        // GIVEN
        List<ReportItem> agreementItems = Arrays.asList(
                givenAgreementItemWith(givenPurchaseAgreement()),
                givenAgreementItemWith(givenPurchaseAgreement()));

        Set<Address> shippingAddresses = purchaseAgreementService.shippingAddressesWithoutDeviations(agreementItems);

        assertThat(shippingAddresses.size(), is(1));
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

    private PurchaseAgreement changeShippingAddressDate(PurchaseAgreement ad) {
        ad.setShippingAddress(AddressBuilder.buildWithGeneratedAttributes(2));
        return ad;
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setExpectedDelivery(LocalDate.now());
        pa.setCustomerNumber(123L);
        pa.setShippingAddress(AddressBuilder.buildWithGeneratedAttributes(1));
        return pa;
    }

}
