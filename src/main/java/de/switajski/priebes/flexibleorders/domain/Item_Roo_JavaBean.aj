// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.Item;
import java.math.BigDecimal;

privileged aspect Item_Roo_JavaBean {
    
    public int Item.getQuantity() {
        return this.quantity;
    }
    
    public void Item.setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal Item.getPriceNet() {
        return this.priceNet;
    }
    
    public void Item.setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }
    
    public String Item.getProductName() {
        return this.productName;
    }
    
    public void Item.setProductName(String productName) {
        this.productName = productName;
    }
    
    public Long Item.getProductNumber() {
        return this.productNumber;
    }
    
    public void Item.setProductNumber(Long productNumber) {
        this.productNumber = productNumber;
    }
    
    public Long Item.getOrderConfirmationNumber() {
        return this.orderConfirmationNumber;
    }
    
    public Long Item.getInvoiceNumber() {
        return this.invoiceNumber;
    }
    
    public Long Item.getAccountNumber() {
        return this.accountNumber;
    }
    
    public Long Item.getOrderNumber() {
        return this.orderNumber;
    }
    
}
