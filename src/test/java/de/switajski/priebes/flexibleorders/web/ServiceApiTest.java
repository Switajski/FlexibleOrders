package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testdata.StandardTestDataRule;
import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;
import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;

@Transactional
public class ServiceApiTest extends SpringMvcTestConfiguration {

    @Autowired
    private ReportRepository rRepo;

    @Autowired
    private ReportItemToItemDtoConverterService ri2IdConverter;

    @Rule
    @Autowired
    public StandardTestDataRule rule;
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
    public void pdfShouldBeAvailable() throws Exception {
        mvc.perform(post("/reports/AB11.pdf"))
                .andExpect(content().contentTypeCompatibleWith("application/pdf"));
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

    @Test
    public void shouldValidateCustomer() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.customerNumber = 123L;

        mvc.perform(post("/customers/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(customer)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("companyName")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldCreateCustomer() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.customerNumber = 1234L;
        customer.companyName = "Flexible Inc.";

        mvc.perform(post("/customers/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(customer)))
                .andExpect(status().is2xxSuccessful());
    }

    private String createStringRequest(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}
