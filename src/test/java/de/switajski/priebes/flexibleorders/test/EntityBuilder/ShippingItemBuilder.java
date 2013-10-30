package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.Address;
import de.switajski.priebes.flexibleorders.reference.Country;


public class ShippingItemBuilder extends ItemBuilder<ShippingItem>{

	
	private Date expectedDelivery;
	private Integer quantityLeft;
	private Boolean transmitToSupplier;
	private String shippingName1;
	private String shippingName2;
	private String shippingStreet;
	private String shippingCity;
	private int shippingPostalCode;
	private Country shippingCountry;


	public ShippingItemBuilder(
			Customer customer, Product product, 
			Long orderNumber, Long productNumber, 
			String productName, BigDecimal priceNet,
			
			Date expectedDelivery,
			boolean transmitToSupplier,
			String shippingStreet,
			String shippingCity,
			Country shippingCountry) {
		
		super(	customer, product, 
				orderNumber, productNumber, 
				productName, priceNet);
		this.expectedDelivery = expectedDelivery;
		this.transmitToSupplier = transmitToSupplier;
		this.shippingStreet = shippingStreet;
		this.shippingCity = shippingCity;
		this.shippingCountry = shippingCountry;

	}

	@Override
	public ShippingItem build() {
		ShippingItem si = new ShippingItem();
		setSuperAttributes(si);
		si.setExpectedDelivery(expectedDelivery);
		si.setQuantityLeft(quantityLeft);
		si.setTransmitToSupplier(transmitToSupplier);
		si.setShippingAddress(new Address(
					shippingName1, 
					shippingName2, 
					shippingStreet, 
					shippingPostalCode, 
					shippingCity,
					shippingCountry
				));
		return si;
	}

	public ShippingItemBuilder setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
		return this;
	}

	public ShippingItemBuilder setQuantityLeft(Integer quantityLeft) {
		this.quantityLeft = quantityLeft;
		return this;
	}

	public ShippingItemBuilder setTransmitToSupplier(Boolean transmitToSupplier) {
		this.transmitToSupplier = transmitToSupplier;
		return this;
	}

	public ShippingItemBuilder setShippingName1(String shippingName1) {
		this.shippingName1 = shippingName1;
		return this;
	}

	public ShippingItemBuilder setShippingName2(String shippingName2) {
		this.shippingName2 = shippingName2;
		return this;
	}

	public ShippingItemBuilder setShippingStreet(String shippingStreet) {
		this.shippingStreet = shippingStreet;
		return this;
	}

	public ShippingItemBuilder setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
		return this;
	}

	public ShippingItemBuilder setShippingPostalCode(int shippingPostalCode) {
		this.shippingPostalCode = shippingPostalCode;
		return this;
	}

	public ShippingItemBuilder setShippingCountry(Country shippingCountry) {
		this.shippingCountry = shippingCountry;
		return this;
	}


}
