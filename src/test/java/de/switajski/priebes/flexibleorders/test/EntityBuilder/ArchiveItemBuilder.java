package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.Country;

public class ArchiveItemBuilder extends ItemBuilder<ArchiveItem> {

	private Date expectedDelivery;
	private Boolean anNaeherei;
    private String shippingName1;
    private String shippingName2;
    private String shippingStreet;
    private String shippingCity;
    private int shippingPostalCode;
    private Country shippingCountry;
    private String invoiceName1;
    private String invoiceName2;
    private String invoiceStreet;
    private String invoiceCity;
    private int invoicePostalCode;
    private Country invoiceCountry;
    private int quantityLeft;

	public ArchiveItemBuilder(Customer customer, Product product, 
			Long orderNumber, Long productNumber, 
			String productName, BigDecimal priceNet,
			
			boolean anNaeherei, String shippingStreet, 
			String shippingCity, int shippingPostalCode, 
			Country shippingCountry, String invoiceStreet, 
			String invoiceCity, int invoicePostalCode, 
			Country invoiceCountry) {
		super(customer, product, orderNumber, productNumber, productName, priceNet);
		
		this.anNaeherei = anNaeherei;
		this.shippingStreet = shippingStreet;
		this.shippingCity = shippingCity;
		this.shippingPostalCode = shippingPostalCode;
		this.shippingCountry = shippingCountry;
		this.invoiceStreet = invoiceStreet;
		this.invoicePostalCode = invoicePostalCode;
		this.invoiceCountry = invoiceCountry;
		
	}
	
	@Override
	public ArchiveItem build() {
		ArchiveItem ai = new ArchiveItem();
		setSuperAttributes(ai);
		ai.setAnNaeherei(anNaeherei);
		ai.setExpectedDelivery(expectedDelivery);
		ai.setInvoiceCity(invoiceCity);
		ai.setInvoiceCountry(invoiceCountry);
		ai.setInvoiceName1(invoiceName1);
		ai.setInvoiceName2(invoiceName2);
		ai.setInvoicePostalCode(invoicePostalCode);
		ai.setInvoiceStreet(invoiceStreet);
		ai.setQuantityLeft(quantityLeft);
		ai.setShippingCity(shippingCity);
		ai.setShippingCountry(shippingCountry);
		ai.setShippingName1(shippingName1);
		ai.setShippingName2(shippingName2);
		ai.setShippingPostalCode(shippingPostalCode);
		ai.setShippingStreet(shippingStreet);
		return ai;
	}
	
    public ArchiveItemBuilder setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
		return this;
	}

	public ArchiveItemBuilder setAnNaeherei(Boolean anNaeherei) {
		this.anNaeherei = anNaeherei;
		return this;
	}

	public ArchiveItemBuilder setShippingName1(String shippingName1) {
		this.shippingName1 = shippingName1;
		return this;
	}

	public ArchiveItemBuilder setShippingName2(String shippingName2) {
		this.shippingName2 = shippingName2;
		return this;
	}

	public ArchiveItemBuilder setShippingStreet(String shippingStreet) {
		this.shippingStreet = shippingStreet;
		return this;
	}

	public ArchiveItemBuilder setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
		return this;
	}

	public ArchiveItemBuilder setShippingPostalCode(int shippingPostalCode) {
		this.shippingPostalCode = shippingPostalCode;
		return this;
	}

	public ArchiveItemBuilder setShippingCountry(Country shippingCountry) {
		this.shippingCountry = shippingCountry;
		return this;
	}

	public ArchiveItemBuilder setInvoiceName1(String invoiceName1) {
		this.invoiceName1 = invoiceName1;
		return this;
	}

	public ArchiveItemBuilder setInvoiceName2(String invoiceName2) {
		this.invoiceName2 = invoiceName2;
		return this;
	}

	public ArchiveItemBuilder setInvoiceStreet(String invoiceStreet) {
		this.invoiceStreet = invoiceStreet;
		return this;
	}

	public ArchiveItemBuilder setInvoiceCity(String invoiceCity) {
		this.invoiceCity = invoiceCity;
		return this;
	}

	public ArchiveItemBuilder setInvoicePostalCode(int invoicePostalCode) {
		this.invoicePostalCode = invoicePostalCode;
		return this;
	}

	public ArchiveItemBuilder setInvoiceCountry(Country invoiceCountry) {
		this.invoiceCountry = invoiceCountry;
		return this;
	}

	public ArchiveItemBuilder setQuantityLeft(int quantityLeft) {
		this.quantityLeft = quantityLeft;
		return this;
	}

}
