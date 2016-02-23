package de.switajski.priebes.flexibleorders.itextpdf;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.BusinessConstants;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

@Service
public class OrderToDtoConversionService {

    @Transactional(readOnly = true)
    public ReportDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.created = order.getCreated();
        Customer customer = order.getCustomer();
        dto.customerNumber = customer.getCustomerNumber();
        dto.documentNumber = order.getOrderNumber();
        dto.customerFirstName = customer.getFirstName();
        dto.customerLastName = customer.getLastName();
        dto.customerEmail = customer.getEmail();
        dto.customerPhone = customer.getPhone();
        dto.address = BusinessConstants.MY_ADDRESS;
        dto.orderItems = order.getItems();
        dto.netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity2(order.getItems()));
        dto.vatRate = order.getVatRate();
        return dto;
    }

}
