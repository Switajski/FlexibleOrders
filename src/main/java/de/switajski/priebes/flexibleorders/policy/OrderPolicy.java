package de.switajski.priebes.flexibleorders.policy;

import de.switajski.priebes.flexibleorders.domain.Customer;

public interface OrderPolicy {

	public boolean isAllowed(Customer customer);
}
