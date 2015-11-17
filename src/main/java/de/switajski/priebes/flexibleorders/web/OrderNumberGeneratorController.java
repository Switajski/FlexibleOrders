package de.switajski.priebes.flexibleorders.web;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.api.OrderNumberGeneratorService;

@Controller
public class OrderNumberGeneratorController extends ExceptionController {

    @Autowired
    OrderNumberGeneratorService service;

    @RequestMapping(value = "/order/generateOrderNumber", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse generate() {
        JsonObjectResponse response = new JsonObjectResponse();
        response.setData(service.generate(new LocalDate()));
        response.setSuccess(true);
        response.setMessage("Order number generated");
        return response;
    }
}
