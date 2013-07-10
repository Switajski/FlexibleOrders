package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooJpaEntity
public class OrderItem extends Item {

    /**
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date expectedDelivery;

    /**
     */
    @NotNull
    private int orderItemNumber;

    /**
     * 
     * @param toSupplier
     * @return
     */
	public ShippingItem confirm(boolean toSupplier) {
		ShippingItem si = new ShippingItem(this, toSupplier);
		return si;
	}
}
