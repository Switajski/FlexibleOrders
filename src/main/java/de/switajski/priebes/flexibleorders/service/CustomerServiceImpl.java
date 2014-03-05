package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

@Service
public class CustomerServiceImpl {

	private CustomerRepository customerRepo;

	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	
	@Transactional
	public Customer create(Customer customer){
		return customerRepo.save(customer);
	}
	
	public List<Customer> findAll() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Transactional
	public void delete(Long customerNumber) {
		Customer customer = customerRepo.findByCustomerNumber(customerNumber);
		if (customer == null) 
			throw new IllegalArgumentException("Kundennr. nicht gefunden");
		customerRepo.delete(customer);
	}

}
