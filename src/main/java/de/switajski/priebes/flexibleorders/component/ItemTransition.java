package de.switajski.priebes.flexibleorders.component;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.AccountParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.Address;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ShippingParameter;
import de.switajski.priebes.flexibleorders.reference.Status;

/**
 * Class to handle and verify transitions of a specific item. A Transition 
 * is an action in the order process. Therefore specific items can be
 * removed or added. 
 * @author Marek Switajski
 *
 */
public class ItemTransition {

	/**
	 * this method represents the second transistion of the order process.
	 * The first one is to create an order item: </br>
	 * 1. new Orderitem </br>
	 * 2. orderItem.confirm </br>
	 * 3. shippingItem.deliver </br>
	 * ...
	 *
	 * @param toSupplier
	 * @return
	 */
	public ShippingItem confirm(OrderItem orderItem, 
			ConfirmationParameter confirmationParameter) {
		if (isConfirmable(orderItem)){

			orderItem.setOrderConfirmationNumber(
					confirmationParameter.getOrderConfirmationNumber());

			ShippingItem si = createShippingItem(confirmationParameter, orderItem);
			orderItem.addConfirmedQuantity(orderItem.getQuantity());		

			return si;
		} else 
			return null;
	}


	//TODO: Exception Handling
	private boolean isConfirmable(OrderItem orderItem2) {
		if (orderItem2== null) return false;
		if (orderItem2.getStatus() == Status.ORDERED)
			return true;
		else return false;

	}


	private ShippingItem createShippingItem(
			ConfirmationParameter cp, OrderItem orderItem) {

		ShippingItem si = new ShippingItem();
		si.setOrderConfirmationNumber(cp.orderConfirmationNumber);
		si.historize(orderItem);
		si.setTransmitToSupplier(cp.isTransmitToSupplier());
		si.setCreated(new Date());
		si.setQuantity(orderItem.getQuantity());
		si.setQuantityLeft(orderItem.getQuantity());
		Customer customer = orderItem.getCustomer();
		//TODO: Create @Embedded class shippingAddress
		si.setShippingAddress(new Address(
				customer.getName1(),
				customer.getName2(),
				customer.getStreet(),
				customer.getPostalCode(),
				customer.getCity(),
				customer.getCountry()
				));

		return si;
	}



	/**
	 * creates an invoice item by delivering a shipping item
	 * @param shippingItem2 the shipping item which has to be delivered
	 * @param shippingParameter the shipping information
	 * @return
	 */
	public InvoiceItem deliver(ShippingItem shippingItem2,
			ShippingParameter shippingParameter) {
		if (!isDeliverable(shippingItem2))
				return null;
		else {

			shippingItem2.setInvoiceNumber(shippingParameter.getInvoiceNumber());
			InvoiceItem ii = createInvoiceItem(shippingItem2, shippingParameter);
			shippingItem2.addShippedQuantity(shippingParameter.getQuantity());

			return ii;
		}
	}


	private boolean isDeliverable(ShippingItem shippingItem2) {
		if (shippingItem2 == null) return false;
		if (shippingItem2.getOrderConfirmationNumber() == null) return false;
		return true;
	}


	private InvoiceItem createInvoiceItem(ShippingItem shippingItem, ShippingParameter shippingParameter) {

		InvoiceItem ii = new InvoiceItem();
		ii.setInvoiceNumber(shippingParameter.getInvoiceNumber());
		ii.historize(shippingItem);
		ii.setCreated(new Date());
		ii.setQuantity(shippingParameter.getQuantity());
		ii.setQuantityLeft(shippingParameter.getQuantity());
		Customer customer = shippingItem.getCustomer();
		ii.setInvoiceCity(customer.getCity());
		ii.setInvoiceCountry(customer.getCountry());
		ii.setInvoiceName1(customer.getName1());
		ii.setInvoiceName2(customer.getName2());
		ii.setInvoicePostalCode(customer.getPostalCode());
		ii.setInvoiceStreet(customer.getStreet());

		return ii;

	}

	/**
	 * The only way to create a ArchiveItem is to generate it from a IvoiceItem.
	 * @param invoiceItem
	 * @param accountParameter
	 * @return
	 */
	public ArchiveItem complete(InvoiceItem invoiceItem, AccountParameter accountParameter){
		invoiceItem.setAccountNumber(accountParameter.getAccountNumber());
		ArchiveItem ai = createArchiveItem(invoiceItem, accountParameter);
		invoiceItem.addCompletedQuantity(invoiceItem.getQuantity());
		return ai;
	}


	private ArchiveItem createArchiveItem(InvoiceItem invoiceItem,
			AccountParameter accountParameter) {

		// reduce the whole QuantityLeft
		int quantity = invoiceItem.getQuantity();

		ArchiveItem ai = new ArchiveItem();
		ai.setAccountNumber(accountParameter.getAccountNumber());
		ai.historize(invoiceItem);
		ai.setCreated(new Date());
		ai.setQuantity(quantity);
		ai.setQuantityLeft(quantity);
		Customer customer = invoiceItem.getCustomer();
		ai.setInvoiceCity(customer.getCity());
		ai.setInvoiceCountry(customer.getCountry());
		ai.setInvoiceName1(customer.getName1());
		ai.setInvoiceName2(customer.getName2());
		ai.setInvoicePostalCode(customer.getPostalCode());
		ai.setInvoiceStreet(customer.getStreet());
		ai.setShippingCity(customer.getCity());
		ai.setShippingCountry(customer.getCountry());
		ai.setShippingName1(customer.getName1());
		ai.setShippingName2(customer.getName2());
		ai.setShippingPostalCode(customer.getPostalCode());
		ai.setShippingStreet(customer.getStreet());
		ai.setAnNaeherei(false);
		return ai;
	}


	/**
	 * Rolls back the item transition "confirm"
	 * @param siToDelete
	 * @param orderItemToDeconfirm
	 */
	public void deconfirm(ShippingItem siToDelete,
			OrderItem orderItemToDeconfirm) {
		
		orderItemToDeconfirm.reduceConfirmedQuantity(siToDelete.getQuantity());
		orderItemToDeconfirm.setOrderConfirmationNumber(null);
		
		
	}

	/**
	 * Rolls back the item transition "deliver". Given roll back has two use cases: 
	 * </br>
	 * 1. One shipping item is completely shipped by one invoice item.</br>
     * 2. One shipping item is shipped by several invoice items. 
	 * @param shippingItemToWithdraw
	 * @param invoiceItemToDelete
	 */
	public void withdraw(ShippingItem shippingItemToWithdraw, 
			InvoiceItem invoiceItemToDelete) {
		shippingItemToWithdraw.reduceShippedQuantity(
				invoiceItemToDelete.getQuantity());		
		
		//for the second use case:
		if (shippingItemToWithdraw.getQuantity() == shippingItemToWithdraw.getQuantityLeft())
			shippingItemToWithdraw.setInvoiceNumber(null);
		
	}


	/**
	 * Rolls back the item transition "complete"
	 * @param archiveItemToDelete
	 * @param invoiceItemToDecomplete
	 */
	public void decomplete(ArchiveItem archiveItemToDelete, InvoiceItem invoiceItem) {
		invoiceItem.reduceCompletedQuantity(invoiceItem.getQuantity());
		invoiceItem.setAccountNumber(null);
	}
	
}
