package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl extends JpaRepositoryToServiceAdapter<Customer> implements CustomerService {

	@Autowired
	public CustomerServiceImpl(CustomerRepository jpaRepository) {
		super(jpaRepository);
	}

	@Autowired
    CustomerRepository customerRepository;

	public long countAllCustomers() {
        return customerRepository.count();
    }

	public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

	public Customer findCustomer(Long id) {
        return customerRepository.findOne(id);
    }

	public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

	public List<Customer> findCustomerEntries(int firstResult, int maxResults) {
        return customerRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

	public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
