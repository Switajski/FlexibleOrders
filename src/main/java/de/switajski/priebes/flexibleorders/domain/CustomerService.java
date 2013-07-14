package de.switajski.priebes.flexibleorders.domain;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.Customer.class })
public interface CustomerService {

	public abstract long countAllCustomers();


	public abstract void deleteCustomer(Customer customer);


	public abstract Customer findCustomer(Long id);


	public abstract List<Customer> findAllCustomers();


	public abstract List<Customer> findCustomerEntries(int firstResult, int maxResults);


	public abstract void saveCustomer(Customer customer);


	public abstract Customer updateCustomer(Customer customer);

}
