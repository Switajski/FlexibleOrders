package de.switajski.priebes.flexibleorders.domain.parameter;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * encapsulates and validates parameters for the "confirm" transition 
 * {@link ItemTransition#confirm}
 * @author Marek Switajski
 *
 */
public class ConfirmationParameter {

	public boolean transmitToSupplier;
	public long orderConfirmationNumber;
	
	public ConfirmationParameter(
			boolean transmitToSupplier,
			long orderConfirmationNumber) {
		if (orderConfirmationNumber < 1l)
			throw new IllegalArgumentException("order confirmation number must be more than 1");
		if (transmitToSupplier)
			throw new NotImplementedException();

		this.transmitToSupplier = transmitToSupplier;
		this.orderConfirmationNumber = orderConfirmationNumber;
	}

	public boolean isTransmitToSupplier() {
		return transmitToSupplier;
	}
	
	public long getOrderConfirmationNumber() {
		return orderConfirmationNumber;
	}

}
