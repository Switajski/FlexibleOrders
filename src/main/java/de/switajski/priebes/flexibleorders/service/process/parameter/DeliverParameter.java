package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.validation.ReportNumber;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@JsonAutoDetect
public class DeliverParameter {

    @ReportNumber(shouldExist = false)
    @NotNull
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String deliveryNotesNumber;
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private Long customerId;
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String trackNumber;
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String packageNumber;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal shipment;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;
    @Valid
    @NotEmpty
    private List<ItemDto> items;
    private boolean ignoreContradictoryExpectedDeliveryDates;
    private DeliveryMethod deliveryMethod;
    private boolean showPricesInDeliveryNotes;
    private boolean singleDeliveryNotes;

    /**
     * Constructor with mandatory fields
     * 
     * @param deliveryNotesNumber
     * @param created
     * @param agreementItemDtos
     */
    public DeliverParameter(
            String deliveryNotesNumber,
            LocalDate created,
            List<ItemDto> agreementItemDtos) {
        this.deliveryNotesNumber = deliveryNotesNumber;
        this.created = created;
        this.items = agreementItemDtos;
    }

    public DeliverParameter() {}

    public void addItem(ItemDto item) {
        items.add(item);
    }

    public String getDeliveryNotesNumber() {
        return deliveryNotesNumber;
    }

    public void setDeliveryNotesNumber(String deliveryNotesNumber) {
        this.deliveryNotesNumber = deliveryNotesNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public BigDecimal getShipment() {
        return shipment;
    }

    public void setShipment(BigDecimal shipment) {
        this.shipment = shipment;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public boolean isIgnoreContradictoryExpectedDeliveryDates() {
        return ignoreContradictoryExpectedDeliveryDates;
    }

    public void setIgnoreContradictoryExpectedDeliveryDates(boolean ignoreContradictoryExpectedDeliveryDates) {
        this.ignoreContradictoryExpectedDeliveryDates = ignoreContradictoryExpectedDeliveryDates;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public boolean isShowPricesInDeliveryNotes() {
        return showPricesInDeliveryNotes;
    }

    public void setShowPricesInDeliveryNotes(boolean showPricesInDeliveryNotes) {
        this.showPricesInDeliveryNotes = showPricesInDeliveryNotes;
    }

    public boolean isSingleDeliveryNotes() {
        return singleDeliveryNotes;
    }

    public void setSingleDeliveryNotes(boolean singleDeliveryNotes) {
        this.singleDeliveryNotes = singleDeliveryNotes;
    }
}
