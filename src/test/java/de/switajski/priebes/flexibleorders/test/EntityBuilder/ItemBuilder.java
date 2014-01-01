package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.OriginSystem;

public class ItemBuilder implements Builder<Item> {

	private Customer customer;
	private DeliveryHistory deliveryHistory;
	private OriginSystem originSystem;
	private OrderedSpecification orderedSpecification;
	
	@Override
	public Item build() {
		Item item = new Item();
		item.setOrderedSpecification(orderedSpecification);
		item.setCustomer(customer);
		item.setDeliveryHistory(deliveryHistory);
		item.setOriginSystem(originSystem);
		return item;
	}

	public ItemBuilder setOrderedSpecification(OrderedSpecification orderedSpecification) {
		this.orderedSpecification = orderedSpecification;
		return this;
	}

	public ItemBuilder setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public ItemBuilder setDeliveryHistory(DeliveryHistory deliveryHistory) {
		this.deliveryHistory = deliveryHistory;
		return this;
	}

	public ItemBuilder setOriginSystem(OriginSystem originSystem) {
		this.originSystem = originSystem;
		return this;
	}

}
