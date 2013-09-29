package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.domain.Category;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.ProductType;

public class ProductBuilder implements Builder<Product> {
	
	private Long id;
    private Long productNumber = 1l;
    private ProductType productType = ProductType.PRODUCT;
    private String name = "product";
    private Boolean active = true;
    private Long sortOrder = 1l;
    private String intro = "intro";
    private String description = "description";
    private String image = "image";
    private String imageGalery = "imageGalery";
    private Category category;
    private BigDecimal priceNet = BigDecimal.ONE;
	
	public ProductBuilder(){
		CategoryBuilder builder = new CategoryBuilder();
		category = builder.build();
	};
	
	public ProductBuilder withStandardAttributes(Integer i){
		productNumber = i.longValue();
		name = name.concat(i.toString());
		sortOrder = i.longValue();
		intro = intro.concat(i.toString());
		description = description.concat(i.toString());
		image = image.concat(i.toString());
		imageGalery = imageGalery.concat(i.toString());
		priceNet = new BigDecimal(i);
		
		return this;
	}
	
	public ProductBuilder withId(Long id){
		this.id = id;
		return this;
	}
	
	public ProductBuilder withPriceNet(BigDecimal priceNet){
		this.priceNet = priceNet;
		return this;
	}
	
	public Product build(){
		Product p = new Product();
		p.setId(id);
		p.setProductNumber(productNumber);
		p.setProductType(productType);
		p.setName(name);
		p.setActive(active);
		p.setSortOrder(sortOrder);
		p.setIntro(intro);
		p.setDescription(description);
		p.setImage(image);
		p.setImageGalery(imageGalery);
		p.setCategory(category);
		p.setPriceNet(priceNet);
		return p;
		
	}

}
