package de.switajski.priebes.flexibleorders.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Item;

public abstract class ItemSpecification implements Specification<Item>{

	abstract boolean isSatisfiedBy(Item item);
	
	protected int getHandledQuantityFromEvents(Item item, HandlingEventType type) {
		int summedQuan = 0;
		for (HandlingEvent orderEvent: item.getDeliveryHistory().getHandlingEvents()){
			if (orderEvent.getType() == type)
				summedQuan += orderEvent.getQuantity();
		}
		return summedQuan;
	}
}
