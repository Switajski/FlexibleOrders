// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.ProductType;

privileged aspect Product_Roo_JavaBean {
    
    public Long Product.getProductNumber() {
        return this.productNumber;
    }
    
    public void Product.setProductNumber(Long productNumber) {
        this.productNumber = productNumber;
    }
    
    public ProductType Product.getProductType() {
        return this.productType;
    }
    
    public void Product.setProductType(ProductType productType) {
        this.productType = productType;
    }
    
    public String Product.getName() {
        return this.name;
    }
    
    public void Product.setName(String name) {
        this.name = name;
    }
    
    public Boolean Product.getActive() {
        return this.active;
    }
    
    public void Product.setActive(Boolean active) {
        this.active = active;
    }
    
    public Long Product.getSortOrder() {
        return this.sortOrder;
    }
    
    public void Product.setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String Product.getIntro() {
        return this.intro;
    }
    
    public void Product.setIntro(String intro) {
        this.intro = intro;
    }
    
    public String Product.getDescription() {
        return this.description;
    }
    
    public void Product.setDescription(String description) {
        this.description = description;
    }
    
}
