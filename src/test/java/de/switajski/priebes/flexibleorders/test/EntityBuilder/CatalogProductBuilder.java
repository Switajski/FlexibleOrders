package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.reference.ProductType;

public class CatalogProductBuilder implements Builder<CatalogProduct> {
	
	private Long id;
    private Long productNumber;
    private ProductType productType;
    private String name;
    private BigDecimal recommendedPriceNet;
	
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
	    recommendedPriceNet = BigDecimal.ONE;
		productNumber = i.longValue();
		name = name.concat(i.toString());
		return this;
	}
	
	public CatalogProductBuilder setId(Long id){
		this.id = id;
		return this;
	}
	
	public CatalogProductBuilder setRecommendedPriceNet(BigDecimal recommendedPriceNet){
		this.recommendedPriceNet = recommendedPriceNet;
		return this;
	}
	
	public CatalogProduct build(){
		CatalogProduct p = new CatalogProduct();
		p.setId(id);
		p.setProductNumber(productNumber);
		p.setProductType(productType);
		p.setName(name);
		p.setRecommendedPriceNet(recommendedPriceNet);
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

}
