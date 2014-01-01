package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.specification.ToBeConfirmedSpecification;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	private ItemRepository itemRepo;

	@Autowired
	public CustomerServiceImpl(ItemRepository itemRepository) {
		itemRepo = itemRepository;
	}
	
	@Override
	public List<Item> findToBeConfirmedItems(Customer customer) {
		ToBeConfirmedSpecification spec = new ToBeConfirmedSpecification();
		//TODO: Add customer to spec
		List<Item> items = itemRepo.findAll(spec);
		return items;
	}

	@Override
	public Page<Item> findToBeConfirmedItems(Customer customer,
			Pageable pageable) {
		ToBeConfirmedSpecification spec = new ToBeConfirmedSpecification();
		//TODO: Add customer to spec
		Page<Item> items = itemRepo.findAll(spec, pageable);
		return items;
	}

	@Override
	public List<Customer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
