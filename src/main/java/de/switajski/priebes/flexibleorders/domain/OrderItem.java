package de.switajski.priebes.flexibleorders.domain;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

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
     */
    private Integer quantityLeft;
    
    /**
     * method to create set the initial state of an order item. These parameters are required to
     * persist the entity.
     * 
     * @param product the product ordered
     * @param customer the customer that ordered an product
     * @param quantity how many pieces of a product were ordered
     * @param orderNumber a unique order number
     */
    public void setInitialState(Product product, 
    		Customer customer, 
    		int quantity,
    		Long orderNumber){
    	setCustomer(customer);
    	setQuantity(quantity);
    	setOrderNumber(orderNumber);
    	
    	setProduct(product);
    	setCreated(new Date());
    	setQuantityLeft(quantity);
    }

    @Override
    public int compareTo(Item o) {
        // TODO Auto-generated method stub
        return 0;
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
	
	@Override
	public Status getStatus(){
		if (getQuantityLeft()==0)
			return Status.CONFIRMED;
		else return Status.ORDERED;
	}

}
