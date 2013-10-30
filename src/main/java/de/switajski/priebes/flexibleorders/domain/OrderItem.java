package de.switajski.priebes.flexibleorders.domain;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Status;

@Entity
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
     */
    private Integer quantityLeft;
    
    /**
     * Constructor with minimal attributes needed to
     * persist the entity.
     * 
     * @param product the product ordered
     * @param customer the customer that ordered an product
     * @param quantity how many pieces of a product were ordered
     * @param orderNumber a unique order number
     */
    public OrderItem(OrderParameter orderParameter){
    	this.setOrderParameter(orderParameter);
    }
    
    public OrderItem(){}


    @Override
    public int compareTo(Item o) {
        // TODO Auto-generated method stub
    	throw new NotImplementedException();
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getExpectedDelivery() {
        return this.expectedDelivery;
    }

    @JsonDeserialize(using = JsonDateDeserializer.class)
    public void setExpectedDelivery(Date expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    /**
     * adds quantity to confirm. Afterwards the quantity left to reach the next CONFIRMED State 
     * is more
     * @param quantity amount to add in quantityLeft
     */
	public void addConfirmedQuantity(int quantity) {
		setQuantityLeft(getQuantityLeft()-quantity);
	}
	
	/**
	 * reduces quantity. Afterwards the quantity left to reach the next CONFIRMED State 
	 * is less
	 * @param quantity
	 */
	public void reduceConfirmedQuantity(int quantity) {
		setQuantityLeft(getQuantityLeft()+quantity);
	}
	
	//TODO: move to item
	@Override
	public Status getStatus(){
		if (getQuantityLeft()==0)
			return Status.CONFIRMED;
		else return Status.ORDERED;
	}

	public void setOrderParameter(OrderParameter orderParameter) {
		//TODO: create OrderParameter instead of seperate fields
		customer = orderParameter.getCustomer();
		expectedDelivery = orderParameter.getExpectedDelivery();
		orderNumber = orderParameter.getOrderNumber();
		product = orderParameter.getProduct();
		productName = orderParameter.getProduct().getName();
		productNumber = orderParameter.getProduct().getProductNumber();
		priceNet = orderParameter.getProduct().getPriceNet();
		quantity = orderParameter.getQuantity();
		quantityLeft = orderParameter.getQuantity();
		
	}
	
	public int getOrderItemNumber() {
        return this.orderItemNumber;
    }
    
    public void setOrderItemNumber(int orderItemNumber) {
        this.orderItemNumber = orderItemNumber;
    }
    
    public Integer getQuantityLeft() {
        return this.quantityLeft;
    }
    
    public void setQuantityLeft(Integer quantityLeft) {
        this.quantityLeft = quantityLeft;
    }


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
