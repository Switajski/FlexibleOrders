package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.Country;

public class InvoiceItemBuilder extends ItemBuilder<InvoiceItem> {

	private String invoiceName1;
	private String invoiceName2;
	private String invoiceStreet;
	private String invoiceCity;
	private int invoicePostalCode;
	private Country invoiceCountry;
	private String packageNumber;
	private String trackNumber;
	private int quantityLeft;


	/**
	 * 
	 * @param customer
	 * @param product
	 * @param orderNumber
	 * @param productNumber
	 * @param productName
	 * @param priceNet
	 * @param quantityLeft
	 * @param invoiceCountry
	 * @param invoicePostalCode
	 * @param invoiceCity
	 * @param invoiceStreet
	 */
	public InvoiceItemBuilder(Customer customer, Product product, 
			Long orderNumber, Long productNumber, 
			String productName, BigDecimal priceNet,

			int quantityLeft, Country invoiceCountry,
			int invoicePostalCode, String invoiceCity,
			String invoiceStreet
			) {

		super(customer, product, 
				orderNumber, productNumber, 
				productName, priceNet);
		this.quantityLeft = quantityLeft;
		this.invoiceCountry = invoiceCountry;
		this.invoicePostalCode = invoicePostalCode;
		this.invoiceCity = invoiceCity;
		this.invoiceStreet = invoiceStreet;
	}

	@Override
	public InvoiceItem build() {
		InvoiceItem ii = new InvoiceItem();
		setSuperAttributes(ii);
		ii.setInvoiceCity(invoiceCity);
		ii.setInvoiceCountry(invoiceCountry);
		ii.setInvoiceName1(invoiceName1);
		ii.setInvoiceName2(invoiceName2);
		ii.setInvoicePostalCode(invoicePostalCode);
		ii.setInvoiceStreet(invoiceStreet);
		ii.setPackageNumber(packageNumber);
		ii.setQuantityLeft(quantityLeft);
		ii.setTrackNumber(trackNumber);
		return null;
	}

	public InvoiceItemBuilder setInvoiceName1(String invoiceName1) {
		this.invoiceName1 = invoiceName1;
		return this;
	}

	public InvoiceItemBuilder setInvoiceName2(String invoiceName2) {
		this.invoiceName2 = invoiceName2;
		return this;
	}

	public InvoiceItemBuilder setInvoiceStreet(String invoiceStreet) {
		this.invoiceStreet = invoiceStreet;
		return this;
	}

	public InvoiceItemBuilder setInvoiceCity(String invoiceCity) {
		this.invoiceCity = invoiceCity;
		return this;
	}

	public InvoiceItemBuilder setInvoicePostalCode(int invoicePostalCode) {
		this.invoicePostalCode = invoicePostalCode;
		return this;
	}

	public InvoiceItemBuilder setInvoiceCountry(Country invoiceCountry) {
		this.invoiceCountry = invoiceCountry;
		return this;
	}

	public InvoiceItemBuilder setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
		return this;
	}

	public InvoiceItemBuilder setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
		return this;
	}

	public InvoiceItemBuilder setQuantityLeft(int quantityLeft) {
		this.quantityLeft = quantityLeft;
		return this;
	}

}
