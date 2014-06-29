package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.switajski.priebes.flexibleorders.reference.ProductType;

@Embeddable
public class Product {

	/**
	 * natural id
	 */
	@NotNull
	private Long productNumber;

	@NotNull
	@Enumerated
	private ProductType productType;

	@NotNull
	private String name;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Product))
			return false;
		Product c = (Product) obj;
		if (this.getProductNumber().equals(c.getProductNumber()))
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
	
	public boolean hasProductNo(){
		if (getProductNumber() == null || getProductNumber().equals(0L))
			return false;
		return true;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(
				this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
