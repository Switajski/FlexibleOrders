package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;

/**
 * 
 * @author Marek Switajski
 * 
 */
public class CatalogProductBuilder implements Builder<CatalogProduct> {

	private Long id;
	private String productNumber;
	private ProductType productType;
	private String name;
	private Amount recommendedPriceNet;
	private boolean active;
	private String imageGalery;
	private Long sortOrder;

	public CatalogProductBuilder(
			String name,
			String productNumber,
			ProductType productType) {
		this.productNumber = productNumber;
		this.productType = productType;
		this.name = name;
	};

	public CatalogProductBuilder() {
	}

	public static CatalogProduct buildWithGeneratedAttributes(Integer i) {
		return new CatalogProductBuilder(
				"name".concat(i.toString()),
				i.toString(),
				ProductType.PRODUCT)
				.generateAttributes(i).build();
	}

	public CatalogProductBuilder miladka() {
		this.setName("Miladka athra stars lime")
				.setProductNumber("28189")
				.setProductType(ProductType.PRODUCT)
				.setRecommendedPriceNet(new Amount(new BigDecimal("28.99")));
		return this;
	}

	public CatalogProductBuilder amy() {
		this.setName("Amy Fleece")
			.setProductNumber("75195")
			.setProductType(ProductType.PRODUCT)
			.setRecommendedPriceNet(new Amount(new BigDecimal("12.99")));
		return this;
	}

	public CatalogProductBuilder paul() {
		this.setName("Paul anthra dots")
			.setProductNumber("43092")
			.setProductType(ProductType.PRODUCT)
			.setRecommendedPriceNet(new Amount(new BigDecimal("43.99")));
		return this;
	}

	public CatalogProductBuilder salome() {
		this.setName("Salome stars lime")
			.setProductNumber("33025")
			.setProductType(ProductType.PRODUCT)
			.setRecommendedPriceNet(new Amount(new BigDecimal("33.99")));
		return this;
	}

	public CatalogProductBuilder jurek() {
		this.setName("Jurek destroyer")
			.setProductNumber("56026")
			.setProductType(ProductType.PRODUCT)
			.setRecommendedPriceNet(new Amount(new BigDecimal("56.99")));
		return this;
	}

	public CatalogProductBuilder generateAttributes(Integer i) {
		productType = ProductType.PRODUCT;
		name = "product";
		recommendedPriceNet = new Amount(new BigDecimal(i), Currency.EUR);
		productNumber = i.toString();
		name = name.concat(i.toString());
		return this;
	}

	public CatalogProductBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public CatalogProduct build() {
		CatalogProduct p = new CatalogProduct();
		p.setId(id);
		p.setProductNumber(productNumber);
		p.setProductType(productType);
		p.setName(name);
		p.setRecommendedPriceNet(recommendedPriceNet);
		p.setActive(active);
		p.setImageGalery(imageGalery);
		p.setSortOrder(sortOrder);
		return p;

	}

	public CatalogProductBuilder setProductNumber(String productNumber) {
		this.productNumber = productNumber;
		return this;
	}

	public CatalogProductBuilder setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	public CatalogProductBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public CatalogProductBuilder setRecommendedPriceNet(
			Amount recommendedPriceNet) {
		this.recommendedPriceNet = recommendedPriceNet;
		return this;
	}

	public CatalogProductBuilder setActive(boolean active) {
		this.active = active;
		return this;
	}

	public CatalogProductBuilder setImageGalery(String imageGalery) {
		this.imageGalery = imageGalery;
		return this;
	}

	public CatalogProductBuilder setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public static List<Product> buildShippingCosts() {
		List<Product> shippingCosts = new ArrayList<Product>();
		Product shipping1 = new CatalogProductBuilder(
				"DHL",
				null,
				ProductType.SHIPPING).build().toProduct();
		Product shipping2 = new CatalogProductBuilder(
				"UPS",
				null,
				ProductType.SHIPPING).build().toProduct();
		shippingCosts.add(shipping1);
		shippingCosts.add(shipping2);
		return shippingCosts;
	}

}
