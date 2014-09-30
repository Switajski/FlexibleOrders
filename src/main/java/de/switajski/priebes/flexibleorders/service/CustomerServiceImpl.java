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
	
//	@Transactional
//	public Customer update(JsonCustomer customerDto){
//		Customer customer = retrieveCustomerSavely(customerDto.getCustomerNumber());
//		
//		
//	}

	@Transactional
	public void delete(Long customerNumber) {
		Customer customer = retrieveCustomerSavely(customerNumber);
		customerRepo.delete(customer);
	}

	private Customer retrieveCustomerSavely(Long customerNumber) {
		Customer customer = customerRepo.findByCustomerNumber(customerNumber);
		if (customer == null) 
			throw new IllegalArgumentException("Kunde mit gegebener Kundennummer nicht gefunden");
		return customer;
	}

}
