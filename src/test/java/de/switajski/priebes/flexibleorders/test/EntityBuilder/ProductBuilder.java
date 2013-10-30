package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;

import de.switajski.priebes.flexibleorders.domain.Category;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.ProductType;

public class ProductBuilder implements Builder<Product> {
	
	private Long id;
    private Long productNumber;
    private ProductType productType;
    private String name;
    private Boolean active;
    private Long sortOrder;
    private String intro;
    private String description;
    private String image;
    private String imageGalery;
    private Category category;
    private BigDecimal priceNet;
	
	public ProductBuilder(Category category, 
			Long productNumber, 
			ProductType productType){
		this.category = category;
		this.productNumber = productNumber;
		this.productType = productType;
	};
	
	public static Product buildWithGeneratedAttributes(Integer i){
		return new ProductBuilder(
				CategoryBuilder.buildWithGeneratedAttributes(1),
				i.longValue(), 
				ProductType.PRODUCT)
		.generateAttributes(i).build();
	}
	
	public ProductBuilder generateAttributes(Integer i){
		productNumber = 1l;
	    productType = ProductType.PRODUCT;
	    name = "product";
	    active = true;
	    sortOrder = 1l;
	    intro = "intro";
	    description = "description";
	    image = "image";
	    imageGalery = "imageGalery";
	    category = CategoryBuilder.buildWithGeneratedAttributes(1);
	    priceNet = BigDecimal.ONE;
		
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
	
	public ProductBuilder setId(Long id){
		this.id = id;
		return this;
	}
	
	public ProductBuilder setPriceNet(BigDecimal priceNet){
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

	public ProductBuilder setActive(Boolean active) {
		this.active = active;
		return this;
	}

	public ProductBuilder setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public ProductBuilder setIntro(String intro) {
		this.intro = intro;
		return this;
	}

	public ProductBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public ProductBuilder setImage(String image) {
		this.image = image;
		return this;
	}

	public ProductBuilder setImageGalery(String imageGalery) {
		this.imageGalery = imageGalery;
		return this;
	}

	public ProductBuilder setCategory(Category category) {
		this.category = category;
		return this;
	}

}
