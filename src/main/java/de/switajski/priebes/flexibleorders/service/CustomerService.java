package de.switajski.priebes.flexibleorders.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.service.RooService;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.Customer.class })
public interface CustomerService extends CrudServiceAdapter<Customer> {

	public abstract long countAllCustomers();


	public abstract void deleteCustomer(Customer customer);


	public abstract Customer findCustomer(Long id);


	public abstract List<Customer> findAllCustomers();


	public abstract List<Customer> findCustomerEntries(int firstResult, int maxResults);


	public abstract void saveCustomer(Customer customer);


	public abstract Customer updateCustomer(Customer customer);
	
	
	public abstract Page<OrderItem> findOrderedItems(Customer customer, Pageable pageable);
	public abstract Page<ShippingItem> findConfirmedItems(Customer customer, Pageable pageable);
	public abstract Page<InvoiceItem> findShippedItems(Customer customer, Pageable pageable);
	public abstract Page<ArchiveItem> findCompletedItems(Customer customer, Pageable pageable);

	public abstract List<OrderItem> findOrderedItems(Customer customer);
	public abstract List<ShippingItem> findConfirmedItems(Customer customer);
	public abstract List<InvoiceItem> findShippedItems(Customer customer);
	public abstract List<ArchiveItem> findCompletedItems(Customer customer);

}
