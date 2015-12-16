package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.ReportingService;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.testdata.TestDataCreator;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Transactional
public class ReportItemControllerIntegrationTest extends TestDataCreator {

    @Autowired
    private TransitionsService orderingService;
    @Autowired
    private ConfirmingService confirmingService;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private CustomerRepository cRepo;
    @Autowired
    private CatalogDeliveryMethodRepository deliveryMethodRepo;
    @Autowired
    private ReportItemController riController;

    String filters;

    JsonObjectResponse response;
    List<ItemDto> retrievedItems;

    @Test
    @Ignore("just failing on Travis CI")
    public void shouldRetrieveSpecifiedAgreedItemsToBeShipped() throws Exception {
        super.createTestData();
        givenFilterOnAgreedItems();

        whenListingItemsToBeProcessed();

        assertThat(filterReport("AB11").size(), equalTo(4));
        assertThat(filterReport("AB13").size(), equalTo(4));
        assertThat(filterReport("AB15").size(), equalTo(2));
        assertThat(filterReport("AB22").size(), equalTo(2));
    }

    @Test
    @Ignore("just failing on Travis CI")
    public void shouldRetrieveSpecifiedShippedItemsToBeInvoiced() throws Exception {
        super.createTestData();
        givenFilterOnShippedItems();

        whenListingItemsToBeProcessed();

        assertThat(filterReport("L13").size(), equalTo(3));

        // share same position from order confirmation AB11 - "Paul anthra dots"
        assertThat(filterReport("L14").size(), equalTo(1));
        assertThat(filterReport("L15").size(), equalTo(2));
    }

    @Test
    @Ignore("just failing on Travis CI")
    public void shouldRetrieveSpecifiedInvoicedItemsToBeMarkedAsPayed() throws Exception {
        super.createTestData();
        givenFilterOnInvoicedItems();

        whenListingItemsToBeProcessed();

        assertThat(filterReport("R11").size(), equalTo(4));
    }

    private List<ItemDto> filterReport(String string) {
        List<ItemDto> filteredItems = new ArrayList<ItemDto>();
        for (ItemDto item : retrievedItems) {
            if (item.documentNumber.equals(string)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    @SuppressWarnings("unchecked")
    private void whenListingItemsToBeProcessed() throws Exception {
        response = riController.listAllToBeProcessed(1, null, 20, null, filters);
        retrievedItems = ((List<ItemDto>) response.getData());
    }

    private void givenFilterOnAgreedItems() {
        filters = "[{\"property\":\"status\",\"value\":\"agreed\"}]";
    }

    private void givenFilterOnShippedItems() {
        filters = "[{\"property\":\"status\",\"value\":\"shipped\"}]";
    }

    private void givenFilterOnInvoicedItems() {
        filters = "[{\"property\":\"status\",\"value\":\"invoiced\"}]";
    }

}
