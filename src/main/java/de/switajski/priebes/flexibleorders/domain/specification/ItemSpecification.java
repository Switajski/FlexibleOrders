package de.switajski.priebes.flexibleorders.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

/**
 * Validates and defines states of items. Its responsibility is to identify an object that
 * satisfies it.</br>
 * <br>
 * Follows the Specification pattern by Fowler and Evans</br>
 * 
 * @see <a href="http://martinfowler.com/apsupp/spec.pdf">
 * http://martinfowler.com/apsupp/spec.pdf</a>
 * @author Marek Switajski
 *
 */
public abstract class ItemSpecification implements Specification<OrderItem>{

	public abstract boolean isSatisfiedBy(OrderItem item);
	
	public int getHandledQuantityFromEvents(OrderItem item, HandlingEventType type) {
		int summedQuan = 0;
		for (HandlingEvent orderEvent: item.getDeliveryHistory()){
			if (orderEvent.getType() == type)
				summedQuan += orderEvent.getQuantity();
		}
		return summedQuan;
	}
}
