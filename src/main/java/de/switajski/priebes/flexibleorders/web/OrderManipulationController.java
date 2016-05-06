package de.switajski.priebes.flexibleorders.web;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.api.OrderManipulationService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ManipulateOrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
public class OrderManipulationController extends ExceptionController {

    @Autowired
    private OrderManipulationService orderManipulationService;

    @RequestMapping(value = "/manipulate", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse manipulateOrder(@RequestBody @Valid ManipulateOrderParameter orderRequest)
            throws Exception {
        Set<ItemDto> order = orderManipulationService.manipulateOrder(orderRequest);
        return ExtJsResponseCreator.createSuccessfulTransitionResponse(order, orderRequest.getItems());
    }
}
