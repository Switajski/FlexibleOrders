package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.ProductType;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Product {

    /**
     */
    @NotNull
    @Column(unique = true)
    private Long productNumber;

    /**
     */
    @NotNull
    @Enumerated
    private ProductType productType;

    /**
     */
    @NotNull
    private String name;

    /**
     */
    @NotNull
    private Boolean active;

    /**
     */
    private Long sortOrder;

    /**
     */
    private String intro;

    /**
     */
    private String description;

    /**
     */
    private String image;

    /**
     */
    private String imageGalery;

    /**
     */
    @ManyToOne
    private Category category;

    /**
     */
    @Min(0L)
    private BigDecimal priceNet;
    
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof Product)) return false;
    	Product c = (Product) obj;
    	if (this.getProductNumber()==c.getProductNumber())
    		return true;
    	return false;
    }
}
