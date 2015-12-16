package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderParameter {
    private Long customerNumber;
    private String orderNumber;
    private Date created;
    private LocalDate expectedDelivery;
    private List<ItemDto> reportItems;

    public OrderParameter() {
        created = new Date();
    }

    public OrderParameter(Long customerNumber, String orderNumber,
            Date created, List<ItemDto> reportItems) {
        this.customerNumber = customerNumber;
        this.orderNumber = orderNumber;
        this.created = created;
        this.reportItems = reportItems;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public LocalDate getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDate expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public List<ItemDto> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<ItemDto> reportItems) {
        this.reportItems = reportItems;
    }

}
