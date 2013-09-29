package de.switajski.priebes.flexibleorders.domain;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

@RooJavaBean
@RooToString
@RooJpaEntity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceItem extends Item {

    /**
     */
    private String invoiceName1;

    /**
     */
    private String invoiceName2;

    /**
     */
    @NotNull
    private String invoiceStreet;

    /**
     */
    @NotNull
    private String invoiceCity;

    /**
     */
    @NotNull
    private int invoicePostalCode;

    /**
     */
    @NotNull
    @Enumerated
    private Country invoiceCountry;
    
    /**
     */
    private String packageNumber;

    /**
     */
    private String trackNumber;

    /**
     */
    @NotNull
    @Min(0L)
    private int quantityLeft;

    public InvoiceItem() {
    }

    public void addCompletedQuantity(int quantity) {
    	this.setQuantityLeft(getQuantityLeft()-quantity);
	}
    
    public void reduceCompletedQuantity(int quantity) {
    	this.setQuantityLeft(getQuantityLeft()+quantity);
	}

	@Override
    public int compareTo(Item o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
	public Status getStatus(){
		if (getQuantityLeft()==0)
			return Status.COMPLETED;
		else return Status.SHIPPED;
	}

	public int getQuantityLeft() {
        return this.quantityLeft;
    }

	public void setQuantityLeft(int quantityLeft) {
        this.quantityLeft = quantityLeft;
    }
}
