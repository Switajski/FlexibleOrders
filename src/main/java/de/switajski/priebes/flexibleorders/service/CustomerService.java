package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;

public interface CustomerService {
	
	abstract List<Item> findToBeConfirmedItems(Customer customer);
	
	abstract Page<Item> findToBeConfirmedItems(Customer customer, Pageable pageable);

	abstract List<Customer> findAll();
	
}
