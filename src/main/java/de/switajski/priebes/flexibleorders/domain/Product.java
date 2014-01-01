package de.switajski.priebes.flexibleorders.domain;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.switajski.priebes.flexibleorders.reference.ProductType;

@Embeddable
@Entity
public class Product extends GenericEntity {

    @NotNull
    private Long productNumber;

    @NotNull
    @Enumerated
    private ProductType productType;

    @NotNull
    @Column(unique = true)
    private String name;

    @Min(0L)
    private BigDecimal recommendedPriceNet;
    
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public BigDecimal getRecommendedPriceNet() {
		return recommendedPriceNet;
	}

	public void setRecommendedPriceNet(BigDecimal recommendedPriceNet) {
		this.recommendedPriceNet = recommendedPriceNet;
	}
}
