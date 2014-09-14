package de.switajski.priebes.flexibleorders.web;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.CancelReport;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.service.process.DeliveryService;
import de.switajski.priebes.flexibleorders.service.process.InvoicingService;
import de.switajski.priebes.flexibleorders.service.process.OrderService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
// TODO: distribute methods on business controllers
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoicingService invoicingService;
    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse confirm(
            @RequestBody JsonCreateReportRequest confirmRequest)
            throws Exception {
        // TODO: see if Conversion factory / Jackson serializer has a strip to
        // null method
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setContact1(StringUtils.stripToNull(confirmRequest.getContact1()));
        contactInformation.setContact2(StringUtils.stripToNull(confirmRequest.getContact2()));
        contactInformation.setContact3(StringUtils.stripToNull(confirmRequest.getContact3()));
        contactInformation.setContact4(StringUtils.stripToNull(confirmRequest.getContact4()));

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setContactInformation(contactInformation);
        customerDetails.setMark(StringUtils.stripToNull(confirmRequest.getMark()));
        customerDetails.setPaymentConditions(StringUtils.stripToNull(confirmRequest.getPaymentConditions()));
        customerDetails.setSaleRepresentative(StringUtils.stripToNull(confirmRequest.getSaleRepresentative()));
        customerDetails.setVatIdNo(StringUtils.stripToNull(confirmRequest.getValueAddedTaxIdNo()));
        customerDetails.setVendorNumber(StringUtils.stripToNull(confirmRequest.getVendorNumber()));

        ConfirmParameter confirmParameter = new ConfirmParameter(
                extractOrderNumber(confirmRequest),
                StringUtils.stripToNull(confirmRequest.getOrderConfirmationNumber()),
                confirmRequest.getExpectedDelivery(),
                confirmRequest.getDeliveryMethodNo(),
                confirmRequest.createDeliveryAddress(),
                confirmRequest.createInvoiceAddress(),
                confirmRequest.getItems());
        confirmParameter.customerDetails = customerDetails;
        OrderConfirmation confirmationReport = orderService.confirm(
                confirmParameter);
        return ExtJsResponseCreator.createResponse(confirmationReport);
    }

    private String extractOrderNumber(JsonCreateReportRequest confirmRequest) {
        if (confirmRequest.getItems().isEmpty()) throw new IllegalArgumentException(
                "Liste der Auftragspositionen ist leer");
        return confirmRequest.getItems().iterator().next().orderNumber;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse order(@RequestBody JsonCreateReportRequest deliverRequest)
            throws Exception {
        deliverRequest.validate();

        OrderParameter orderParameter = new OrderParameter(
                deliverRequest.getCustomerId(),
                deliverRequest.getOrderNumber(),
                deliverRequest.getCreated(),
                deliverRequest.getItems());
        orderParameter.expectedDelivery = deliverRequest.getExpectedDelivery();

        Order order = orderService.order(
                orderParameter);

        return ExtJsResponseCreator.createResponse(order);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse invoice(
            @RequestBody JsonCreateReportRequest deliverRequest)
            throws Exception {
        deliverRequest.validate();

        Invoice invoice = invoicingService.invoice(
                deliverRequest.getInvoiceNumber(),
                deliverRequest.getPaymentConditions(),
                deliverRequest.getCreated(),
                deliverRequest.getItems(),
                deliverRequest.getBilling());
        return ExtJsResponseCreator.createResponse(invoice);
    }

    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse deliver(
            @RequestBody JsonCreateReportRequest deliverRequest)
            throws Exception {
        deliverRequest.validate();

        DeliveryNotes dn = deliveryService.deliver(
                new DeliverParameter(
                        deliverRequest.getDeliveryNotesNumber(),
                        deliverRequest.getTrackNumber(),
                        deliverRequest.getPackageNumber(),
                        new Amount(deliverRequest.getShipment(), Currency.EUR),
                        deliverRequest.getCreated(),
                        deliverRequest.getItems()));
        return ExtJsResponseCreator.createResponse(dn);
    }

    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse agree(
            @RequestParam(value = "orderAgreementNumber", required = true) String orderAgreementNumber,
            @RequestParam(value = "orderConfirmationNumber", required = true) String orderConfirmationNumber) {
        OrderAgreement orderAgreement = orderService.agree(orderConfirmationNumber, orderAgreementNumber);
        return ExtJsResponseCreator.createResponse(orderAgreement);
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse deleteOrder(
            @RequestParam(value = "orderNumber", required = true) String orderNumber) {
        orderService.deleteOrder(orderNumber);
        return ExtJsResponseCreator.createResponse(orderNumber);
    }

    @RequestMapping(value = "/deleteReport", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse deleteReport(
            @RequestParam(value = "documentNumber", required = true) String documentNumber)
            throws Exception {
        orderService.deleteReport(documentNumber);
        return ExtJsResponseCreator.createResponse(documentNumber);
    }

    @RequestMapping(value = "/cancelReport", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse cancelConfirmationReport(
            @RequestParam(value = "confirmationNumber", required = true) String orderConfirmationNumber)
            throws Exception {
        CancelReport cr = orderService
                .cancelReport(orderConfirmationNumber);
        return ExtJsResponseCreator.createResponse(cr);
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse complete(
            @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber)
            throws Exception {
        Receipt receipt = orderService.markAsPayed(invoiceNumber, "B"
                + invoiceNumber, new Date());
        return ExtJsResponseCreator.createResponse(receipt);
    }

}
