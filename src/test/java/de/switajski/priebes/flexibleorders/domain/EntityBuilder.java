package de.switajski.priebes.flexibleorders.domain;

import java.math.BigDecimal;
import java.util.Date;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.ProductType;

/**
 * Created for Unit Testing. Returns ready to use Entities with its given 
 * dependencies. The Entities are not persisted (Unit Tests).
 * 
 * @author Marek
 *
 */
public class EntityBuilder {

	public EntityBuilder() {
		// TODO implement builder pattern
		// TODO create as Spring Context
	}
	
	public OrderItem getOrderItem(Double nr, Long orderNumber){
		Customer customer = getCustomer();
		Product product = getProduct(nr);
		
		OrderItem oi = new OrderItem();
		oi.setCreated(new Date());
		oi.setCustomer(customer);
		oi.setOrderNumber(orderNumber);
		oi.setProduct(product);
		oi.setQuantity(10);
		oi.setQuantityLeft(10);
		return oi;
	}

	public ShippingItem getShippingItem(Double nr, Long orderNumber, Long orderConfirmationNumber){
		OrderItem oi = getOrderItem(nr, orderNumber);
		ShippingItem si = oi.confirm(false, 10, orderConfirmationNumber);
		return si;
	}
	
	public InvoiceItem getInvoiceItem(Double nr, Long orderNumber, Long orderConfirmationNumber, Long invoiceNumber){
		ShippingItem si = getShippingItem(nr, orderNumber, orderConfirmationNumber);
		InvoiceItem i = si.deliver(10, invoiceNumber);
		return i;
	}
	
	public ArchiveItem getArchiveItem(Double nr, Long orderNumber, Long orderConfirmationNumber, Long invoiceNumber, Long accountNumber){
		InvoiceItem i = getInvoiceItem(nr, orderNumber, orderConfirmationNumber, invoiceNumber);
		ArchiveItem ai = i.complete(10, accountNumber);
		return ai;
	}
	
	public Product getProduct(Double doubleNr) {
		Product p = new Product();
		p.setActive(true);
		p.setCategory(getCategory("alle auf einmal"));
		p.setDescription("tolles Produkt");
		p.setImage("asdf.jpg");
		p.setName("antilotte "+doubleNr.toString());
		p.setPriceNet(new BigDecimal(doubleNr));
		p.setProductNumber(doubleNr.longValue());
		p.setProductType(ProductType.PRODUCT);
		return p;
	}

	public Category getCategory(String name) {
		Category c = new Category();
		c.setActivated(true);
		c.setName(name);
		c.setImage(name + ".jpg");
		return c;
	}

	public Customer getCustomer(){
		Customer customer = new Customer();
		customer.setCity("Kulmbach");
		customer.setCountry(Country.GERMANY);
		customer.setCreated(new Date());
		customer.setEmail("marek@switajski.de");
		customer.setName1("Marek");
		customer.setName2("Switajski");
		customer.setPhone("134562346");
		customer.setPostalCode(95326);
		customer.setShortName("ms");
		customer.setStreet("Gasfabrikg‰ﬂchen 10");
		return customer;
	}
}
