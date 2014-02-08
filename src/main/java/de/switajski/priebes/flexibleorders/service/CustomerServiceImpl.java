package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;

@Service
public class CustomerServiceImpl {

	private OrderItemRepository itemRepo;
	private CustomerRepository customerRepo;

	@Autowired
	public CustomerServiceImpl(OrderItemRepository itemRepository, CustomerRepository customerRepo) {
		itemRepo = itemRepository;
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

}
