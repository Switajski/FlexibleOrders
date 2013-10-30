package de.switajski.priebes.flexibleorders.domain;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.switajski.priebes.flexibleorders.reference.ProductType;

@Entity
public class Product extends GenericEntity {

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
    @Column(unique = true)
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

	

	public Long getProductNumber() {
        return this.productNumber;
    }

	public void setProductNumber(Long productNumber) {
        this.productNumber = productNumber;
    }

	public ProductType getProductType() {
        return this.productType;
    }

	public void setProductType(ProductType productType) {
        this.productType = productType;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Boolean getActive() {
        return this.active;
    }

	public void setActive(Boolean active) {
        this.active = active;
    }

	public Long getSortOrder() {
        return this.sortOrder;
    }

	public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

	public String getIntro() {
        return this.intro;
    }

	public void setIntro(String intro) {
        this.intro = intro;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String getImage() {
        return this.image;
    }

	public void setImage(String image) {
        this.image = image;
    }

	public String getImageGalery() {
        return this.imageGalery;
    }

	public void setImageGalery(String imageGalery) {
        this.imageGalery = imageGalery;
    }

	public Category getCategory() {
        return this.category;
    }

	public void setCategory(Category category) {
        this.category = category;
    }

	public BigDecimal getPriceNet() {
        return this.priceNet;
    }

	public void setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
