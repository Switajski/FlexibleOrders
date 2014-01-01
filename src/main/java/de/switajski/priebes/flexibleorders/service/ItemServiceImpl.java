package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;

public class ItemServiceImpl {

	private ItemRepository itemRepo;

	@Autowired
	public ItemServiceImpl(ItemRepository itemRepo) {
		this.itemRepo = itemRepo;
	}
	
	public Page<Item> retrieveAllToBeShipped(Customer customer, Pageable pageable){
//		itemRepo.findAll(new ConfirmedSpecification());
		return null;
	}
	
	public Page<Item> retrieveAllToBeConfirmed(Customer customer, Pageable pageable){
		return null;
	}
	
	public Page<Item> retrieveAllCompleted(Customer customer, Pageable pageable){
		return null;
	}
	
	public Page<Item> retrieveAllDue(Customer customer, Pageable pageable){
		return null;
	}

	//TODO: move to ReportService
	public List<Item> findByOrderConfirmationNumber(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Long> findOrderNumbersLike(Long orderNumberObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Long> getOrderNumbersByCustomer(Customer customer,
			PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Item> findByOrderNumber(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Long> getAllOrderNumbers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Item> findByInvoiceNumber(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
