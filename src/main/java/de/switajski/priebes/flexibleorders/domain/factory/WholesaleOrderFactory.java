package de.switajski.priebes.flexibleorders.domain.factory;

import org.springframework.util.Assert;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.policy.AllowAllOrderPolicy;
import de.switajski.priebes.flexibleorders.policy.OrderPolicy;

/**
 * creates an order with items and an handling events
 *  
 * @author Marek Switajski
 *
 */
public class WholesaleOrderFactory {
	
	private static OrderPolicy orderPolicy = new AllowAllOrderPolicy();
	
	/**
	 * 
	 * @param customer must not be null
	 * @param orderNumber must not be null
	 * @param product must not be null
	 * @param quantity greater than 0
	 * @return null if an (order) item cannot be created
	 */
	public static Item createItem(
			Customer customer,
			Long orderNumber,
			CatalogProduct product,
			Integer quantity){
		Assert.notNull(product);
		Assert.notNull(customer);
		Assert.isTrue(quantity > 0);
				
		OrderedSpecification orderedSpec = new OrderedSpecification();
		orderedSpec.setCustomerEmail(customer.getEmail());
		orderedSpec.setOrderNumber(orderNumber);
		orderedSpec.setProductName(product.getName());
		orderedSpec.setQuantity(quantity);
		orderedSpec.setProductNumber(product.getProductNumber());
		
		return createItem(customer, orderedSpec);
	}

	public static Item createItem(Customer customer, OrderedSpecification orderedSpec) {
		if (orderPolicy.isAllowed(customer)){
			Item item = new Item();
			item.setOrderedSpecification(orderedSpec);
			if (orderedSpec.isSatisfiedBy(item))
			return item;
			else return null;
		}
		
		return null;
	}
	
}
