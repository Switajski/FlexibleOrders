package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

@Transactional
@Service
public class TransitionServiceImpl implements TransitionService {

	private OrderItemRepository orderItemRepository;
	private ShippingItemRepository shippingItemRepository;
	private InvoiceItemRepository invoiceItemRepository;
	private CustomerService customerService;

	@Autowired
	public TransitionServiceImpl(
			OrderItemRepository orderItemRepo,
			ShippingItemRepository shippingItemRepo,
			InvoiceItemRepository invoiceItemRepo,
			CustomerService customerService) {
		this.orderItemRepository = orderItemRepo;
		this.shippingItemRepository = shippingItemRepo;
		this.invoiceItemRepository = invoiceItemRepo;
		this.customerService = customerService;
	}
	
	@Transactional
	@Override
//	@RequestMapping(value="/json", method=RequestMethod.POST)
	public List<ShippingItem> confirm(Customer customer, Product product,
			int quantity, boolean toSupplier, long orderConfirmationNumber) {
		List<ShippingItem> shippingItems = new ArrayList<ShippingItem>();
		List<OrderItem> traversedItems = new ArrayList<OrderItem>();
		
		List<OrderItem> matchingOis = new ArrayList<OrderItem>();
		for (OrderItem oi:customerService.findOrderedItems(customer))
			if (oi.getProduct().equals(product)) matchingOis.add(oi);
		
		for (OrderItem oi:matchingOis){
			traversedItems.add(oi);
			if (isQuantityExceeded(oi, quantity)){
				if (matchingOis.size() == traversedItems.size()){
					throw new IllegalArgumentException(
							"bestaetigte Menge ist zu viel!");
				}
				else if (quantityIsSufficient(quantity - oi.getQuantity(), matchingOis, traversedItems) ) {
					if (oi.getStatus()!=Status.ORDERED)
					shippingItems.add(oi.confirm(toSupplier, oi.getQuantity()));
					orderItemRepository.saveAndFlush(oi);
					
				}
			} else {
				shippingItems.add(oi.confirm(toSupplier, quantity));
				orderItemRepository.saveAndFlush(oi);
			}
		}
		
		for (ShippingItem shippingItem:shippingItems){
			shippingItemRepository.saveAndFlush(shippingItem);
		}
		return shippingItems;
	}
	
	private boolean quantityIsSufficient(int neededQuantity, List<OrderItem> matchingOis, List<OrderItem> traversedItems) {
		int availableQuantity = 0;
		for (OrderItem oi:matchingOis){
			if (!traversedItems.contains(oi))
			availableQuantity += oi.getQuantity();
		}
		return (availableQuantity >= neededQuantity);
	}

	private boolean isQuantityExceeded(Item oi, int quantity) {
		return (oi.getQuantity()>quantity);
	}

	
	@Override
	public List<InvoiceItem> deliver(Customer customer, Product product,
			int quantity, long invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArchiveItem> complete(Customer customer, Product product,
			int quantity, long accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
