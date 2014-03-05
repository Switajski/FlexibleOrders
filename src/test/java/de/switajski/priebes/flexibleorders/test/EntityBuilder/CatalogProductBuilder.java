package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;

public class CatalogProductBuilder implements Builder<CatalogProduct> {
	
	private Long id;
    private Long productNumber;
    private ProductType productType;
    private String name;
    private Amount recommendedPriceNet;
	private boolean active;
	private String imageGalery;
	private Long sortOrder;

	
    /**
     * 
     * @param category
     * @param name
     * @param productNumber
     * @param productType
     * @param active
     */
	public CatalogProductBuilder(
			String name,
			Long productNumber, 
			ProductType productType){
		this.productNumber = productNumber;
		this.productType = productType;
		this.name = name;
	};
	
	public static CatalogProduct buildWithGeneratedAttributes(Integer i){
		return new CatalogProductBuilder(
				"name".concat(i.toString()),
				i.longValue(), 
				ProductType.PRODUCT)
		.generateAttributes(i).build();
	}
	
	public CatalogProductBuilder generateAttributes(Integer i){
	    productType = ProductType.PRODUCT;
	    name = "product";
	    recommendedPriceNet = new Amount(new BigDecimal(i), Currency.EUR);
		productNumber = i.longValue();
		name = name.concat(i.toString());
		return this;
	}
	
	public CatalogProductBuilder setId(Long id){
		this.id = id;
		return this;
	}
	
	public CatalogProduct build(){
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
		
	public CatalogProductBuilder setProductNumber(Long productNumber) {
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

	public CatalogProductBuilder setRecommendedPriceNet(Amount recommendedPriceNet) {
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

}
