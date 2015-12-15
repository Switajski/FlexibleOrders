package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.PurchaseAgreementBuilder;

public class ShippingAddressServiceTest {

    @InjectMocks
    ShippingAddressService shippingAddressService = new ShippingAddressService();

    @Mock
    PurchaseAgreementService paService;

    Set<ReportItem> reportItems;

    Address address;

    private PurchaseAgreement purchaseAgreement1;
    private PurchaseAgreement purchaseAgreement2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        OrderConfirmation orderConfirmation1 = givenConfirmationItem1(1);
        OrderConfirmation orderConfirmation2 = givenConfirmationItem2(2);

        when(paService.retrieve(anySet())).thenReturn(new HashSet<PurchaseAgreement>() {
            {
                add(purchaseAgreement1);
                add(purchaseAgreement2);
            }
        });

        reportItems = new HashSet<ReportItem>() {
            {
                addAll(orderConfirmation1.getItems());
                addAll(orderConfirmation2.getItems());
            }
        };

        whenRetrievingShippingAddress();
    }

    private OrderConfirmation givenConfirmationItem2(int i) {
        purchaseAgreement2 = new PurchaseAgreementBuilder()
                .setShippingAddress(AddressBuilder.buildWithGeneratedAttributes(i)).build();
        OrderConfirmation orderConfirmation2 = new OrderConfirmationBuilder()
                .setAgreementDetails(purchaseAgreement2)
                .addItem(new ConfirmationItemBuilder().build())
                .build();

        return orderConfirmation2;
    }

    private OrderConfirmation givenConfirmationItem1(int i) {
        purchaseAgreement1 = new PurchaseAgreementBuilder()
                .setShippingAddress(AddressBuilder.buildWithGeneratedAttributes(i)).build();
        OrderConfirmation orderConfirmation1 = new OrderConfirmationBuilder()
                .setAgreementDetails(purchaseAgreement1)
                .addItem(new ConfirmationItemBuilder().build())
                .build();

        return orderConfirmation1;
    }

    @Test
    public void shouldFindOneShippingAdresses() {
        OrderConfirmation orderConfirmation1 = givenConfirmationItem1(1);
        OrderConfirmation orderConfirmation2 = givenConfirmationItem2(1);

        when(paService.retrieve(anySet())).thenReturn(new HashSet<PurchaseAgreement>() {
            {
                add(purchaseAgreement1);
                add(purchaseAgreement2);
            }
        });

        reportItems = new HashSet<ReportItem>() {
            {
                addAll(orderConfirmation1.getItems());
                addAll(orderConfirmation2.getItems());
            }
        };

        whenRetrievingShippingAddress();

        assertThat(address, is(not(nullValue())));
    }

    private void whenRetrievingShippingAddress() {
        address = shippingAddressService.retrieveShippingAddressOrFail(reportItems);
    }

}
