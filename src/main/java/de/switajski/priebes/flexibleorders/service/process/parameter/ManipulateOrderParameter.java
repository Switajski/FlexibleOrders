package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ManipulateOrderParameter {
    @NotNull
    private String orderNumber;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date created;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expectedDelivery;
    @NotEmpty
    @Valid
    private List<ItemDto> items;

    public ManipulateOrderParameter() {
        created = new Date();
    }

    public ManipulateOrderParameter(
            String orderNumber,
            Date created,
            List<ItemDto> items) {
        this.orderNumber = orderNumber;
        this.created = created;
        this.items = items;
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
