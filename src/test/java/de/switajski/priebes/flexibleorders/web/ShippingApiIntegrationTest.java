package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.SpringMvcWithTestDataTestConfiguration;
import de.switajski.priebes.flexibleorders.web.dto.ChangeShippingAddressParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;
import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;

/**
 * Integration test to assure functionality of common shipping use cases,
 * e.g.: </br>
 * <ul>
 * <li>deliver with deviating expected delivery dates</li>
 * <li>deliver with deviating shipping addresses</li>
 * <li>delivery notes pdf is available</li>
 * </ul>
 * 
 * @author switajski
 *
 */
public class ShippingApiIntegrationTest extends SpringMvcWithTestDataTestConfiguration {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ReportRepository rRepo;

    @Autowired
    private ReportItemToItemDtoConverterService ri2IdConverter;

    private DeliverParameter dr;

    @Test
    public void shouldBeRejectedByValidation() throws Exception {
        JsonCreateReportRequest request = new JsonCreateReportRequest();
        request.setCustomerId(45L);

        mvc.perform(post("/transitions/order")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(request)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"errors\":{\"items\":")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldRejectDuplicateDeliveryNotesNumber() throws Exception {
        dr = new DeliverParameter();
        dr.setDeliveryNotesNumber("L13");

        whenDelivering()
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("deliveryNotesNumber")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldRejectContradictoryExpectedDeliveryDatesAddresses() throws Exception {
        dr = new DeliverParameter();
        dr.setDeliveryNotesNumber("TEST-L");
        dr.setItems(new ArrayList<ItemDto>(overdueItemDtos()));

        whenDelivering()
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("ignoreContradictoryExpectedDeliveryDates")));
    }

    @Test
    public void shouldRejectContradictoryShippingAddresses() throws Exception {
        dr = new DeliverParameter();
        dr.setDeliveryNotesNumber("TEST-L");
        dr.setIgnoreContradictoryExpectedDeliveryDates(true);
        dr.setItems(new ArrayList<ItemDto>(overdueItemDtos()));

        whenDelivering()
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("#CPA-DA")));
    }

    @Test
    public void orderConfirmationsWithDeviatingShippingAddressShouldBeShippableByChangingAddress()
            throws Exception {

        // Change shipping address of AB11, AB13 and AB15, because they are
        // deviating.
        ChangeShippingAddressParameter param = givenChangeShippingAddressParameter();
        mvc.perform(post("/reports/shippingAddress")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(param)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("success")))
                .andExpect(status().is2xxSuccessful());
        em.flush();

        // deliver with changed address
        String docNo = "L_WITH_CHANGED_ADDRESSES";
        dr = new DeliverParameter();
        dr.setDeliveryNotesNumber(docNo);
        dr.setIgnoreContradictoryExpectedDeliveryDates(true);
        dr.setItems(new ArrayList<ItemDto>(overdueItemDtos()));
        whenDelivering().andExpect(status().is2xxSuccessful());
        em.flush();

        // assure new document is available in pdf
        expectPdfIsRendering(docNo);

        // assure old documents are available in pdf
        em.clear(); // items of report AB11 have 12 items, althoug db has only 5
        expectPdfIsRendering("AB11");
        expectPdfIsRendering("AB13");
        expectPdfIsRendering("AB15");

    }

    private ChangeShippingAddressParameter givenChangeShippingAddressParameter() {
        ChangeShippingAddressParameter param = new ChangeShippingAddressParameter();
        param.setCity("new city");
        param.setCountry(Country.AT);
        param.setName1("new Name1");
        param.setStreet("new street");
        param.setPostalCode(123);
        param.setDocumentNumbers(Arrays.asList(new String[] { "AB11", "AB13", "AB15" }));
        return param;
    }

    private Set<ItemDto> overdueItemDtos() {
        Set<ReportItem> reportItems = rRepo.findByDocumentNumber("AB11").getItems();
        reportItems.addAll(rRepo.findByDocumentNumber("AB13").getItems());
        reportItems.addAll(rRepo.findByDocumentNumber("AB15").getItems());

        List<ReportItem> overdueRis = reportItems.stream()
                .filter(ri -> ri.isOverdue())
                .collect(Collectors.toList());

        Set<ItemDto> overdueItemDtos = new HashSet<ItemDto>();
        for (ReportItem ri : overdueRis) {
            overdueItemDtos.add(ri2IdConverter.convert(ri, ri.overdue()));
        }
        return overdueItemDtos;
    }

    private ResultActions whenDelivering() throws Exception, JsonProcessingException {
        return mvc.perform(post("/transitions/deliver")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(dr)));
    }

}
