package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.reference.ProductType;

/**
 * 
 * @author Marek Switajski
 *
 */
@Entity
public class CatalogProduct extends GenericEntity {

	@Column(unique = true)
	@NotNull
	private String productNumber;

	@NotNull
	@Enumerated
	private ProductType productType;

	@NotNull
	private String name;

	@Embedded
	private Amount recommendedPriceNet;

	private boolean active;

	private String imageGalery;

	private Long sortOrder;

	@ManyToOne
	private Category category;

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	@JsonIgnore
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

	public Amount getRecommendedPriceNet() {
		return recommendedPriceNet;
	}

	public void setRecommendedPriceNet(Amount recommendedPriceNet) {
		this.recommendedPriceNet = recommendedPriceNet;
	}

	@JsonIgnore
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@JsonIgnore
	public String getImageGalery() {
		return imageGalery;
	}

	public void setImageGalery(String imageGalery) {
		this.imageGalery = imageGalery;
	}
	
	@JsonIgnore
	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@JsonIgnore
	public Product toProduct() {
		Product product = new Product();
		product.setName(name);
		product.setProductNumber(this.productNumber);
		product.setProductNumber(productNumber);
		product.setProductType(ProductType.PRODUCT);
		return product;
	}

}
