package de.switajski.priebes.flexibleorders.service;

import static de.switajski.priebes.flexibleorders.testdata.ConfirmParameterShorthand.confirm;
import static de.switajski.priebes.flexibleorders.testdata.ItemDtoShorthand.item;
import static de.switajski.priebes.flexibleorders.testdata.OrderParameterShorthand.orderParam;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AMY;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.DHL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.JUREK;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.MILADKA;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.NAIDA;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.PAUL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.SALOME;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.UPS;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.YVONNE;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.delay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.specification.AgreedItemsToBeShippedSpec;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.OrderingService;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ProcessingServiceIntegrationTest extends AbstractSpringContextTest {

    @Autowired
    private OrderingService orderingService;
    @Autowired
    private ConfirmingService confirmingService;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private CustomerRepository cRepo;
    @Autowired
    private CatalogDeliveryMethodRepository deliveryMethodRepo;

    @Rollback(false)
    @Test
    public void shouldDisplayWholeDocuments() {

        givenCustomersAndDeliveryMethods();
        givenOrderConfirmations();

        int itemLimit = 2; // qty of documents
        Page<ItemDto> toBeShipped = reportingService.retrieve(
                new PageRequest(0, itemLimit), new AgreedItemsToBeShippedSpec());

        assertThat(toBeShipped.getContent().size(), equalTo(16));// qty of items
        // in document

    }

    private void givenCustomersAndDeliveryMethods() {
        cRepo.save(NAIDA);
        cRepo.flush();
        deliveryMethodRepo.save(new CatalogDeliveryMethod(UPS));
        deliveryMethodRepo.save(new CatalogDeliveryMethod(DHL));
    }

    private void givenOrderConfirmations() {
        LocalDate now = new LocalDate(new Date());

        String b100 = "B100";
        orderingService.order(orderParam(b100, NAIDA, now,
                item(SALOME, 2),
                item(PAUL, 2),
                item(MILADKA, 2),
                item(JUREK, 2),
                item(AMY, 2),
                item(SALOME, 2),
                item(SALOME, 2),
                item(MILADKA, 2)));

        String b101 = "B101";
        orderingService.order(orderParam(b101, NAIDA, now,
                item(SALOME, 2),
                item(PAUL, 2),
                item(MILADKA, 2),
                item(JUREK, 2),
                item(AMY, 2),
                item(SALOME, 2),
                item(SALOME, 2),
                item(MILADKA, 2)));

        String b102 = "B102";
        orderingService.order(orderParam(b102, NAIDA, now,
                item(SALOME, 2),
                item(PAUL, 2),
                item(MILADKA, 2),
                item(JUREK, 2),
                item(AMY, 2),
                item(SALOME, 2),
                item(SALOME, 2),
                item(MILADKA, 2)));

        confirmingService.confirm(confirm(b100, "AB100", YVONNE, delay(10),
                item(SALOME, 2, b100),
                item(PAUL, 2, b100),
                item(MILADKA, 2, b100),
                item(JUREK, 2, b100),
                item(AMY, 2, b100),
                item(SALOME, 2, b100),
                item(SALOME, 2, b100),
                item(MILADKA, 2, b100)));

        confirmingService.confirm(confirm(b101, "AB101", YVONNE, delay(10),
                item(SALOME, 2, b101),
                item(PAUL, 2, b101),
                item(MILADKA, 2, b101),
                item(JUREK, 2, b101),
                item(AMY, 2, b101),
                item(SALOME, 2, b101),
                item(SALOME, 2, b101),
                item(MILADKA, 2, b101)));

        confirmingService.confirm(confirm(b102, "AB102", YVONNE, delay(10),
                item(SALOME, 2, b102),
                item(PAUL, 2, b102),
                item(MILADKA, 2, b102),
                item(JUREK, 2, b102),
                item(AMY, 2, b102),
                item(SALOME, 2, b102),
                item(SALOME, 2, b102),
                item(MILADKA, 2, b102)));
    }

}
