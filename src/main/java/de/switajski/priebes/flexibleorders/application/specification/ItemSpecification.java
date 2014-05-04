package de.switajski.priebes.flexibleorders.application.specification;

import org.springframework.data.jpa.domain.Specification;

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
	
}
