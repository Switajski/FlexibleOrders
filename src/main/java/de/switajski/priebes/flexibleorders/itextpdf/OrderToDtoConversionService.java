package de.switajski.priebes.flexibleorders.itextpdf;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.BusinessConstants;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;

@Service
public class OrderToDtoConversionService {

    @Transactional(readOnly = true)
    public ReportInPdf toDto(Order order) {
        OrderInPdf dto = new OrderInPdf();
        dto.created = order.getCreated();
        Customer customer = order.getCustomer();
        dto.customerNumber = customer.getCustomerNumber();
        dto.documentNumber = order.getOrderNumber();
        dto.customerFirstName = customer.getFirstName();
        dto.customerLastName = customer.getLastName();
        dto.customerEmail = customer.getEmail();
        dto.customerPhone = customer.getPhone();
        dto.address = BusinessConstants.MY_ADDRESS;
        dto.subject = "Bestellung " + order.getOrderNumber();
        dto.orderItems = order.getItems();
        dto.netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity2(order.getItems()));
        dto.vatRate = order.getVatRate();
        return dto;
    }

}
