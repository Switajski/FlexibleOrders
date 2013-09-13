package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
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
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;
import de.switajski.priebes.flexibleorders.web.JsonController;

@Transactional
@Service
public class TransitionServiceImpl implements TransitionService {

	private OrderItemRepository orderItemRepository;
	private ShippingItemRepository shippingItemRepository;
	private InvoiceItemRepository invoiceItemRepository;
	private CustomerService customerService;
	private ArchiveItemRepository archiveItemRepository;
	private OrderItemService orderItemService;
	private static Logger log = Logger.getLogger(JsonController.class);

	@Autowired
	public TransitionServiceImpl(
			OrderItemRepository orderItemRepo,
			ShippingItemRepository shippingItemRepo,
			InvoiceItemRepository invoiceItemRepo,
			CustomerService customerService,
			ArchiveItemRepository archiveItemRepository,
			OrderService orderService,
			OrderItemService orderItemService) {
		this.orderItemRepository = orderItemRepo;
		this.shippingItemRepository = shippingItemRepo;
		this.invoiceItemRepository = invoiceItemRepo;
		this.customerService = customerService;
		this.archiveItemRepository = archiveItemRepository;
		this.orderItemService = orderItemService;
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
					shippingItems.add(oi.confirm(toSupplier, oi.getQuantity(), orderConfirmationNumber));
					orderItemRepository.saveAndFlush(oi);
					
				}
			} else {
				shippingItems.add(oi.confirm(toSupplier, quantity, orderConfirmationNumber));
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
	
	private boolean quantityIsSufficient2(int neededQuantity, List<ShippingItem> matchingOis, List<ShippingItem> traversedItems) {
		int availableQuantity = 0;
		for (ShippingItem oi:matchingOis){
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
		List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
		List<ShippingItem> traversedItems = new ArrayList<ShippingItem>();
		
		List<ShippingItem> matchingOis = new ArrayList<ShippingItem>();
		for (ShippingItem oi:customerService.findConfirmedItems(customer))
			if (oi.getProduct().equals(product)) matchingOis.add(oi);
		
		for (ShippingItem oi:matchingOis){
			traversedItems.add(oi);
			if (isQuantityExceeded(oi, quantity)){
				if (matchingOis.size() == traversedItems.size()){
					throw new IllegalArgumentException(
							"bestaetigte Menge ist zu viel!");
				}
				else if (quantityIsSufficient2(quantity - oi.getQuantity(), matchingOis, traversedItems) ) {
					if (oi.getStatus()!=Status.CONFIRMED)
						invoiceItems.add(oi.deliver(oi.getQuantity(), invoiceNumber));
					shippingItemRepository.saveAndFlush(oi);
					
				}
			} else {
				invoiceItems.add(oi.deliver(quantity, invoiceNumber));
				shippingItemRepository.saveAndFlush(oi);
			}
		}
		
		for (InvoiceItem invoiceItem:invoiceItems){
			invoiceItemRepository.saveAndFlush(invoiceItem);
		}
		return invoiceItems;
	}

	@Override
	public List<ArchiveItem> complete(Customer customer, Product product,
			int quantity, long accountNumber) {
		List<ArchiveItem> archiveItems = new ArrayList<ArchiveItem>();
		List<InvoiceItem> traversedItems = new ArrayList<InvoiceItem>();
		
		List<InvoiceItem> matchingOis = new ArrayList<InvoiceItem>();
		for (InvoiceItem oi:customerService.findShippedItems(customer))
			if (oi.getProduct().equals(product)) matchingOis.add(oi);
		
		for (InvoiceItem oi:matchingOis){
			traversedItems.add(oi);
			if (isQuantityExceeded(oi, quantity)){
				if (matchingOis.size() == traversedItems.size()){
					throw new IllegalArgumentException(
							"bestaetigte Menge ist zu viel!");
				}
				else if (quantityIsSufficient3(quantity - oi.getQuantity(), matchingOis, traversedItems) ) {
					if (oi.getStatus()!=Status.CONFIRMED)
						archiveItems.add(oi.complete(oi.getQuantity(), accountNumber));
					invoiceItemRepository.saveAndFlush(oi);
					
				}
			} else {
				archiveItems.add(oi.complete(quantity, accountNumber));
				invoiceItemRepository.saveAndFlush(oi);
			}
		}
		
		for (ArchiveItem archiveItem:archiveItems){
			archiveItemRepository.saveAndFlush(archiveItem);
		}
		return archiveItems;
	}
	
	private boolean quantityIsSufficient3(int neededQuantity, List<InvoiceItem> matchingOis, List<InvoiceItem> traversedItems) {
		int availableQuantity = 0;
		for (InvoiceItem oi:matchingOis){
			if (!traversedItems.contains(oi))
			availableQuantity += oi.getQuantity();
		}
		return (availableQuantity >= neededQuantity);
	}

	@Override
	public ShippingItem deconfirm(Customer customer, Product product,
			long orderConfirmationNumber) {
		List<ShippingItem> siToReturn = new ArrayList<ShippingItem>();
		
		// find open shipping items
		List<ShippingItem> openShippingItems = shippingItemRepository.findByOrderConfirmationNumber(orderConfirmationNumber);
		for (ShippingItem si:openShippingItems){
			if (si.getStatus().equals(Status.CONFIRMED))
				if (si.getProduct().equals(product))
					if (si.getCustomer().equals(customer))
						siToReturn.add(si);
		}
		
		// find corresponding order items
		OrderItem oi = orderItemService.findCorresponding(openShippingItems.get(0));
		
		// set order items to ORDERED and set the order confirmation number to null
		oi.setStatus(Status.ORDERED);
		oi.setOrderConfirmationNumber(null);
		
		// delete shipping items
		for (ShippingItem si:siToReturn)
			shippingItemRepository.delete(si);
		
		return siToReturn.get(0);
	}

	@Override
	public InvoiceItem withdraw(Customer customer, Product product,
			long invoiceNumber, int quantity) {
		
		log.debug("withdrawing customer:" + customer.getId() +
				" productNumber:" + product.getProductNumber() +
				" invoiceNumber" + invoiceNumber +
				" quantity:" + quantity);
		return null;
	}

	@Override
	public ArchiveItem decomplete(Customer customer, Product product,
			long accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
