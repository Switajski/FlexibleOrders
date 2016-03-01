package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.validation.UniqueOrderNumber;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderParameter {
    private Long customerNumber;
    @UniqueOrderNumber
    private String orderNumber;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date created;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expectedDelivery;
    private List<ItemDto> items;

    public OrderParameter() {
        created = new Date();
    }

    public OrderParameter(
            Long customerNumber,
            String orderNumber,
            Date created,
            List<ItemDto> items) {
        this.customerNumber = customerNumber;
        this.orderNumber = orderNumber;
        this.created = created;
        this.items = items;
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

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> reportItems) {
        this.items = reportItems;
    }

}
