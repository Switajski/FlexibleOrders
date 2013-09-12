package de.switajski.priebes.flexibleorders.domain;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Status;

@RooJavaBean
@RooToString
@RooJpaEntity
public class OrderItem extends Item {

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date expectedDelivery;

    /**
     */
    @NotNull
    private int orderItemNumber;

    /**
     * this method represents the second transistion of the order process. 
     * The first one is to create an order item: </br>
     * 1. new Orderitem </br>
     * 2. orderItem.confirm </br>
     * 3. shippingItem.deliver </br>
     * ...
     * 
     * @param toSupplier
     * @return
     */
	public ShippingItem confirm(boolean toSupplier, int quantity, long orderConfirmationNumber) {
		setOrderConfirmationNumber(orderConfirmationNumber);
		ShippingItem si = new ShippingItem(this, quantity, toSupplier, orderConfirmationNumber);
		this.setStatus(Status.CONFIRMED);
		return si;
	}

	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getExpectedDelivery() {
        return this.expectedDelivery;
    }

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setExpectedDelivery(Date expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }
}
