package de.switajski.priebes.flexibleorders.testdata;

import java.util.Arrays;

import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderParameterShorthand {

    public static OrderParameter orderParam(String orderNumber, Customer customer, LocalDate expectedDelivery, ItemDto... items) {
        OrderParameter op = new OrderParameter();
        op.customerNumber = customer.getCustomerNumber();
        op.orderNumber = orderNumber;
        op.expectedDelivery = expectedDelivery;
        op.reportItems = Arrays.asList(items);
        return op;
    }

}
