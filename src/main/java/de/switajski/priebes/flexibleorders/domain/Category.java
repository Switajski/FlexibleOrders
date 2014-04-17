package de.switajski.priebes.flexibleorders.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Category extends GenericEntity {

	@OneToMany
	private Set<Category> childCategories;

	@NotNull
	@Column(unique = true)
	private String name;

	private String intro;

	private String description;

	private String image;

	private String imageGalery;

	private int sortOrder;

	@NotNull
	private Boolean activated;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageGalery() {
		return this.imageGalery;
	}

	public void setImageGalery(String imageGalery) {
		this.imageGalery = imageGalery;
	}

	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean getActivated() {
		return this.activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(
				this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public Set<Category> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(Set<Category> childCategories) {
		this.childCategories = childCategories;
	}
}
