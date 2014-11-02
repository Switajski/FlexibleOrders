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
	private String no;

	@NotNull
	@Enumerated
	private ProductType type;

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

	//TODO: Naming Convention: product.getProductNumber?! -> product.getNo()!
	public String getProductNumber() {
		return this.no;
	}

	public void setProductNumber(String productNumber) {
		this.no = productNumber;
	}

	public ProductType getProductType() {
		return this.type;
	}

	public void setProductType(ProductType productType) {
		this.type = productType;
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
