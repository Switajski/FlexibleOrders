package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Category;

public class CategoryBuilder implements Builder<Category> {

	private String name = "Category";

	private String intro = "Intro";

	private String description = "Description";

	private String image = "Image";

	private String imageGalery = "ImageGallery";

	private int sortOrder = 1;

	private Boolean activated = true;
	
	public CategoryBuilder() {}
	
	public void withStandardAttributes(Integer i){
		name.concat(i.toString());
		intro.concat(i.toString());
		description.concat(i.toString());
		image.concat(i.toString());
		imageGalery.concat(i.toString());
		sortOrder = i;
	}

	public CategoryBuilder withActivated(boolean active){
		this.activated = active;
		return this;
	}
	
	@Override
	public Category build(){
		Category c = new Category();
		c.setName(name);
		c.setIntro(intro);
		c.setDescription(description);
		c.setImage(image);
		c.setImageGalery(imageGalery);
		c.setSortOrder(sortOrder);
		c.setActivated(activated);
		return c;
	}
}
