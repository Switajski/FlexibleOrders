package de.switajski.priebes.flexibleorders.domain;

import java.math.BigDecimal;
import java.util.Date;

import de.switajski.priebes.flexibleorders.component.ItemTransition;
import de.switajski.priebes.flexibleorders.domain.parameter.AccountParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ShippingParameter;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.OrderItemBuilder;

/**
 * Created for Unit Testing. Returns ready to use Entities with its given 
 * dependencies. The Entities are not persisted (Unit Tests).
 * 
 * @author Marek
 *
 */
public class EntityBuilder {

	public ShippingItem getShippingItem(Double nr, Long orderNumber, Long orderConfirmationNumber){
		OrderItemBuilder oib = new OrderItemBuilder(); 
		OrderItem oi = oib.build();		
		ConfirmationParameter parameter = new ConfirmationParameter(false, orderConfirmationNumber);
		ItemTransition it = new ItemTransition();
		ShippingItem si = it.confirm(oi, parameter);
		return si;
	}
	
	public InvoiceItem getInvoiceItem(Double nr, Long orderNumber, Long orderConfirmationNumber, Long invoiceNumber){
		ShippingItem si = getShippingItem(nr, orderNumber, orderConfirmationNumber);
		InvoiceItem i = new ItemTransition().
				deliver(
						si, 
						new ShippingParameter(
								10, 
								invoiceNumber, 
								si.getCustomer().getShippingAddress())
						);
		return i;
	}
	
	public ArchiveItem getArchiveItem(Double nr, Long orderNumber, Long orderConfirmationNumber, Long invoiceNumber, Long accountNumber){
		InvoiceItem i = getInvoiceItem(nr, orderNumber, orderConfirmationNumber, invoiceNumber);
		ArchiveItem ai = new ItemTransition().
				complete(i, new AccountParameter(accountNumber));
		return ai;
	}
	
}
