package de.switajski.priebes.flexibleorders.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.api.DeliveryNotesNumberGeneratorService;
import de.switajski.priebes.flexibleorders.service.api.OrderNumberGeneratorService;

@Controller
public class DocumentNumberGeneratorController extends ExceptionController {

    @Autowired
    OrderNumberGeneratorService orderNumberGeneratorService;

    @Autowired
    DeliveryNotesNumberGeneratorService deliveryNotesGeneratorService;

    @RequestMapping(value = "/order/generateNumber", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse generate() {
        JsonObjectResponse response = new JsonObjectResponse();
        response.setData(orderNumberGeneratorService.byymmggg(LocalDate.now()));
        response.setSuccess(true);
        response.setMessage("Order number generated");
        return response;
    }

    @RequestMapping(value = "/report/generateNumber", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse generateDocumentNumber(
            @RequestParam(value = "orderNumber", required = true) String orderNumber) {
        JsonObjectResponse response = new JsonObjectResponse();
        response.setData(orderNumberGeneratorService.yymmggg(LocalDate.now(), orderNumber));
        response.setSuccess(true);
        response.setMessage("Document number generated");
        return response;
    }

    @RequestMapping(value = "/deliveryNotes/generateNumber", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse generateDeliveryNotesNumber(
            @RequestParam(value = "orderConfirmationNumber", required = true) String orderConfirmationNumber) {
        JsonObjectResponse response = new JsonObjectResponse();
        response.setData(deliveryNotesGeneratorService.byOrderConfrimationNumber(orderConfirmationNumber));
        response.setSuccess(true);
        response.setMessage("Delivery notes number generated");
        return response;
    }
}
