package de.switajski.priebes.flexibleorders.service.api;

import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.EDWARD;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.PAUL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.SALOME;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.exceptions.DeviatingExpectedDeliveryDatesException;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.specification.HasCustomerSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.IsInvoiceItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.OverdueItemSpecification;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testconfiguration.AbstractSpringContextTestConfiguration;
import de.switajski.priebes.flexibleorders.testdata.StandardTestDataRule;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliverOffTheRecordIntegrationTest extends AbstractSpringContextTestConfiguration {

    @Rule
    @Autowired
    public StandardTestDataRule rule;

    @Autowired
    ShippingService shippingService;

    @Autowired
    ReportItemRepository reportItemRepository;

    DeliverParameter deliverParameter = new DeliverParameter();

    @Ignore("future functionality")
    @Test
    public void shouldDeliverAnItemOffTheRecord() throws Exception {
        String salome = SALOME.getProductNumber();
        Long edward = EDWARD.getCustomerNumber();
        deliverParameter.setCustomerId(edward);
        deliverParameter.setItems(Arrays.asList(overdueItem(), offTheRecordItem(salome)));

        whenDelivering();

        overdueInvoicesExistsWith(salome, edward);
    }

    private void overdueInvoicesExistsWith(String salome, Long edward) {
        List<ReportItem> overdueInvoicingItems = reportItemRepository.findAll(
                where(new HasCustomerSpecification(edward.toString()))
                        .and(new IsInvoiceItemSpecification())
                        .and(new OverdueItemSpecification()));

        assertTrue(
                overdueInvoicingItems.stream()
                        .anyMatch(i -> i.getOrderItem()
                                .getProduct()
                                .getProductNumber()
                                .equals(salome)));
    }

    private void whenDelivering() throws ContradictoryAddressException, DeviatingExpectedDeliveryDatesException, NoItemsToShipFoundException {
        shippingService.ship(deliverParameter);
    }

    private ItemDto offTheRecordItem(String productNo) {
        ItemDto item = new ItemDto();
        item.setProduct(productNo);
        item.setOffTheRecord(true);
        item.setPriceNet(BigDecimal.valueOf(12.9));
        item.setQuantityLeft(30);
        return item;
    }

    private ItemDto overdueItem() {
        Integer overdueQty = 6;

        ItemDto itemDto = new ItemDto();
        itemDto.setProduct(PAUL.getProductNumber());
        itemDto.setQuantityLeft(overdueQty);
        return itemDto;
    }

}
