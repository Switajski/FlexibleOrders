package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	private OrderItemRepository itemRepo;

	@Autowired
	public InvoiceServiceImpl(OrderItemRepository itemRepo){
		this.itemRepo = itemRepo;
	}

	@Override
	public List<DeliveryNotes> findByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findInvoiceNumbersLike(Long invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Long> getInvoiceNumbersByCustomer(Customer customer,
			PageRequest pageRequest) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
}
