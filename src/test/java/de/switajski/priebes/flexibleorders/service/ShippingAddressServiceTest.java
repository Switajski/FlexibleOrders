package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;

public class ShippingAddressServiceTest {

    @InjectMocks
    ShippingAddressService shippingAddressService = new ShippingAddressService();

    Set<ReportItem> reportItems;

    Address address;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        reportItems = new HashSet<ReportItem>();
        reportItems.add(ConfirmationItemBuilder.createConfirmationItemWithAddress(1));
        reportItems.add(ConfirmationItemBuilder.createConfirmationItemWithAddress(2));

        whenRetrievingShippingAddress();
    }

    @Test
    public void shouldFindOneShippingAdresses() {
        reportItems = new HashSet<ReportItem>();
        reportItems.add(ConfirmationItemBuilder.createConfirmationItemWithAddress(1));
        reportItems.add(ConfirmationItemBuilder.createConfirmationItemWithAddress(1));

        whenRetrievingShippingAddress();

        assertThat(address, is(not(nullValue())));
    }

    private void whenRetrievingShippingAddress() {
        address = shippingAddressService.retrieveShippingAddressOrFail(reportItems);
    }

}
