package de.switajski.priebes.flexibleorders.testdata;

import java.util.Arrays;

import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ConfirmParameterShorthand {

    public static ConfirmParameter confirm(String orderNumber, String confirmNumber, Customer customer, 
            LocalDate expectedDelivery, ItemDto... items){
        ConfirmParameter cp = new ConfirmParameter();
        cp.orderNumber = orderNumber;
        cp.confirmNumber = confirmNumber;
        cp.customerNumber = customer.getCustomerNumber();
        cp.customerDetails = customer.getDetails();
        cp.deliveryMethodNo = 1L;
        cp.invoiceAddress = customer.getInvoiceAddress();
        cp.shippingAddress = customer.getShippingAddress();
        cp.orderItems = Arrays.asList(items);
        return cp;
    }
}
