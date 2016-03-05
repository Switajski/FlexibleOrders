package de.switajski.priebes.flexibleorders.web;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementWriteService;
import de.switajski.priebes.flexibleorders.validation.ReportNumberValidator;
import de.switajski.priebes.flexibleorders.web.dto.AddressDto;
import de.switajski.priebes.flexibleorders.web.dto.ChangeAddressParameter;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@CrossOrigin
@Controller
public class PurchaseAgreementController {

    @Autowired
    PurchaseAgreementWriteService paService;

    @Autowired
    PurchaseAgreementReadService paReadService;

    @Autowired
    ReportRepository reportRepo;

    @Autowired
    ReportNumberValidator rnValidator;

    @RequestMapping(value = "/reports/shippingAddress", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse changeShippingAddress(
            @RequestBody @Valid ChangeAddressParameter parameter) {

        for (String docNo : parameter.getDocumentNumbers()) {
            paService.changeShippingAddress(docNo, parameter.getAddress());
        }

        return ExtJsResponseCreator.createSuccessResponse(null);
    }

    @RequestMapping(value = "/reports/invoicingAddress", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse changeInvoicingAddress(
            @RequestBody @Valid ChangeAddressParameter parameter) {

        for (String docNo : parameter.getDocumentNumbers()) {
            paService.changeInvoicingAddress(docNo, parameter.getAddress());
        }

        return ExtJsResponseCreator.createSuccessResponse(null);
    }

    @RequestMapping(value = "/reports/shippingAddress", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse readShippingAddresses(HttpServletRequest request) {
        Set<String> potentialParams = new HashSet<String>();

        for (Map.Entry<String, String[]> entry : ((Map<String, String[]>) request.getParameterMap()).entrySet()) {
            if (StringUtils.isNumeric(entry.getKey())) {
                potentialParams.add(entry.getValue()[0]);
            }
        }

        Set<OrderConfirmation> reports = potentialParams.stream()
                .map(p -> reportRepo.findByDocumentNumber(p))
                .filter(r -> r instanceof OrderConfirmation)
                .map(OrderConfirmation.class::cast)
                .collect(Collectors.toSet());

        Set<AddressDto> addressDtos = new HashSet<AddressDto>();
        for (OrderConfirmation oc : reports) {
            Address address = oc.actualPurchaseAgreement().getShippingAddress();
            addressDtos.add(new AddressDto(address, oc.getDocumentNumber()));
        }

        JsonObjectResponse response = new JsonObjectResponse();
        response.setSuccess(true);
        response.setTotal(1L);
        response.setData(addressDtos);
        return response;
    }
}
