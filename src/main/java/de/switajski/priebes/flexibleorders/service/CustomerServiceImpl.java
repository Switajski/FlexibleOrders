package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

@Service
@Transactional
public class CustomerServiceImpl 
extends JpaRepositoryToServiceAdapter<Customer> implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	private ArchiveItemRepository archiveItemRepository;
	private ShippingItemRepository shippingItemRepository;
	private OrderItemRepository orderItemRepository;
	private InvoiceItemRepository invoiceItemRepository;

	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository,
			OrderItemRepository orderItemRepo,
			InvoiceItemRepository invoiceItemRepo,
			ShippingItemRepository shippingItemRepo,
			ArchiveItemRepository archiveItemRepo) {
		super(customerRepository);
		this.customerRepository = customerRepository;
		this.invoiceItemRepository = invoiceItemRepo;
		this.orderItemRepository = orderItemRepo;
		this.shippingItemRepository = shippingItemRepo;
		this.archiveItemRepository = archiveItemRepo;
		
	}
	
	//TODO: Delete all SpringRoo generated methods with *Customer*
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

	@Override
	public Page<OrderItem> findOpenOrderItems(Customer customer, Pageable pageable) {
		return orderItemRepository.findByCustomerAndOpen(customer, pageable);
	}

	@Override
	public Page<ShippingItem> findOpenShippingItems(Customer customer, Pageable pageable) {
		return shippingItemRepository.findByCustomerAndOpen(customer, pageable);
	}

	@Override
	public Page<InvoiceItem> findOpenInvoiceItems(Customer customer, Pageable pageable) {
		return invoiceItemRepository.findByCustomerAndOpen(customer, pageable);
	}

	@Override
	public Page<ArchiveItem> findOpenArchiveItems(Customer customer, Pageable pageable) {
		return archiveItemRepository.findByCustomerAndCompleted(customer, pageable);
	}

	@Override
	public List<OrderItem> findOpenOrderItems(Customer customer) {
		return orderItemRepository.findByCustomerAndOpen(customer);
	}

	@Override
	public List<ShippingItem> findOpenShippingItems(Customer customer) {
		return shippingItemRepository.findByCustomerAndOpen(customer);
	}

	@Override
	public List<InvoiceItem> findOpenInvoiceItems(Customer customer) {
		return invoiceItemRepository.findByCustomerAndOpen(customer);
	}

	@Override
	public List<ArchiveItem> findOpenArchiveItems(Customer customer) {
		return archiveItemRepository.findByCustomerAndOpen(customer);
	}

}
