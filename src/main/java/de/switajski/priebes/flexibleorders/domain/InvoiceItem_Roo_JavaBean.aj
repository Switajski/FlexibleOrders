// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.reference.Country;

privileged aspect InvoiceItem_Roo_JavaBean {
    
    public String InvoiceItem.getInvoiceName1() {
        return this.invoiceName1;
    }
    
    public void InvoiceItem.setInvoiceName1(String invoiceName1) {
        this.invoiceName1 = invoiceName1;
    }
    
    public String InvoiceItem.getInvoiceName2() {
        return this.invoiceName2;
    }
    
    public void InvoiceItem.setInvoiceName2(String invoiceName2) {
        this.invoiceName2 = invoiceName2;
    }
    
    public String InvoiceItem.getInvoiceStreet() {
        return this.invoiceStreet;
    }
    
    public void InvoiceItem.setInvoiceStreet(String invoiceStreet) {
        this.invoiceStreet = invoiceStreet;
    }
    
    public String InvoiceItem.getInvoiceCity() {
        return this.invoiceCity;
    }
    
    public void InvoiceItem.setInvoiceCity(String invoiceCity) {
        this.invoiceCity = invoiceCity;
    }
    
    public int InvoiceItem.getInvoicePostalCode() {
        return this.invoicePostalCode;
    }
    
    public void InvoiceItem.setInvoicePostalCode(int invoicePostalCode) {
        this.invoicePostalCode = invoicePostalCode;
    }
    
    public Country InvoiceItem.getInvoiceCountry() {
        return this.invoiceCountry;
    }
    
    public void InvoiceItem.setInvoiceCountry(Country invoiceCountry) {
        this.invoiceCountry = invoiceCountry;
    }
    
}
