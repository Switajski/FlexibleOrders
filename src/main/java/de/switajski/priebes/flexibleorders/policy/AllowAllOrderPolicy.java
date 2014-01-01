package de.switajski.priebes.flexibleorders.policy;

import de.switajski.priebes.flexibleorders.domain.Customer;

public class AllowAllOrderPolicy implements OrderPolicy{

	@Override
	public boolean isAllowed(Customer customer) {
		if (customer.getAddress() == null) return false;
		return true;
	}

}
