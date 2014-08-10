package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.ProductType;

public class ProductBuilder implements Builder<Product>{

	private Long productNumber;

	private ProductType productType;

	private String name;

	@Override
	public Product build() {
		Product product = new Product();
		product.setProductNumber(productNumber);
		product.setProductType(productType);
		product.setName(name);
		return product;
	}

	public ProductBuilder setProductNumber(Long productNumber) {
		this.productNumber = productNumber;
		return this;
	}

	public ProductBuilder setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	public ProductBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
}
