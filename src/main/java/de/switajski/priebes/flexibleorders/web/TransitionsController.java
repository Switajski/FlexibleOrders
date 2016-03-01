package de.switajski.priebes.flexibleorders.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.CancelReport;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.api.AgreeingService;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;
import de.switajski.priebes.flexibleorders.service.api.InvoicingService;
import de.switajski.priebes.flexibleorders.service.api.MarkingPaidService;
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.process.parameter.BillingParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@CrossOrigin
@Controller
// TODO: distribute methods on business controllers
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
            @RequestBody @Valid ConfirmParameter confirmRequest)
                    throws Exception {
        confirmRequest.setOrderNumber(confirmRequest.getItems().iterator().next().getOrderNumber());
        OrderConfirmation confirmationReport = confirmService.confirm(confirmRequest);
        return ExtJsResponseCreator.createSuccessResponse(confirmationReport);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse order(@RequestBody @Valid OrderParameter orderRequest)
            throws Exception {

        Order order = transistionsService.order(orderRequest);

        return ExtJsResponseCreator.createSuccessResponse(order);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse invoice(
            @RequestBody @Valid InvoicingParameter invoicingRequest)
                    throws Exception {

        Invoice invoice = invoicingService.invoice(invoicingRequest);
        return ExtJsResponseCreator.createSuccessResponse(invoice);
    }

    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse deliver(
            @RequestBody @Valid DeliverParameter deliverParameter,
            HttpServletResponse response)
                    throws Exception {

        try {
            if (deliverParameter.isSingleDeliveryNotes()) {
                DeliveryNotes deliveryNotes = shippingService.ship(deliverParameter);
                return ExtJsResponseCreator.createSuccessResponse(deliveryNotes);
            }
            else {
                Set<DeliveryNotes> dns = shippingService.shipMany(deliverParameter);
                return ExtJsResponseCreator.createSuccessResponse(dns);
            }
        }
        catch (ContradictoryPurchaseAgreementException e) {
            // TODO: use custom validator #103
            JsonObjectResponse resp = new JsonObjectResponse();
            resp.setErrors(new HashMap<String, String>() {
                {
                    put("ignoreContradictoryExpectedDeliveryDates", e.getMessage());
                }
            });
            resp.setMessage(e.getMessage());
            resp.setSuccess(false);
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return resp;
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

    @RequestMapping(value = "/markPaid", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse markPaid(
            @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber)
                    throws Exception {
        Receipt receipt = markingPaidService.markAsPayed(new BillingParameter(invoiceNumber, "B"
                + invoiceNumber, new Date()));
        return ExtJsResponseCreator.createSuccessResponse(receipt);
    }

}
