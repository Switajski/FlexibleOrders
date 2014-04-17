package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Category;

public class CategoryBuilder implements Builder<Category> {

	private String name;
	private String intro;
	private String description;
	private String image;
	private String imageGalery;
	private int sortOrder;
	private Boolean activated;

	public CategoryBuilder(String name, boolean activated) {
		this.name = name;
		this.activated = activated;
	}

	public static Category buildWithGeneratedAttributes(Integer i) {
		return new CategoryBuilder(
				"Category" + i.toString(), true)
				.generateAttributes(i).build();
	}

	public CategoryBuilder generateAttributes(Integer i) {
		intro = "Intro" + i.toString();
		description = "Description" + i.toString();
		image = "Image" + i.toString();
		imageGalery = "ImageGallery" + i.toString();
		sortOrder = i;
		return this;
	}

	@Override
	public Category build() {
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

	public CategoryBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public CategoryBuilder setIntro(String intro) {
		this.intro = intro;
		return this;
	}

	public CategoryBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public CategoryBuilder setImage(String image) {
		this.image = image;
		return this;
	}

	public CategoryBuilder setImageGalery(String imageGalery) {
		this.imageGalery = imageGalery;
		return this;
	}

	public CategoryBuilder setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public CategoryBuilder setActivated(Boolean activated) {
		this.activated = activated;
		return this;
	}
}
