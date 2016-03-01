package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
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

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConverterService;
import de.switajski.priebes.flexibleorders.testconfiguration.SpringMvcWithTestDataTestConfiguration;
import de.switajski.priebes.flexibleorders.validation.ConsistentInvoicingAddress;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingApiIntegrationTest extends SpringMvcWithTestDataTestConfiguration {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ReportRepository rRepo;

    @Autowired
    private ReportItemToItemDtoConverterService ri2IdConverter;

    private InvoicingParameter invoicingParameter = new InvoicingParameter();

    @Test
    public void shouldBeRejectedByValidationAndClientShouldReceiveSpecialHandlingTag() throws Exception {
        invoicingParameter.setInvoiceNumber("TEST-I");
        invoicingParameter.setItems(overdueItemsof("L13", "L15"));

        whenInvoicing()
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString(
                        ConsistentInvoicingAddress.SPECIAL_HANDLING_TAG)));
    }

    private List<ItemDto> overdueItemsof(String... strings) {
        Set<ReportItem> ris = new HashSet<ReportItem>();
        for (String str : strings) {
            ris.addAll(rRepo.findByDocumentNumber(str).getItems());
        }

        Set<ReportItem> overdueRis = ris.stream()
                .filter(ri -> ri.isOverdue())
                .collect(Collectors.toSet());

        List<ItemDto> overdueItemDtos = new ArrayList<ItemDto>();
        for (ReportItem ri : overdueRis) {
            overdueItemDtos.add(ri2IdConverter.convert(ri, ri.overdue()));
        }
        return overdueItemDtos;
    }

    private ResultActions whenInvoicing() throws Exception {
        return mvc.perform(post("/transitions/invoice")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(invoicingParameter)));
    }
}
