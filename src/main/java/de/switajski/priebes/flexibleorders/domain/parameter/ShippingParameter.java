package de.switajski.priebes.flexibleorders.domain.parameter;

/**
 * encapsulates and validates parameters for the "deliver" transition 
 * {@link ItemTransition#deliver}
 * @author Marek Switajski
 *
 */
public class ShippingParameter {

	public int quantity;

	private long invoiceNumber;
	private Address address;
	
	/**
	 * TODO: add TrackNumber and PackageNumber to parameters
	 * @param quantity how many pieces should be shipped
	 * @param invoiceNumber the number of the created invoice
	 * @param shippingDestinationAddress
	 */
	public ShippingParameter(int quantity,
			long invoiceNumber,
			Address shippingDestinationAddress) {
		if (quantity < 1)
			throw new IllegalArgumentException("quantity cannot be less than 1");
		if (invoiceNumber < 1l)
			throw new IllegalArgumentException("invoice number must be more than 1");
		this.quantity = quantity;
		this.invoiceNumber = invoiceNumber;
		this.address = shippingDestinationAddress;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public long getInvoiceNumber() {
		return invoiceNumber;
	}
	
	public Address getAddress(){
		return address;
	}
}
