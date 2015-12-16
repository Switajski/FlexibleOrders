package de.switajski.priebes.flexibleorders.testdata;

import java.time.LocalDate;
import java.util.Arrays;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderParameterShorthand {

    public static OrderParameter orderParam(String orderNumber, Customer customer, LocalDate expectedDelivery, ItemDto... items) {
        OrderParameter op = new OrderParameter();
        op.setCustomerNumber(customer.getCustomerNumber());
        op.setOrderNumber(orderNumber);
        op.setExpectedDelivery(expectedDelivery);
        op.setReportItems(Arrays.asList(items));
        return op;
    }

}
