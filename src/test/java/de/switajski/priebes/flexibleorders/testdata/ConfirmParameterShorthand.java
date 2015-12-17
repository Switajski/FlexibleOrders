package de.switajski.priebes.flexibleorders.testdata;

import java.time.LocalDate;
import java.util.Arrays;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ConfirmParameterShorthand {

    public static ConfirmParameter confirm(String orderNumber, String confirmNumber, Customer customer,
            LocalDate expectedDelivery, ItemDto... items) {
        ConfirmParameter cp = new ConfirmParameter();
        cp.setOrderNumber(orderNumber);
        cp.setConfirmNumber(confirmNumber);
        cp.setCustomerNumber(customer.getCustomerNumber());
        cp.setCustomerDetails(customer.getDetails());
        cp.setInvoiceAddress(customer.getInvoiceAddress());
        cp.setShippingAddress(customer.getShippingAddress());
        cp.setItems(Arrays.asList(items));
        return cp;
    }
}
