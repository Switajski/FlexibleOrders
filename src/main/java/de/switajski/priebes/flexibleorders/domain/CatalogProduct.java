package de.switajski.priebes.flexibleorders.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.ProductType;

@Entity
public class CatalogProduct extends GenericEntity {
	
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

	private boolean active;
	
	private String ImageGalery;
	
	private Long sortOrder;
	
	@ManyToOne
	private Category category;
	

	public Long getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(Long productNumber) {
		this.productNumber = productNumber;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRecommendedPriceNet() {
		return recommendedPriceNet;
	}

	public void setRecommendedPriceNet(BigDecimal recommendedPriceNet) {
		this.recommendedPriceNet = recommendedPriceNet;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getImageGalery() {
		return ImageGalery;
	}

	public void setImageGalery(String imageGalery) {
		ImageGalery = imageGalery;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
