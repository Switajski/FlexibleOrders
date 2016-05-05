package de.switajski.priebes.flexibleorders.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.report.CancelReport;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.exceptions.DeviatingExpectedDeliveryDatesException;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.api.AgreeingService;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;
import de.switajski.priebes.flexibleorders.service.api.InvoicingService;
import de.switajski.priebes.flexibleorders.service.api.MarkingPaidService;
import de.switajski.priebes.flexibleorders.service.api.NoItemsToShipFoundException;
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.process.parameter.BillingParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@CrossOrigin
@Controller
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController {

    @Autowired
    private TransitionsService transistionsService;
    @Autowired
    private InvoicingService invoicingService;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private AgreeingService agreeingService;
    @Autowired
    private MarkingPaidService markingPaidService;
    @Autowired
    private ConfirmingService confirmService;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse confirm(
            @RequestBody @Valid ConfirmParameter confirmRequest) throws Exception {
        List<ItemDto> completed = confirmRequest.getItems();
        confirmRequest.setOrderNumber(completed.iterator().next().getOrderNumber());
        Set<ItemDto> items = confirmService.confirm(confirmRequest);
        return ExtJsResponseCreator.createSuccessfulTransitionResponse(items, completed);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse order(@RequestBody @Valid OrderParameter orderRequest)
            throws Exception {
        Set<ItemDto> order = transistionsService.order(orderRequest);
        return ExtJsResponseCreator.createSuccessfulTransitionResponse(order, null);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse invoice(
            @RequestBody @Valid InvoicingParameter invoicingRequest)
            throws Exception {
        Set<ItemDto> created = invoicingService.invoice(invoicingRequest);
        List<ItemDto> completed = invoicingRequest.getItems();
        return ExtJsResponseCreator.createSuccessfulTransitionResponse(created, completed);
    }

    @RequestMapping(value = "/markPaid", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse markPaid(
            @RequestBody @Valid BillingParameter billingParameter)
            throws Exception {
        Set<ItemDto> createdReceiptItems = markingPaidService.markAsPayed(billingParameter);
        List<ItemDto> completed = billingParameter.getItems();
        return ExtJsResponseCreator.createSuccessfulTransitionResponse(createdReceiptItems, completed);
    }

    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse deliver(
            @RequestBody @Valid DeliverParameter deliverParameter,
            HttpServletResponse response)
            throws ContradictoryAddressException,
            DeviatingExpectedDeliveryDatesException,
            NoItemsToShipFoundException {

        if (deliverParameter.isSingleDeliveryNotes()) {
            Set<ItemDto> createdDeliveryNote = shippingService.ship(deliverParameter);
            Set<ItemDto> completed = new HashSet<ItemDto>(deliverParameter.getItems());
            return ExtJsResponseCreator.createSuccessfulTransitionResponse(
                    createdDeliveryNote,
                    completed);
        }
        else {
            Set<ItemDto> createdDeliveryNotes = shippingService.shipMany(deliverParameter);
            return ExtJsResponseCreator.createSuccessfulTransitionResponse(createdDeliveryNotes, deliverParameter.getItems());
        }

    }

    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse agree(
            @RequestParam(value = "orderAgreementNumber", required = true) String orderAgreementNumber,
            @RequestParam(value = "orderConfirmationNumber", required = true) String orderConfirmationNumber) {
        OrderConfirmation orderAgreement = agreeingService.agree(orderConfirmationNumber, orderAgreementNumber);
        return ExtJsResponseCreator.createSuccessResponse(orderAgreement);
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse deleteOrder(
            @RequestParam(value = "orderNumber", required = true) String orderNumber) {
        transistionsService.deleteOrder(orderNumber);
        return ExtJsResponseCreator.createSuccessResponse(orderNumber);
    }

    @RequestMapping(value = "/Report/{documentNumber}", method = RequestMethod.DELETE)
    public @ResponseBody JsonObjectResponse deleteReport(
            @PathVariable(value = "documentNumber") String documentNumber)
            throws Exception {
        transistionsService.deleteReport(documentNumber);
        return ExtJsResponseCreator.createSuccessResponse(documentNumber);
    }

    @RequestMapping(value = "/cancelReport", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse cancelConfirmationReport(
            @RequestParam(value = "confirmationNumber", required = true) String orderConfirmationNumber)
            throws Exception {
        CancelReport cr = transistionsService.cancelReport(orderConfirmationNumber);
        return ExtJsResponseCreator.createSuccessResponse(cr);
    }

}
