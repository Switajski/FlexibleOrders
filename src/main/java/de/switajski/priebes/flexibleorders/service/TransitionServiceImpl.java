package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.NotFoundException;

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
			OrderItemService orderItemService) {
		this.orderItemRepository = orderItemRepo;
		this.shippingItemRepository = shippingItemRepo;
		this.invoiceItemRepository = invoiceItemRepo;
		this.customerService = customerService;
		this.archiveItemRepository = archiveItemRepository;
		this.orderItemService = orderItemService;
	}
	
	@Override
	public ShippingItem confirm(long orderNumber, Product product,
			int quantity, boolean toSupplier, long orderConfirmationNumber) {
		List<OrderItem> orderItems = 
				orderItemRepository.findByOrderNumberAndProduct(orderNumber, product);
		if (orderItems.size()>1) 
			throw new IllegalStateException("Order has order items with same products");
		OrderItem oiToConfirm = orderItems.get(0);
		ShippingItem si = oiToConfirm.confirm(toSupplier, quantity, orderConfirmationNumber);
		
		orderItemRepository.saveAndFlush(oiToConfirm);
		shippingItemRepository.saveAndFlush(si);
		return si;
	}

	@Override
	public InvoiceItem deliver(long orderConfirmationNumber, Product product,
			int quantity, long invoiceNumber, String trackNumber,
			String packageNumber) {
		List<ShippingItem> shippingItems = 
				shippingItemRepository.findByOrderNumberAndProduct(orderConfirmationNumber, product);
		if (shippingItems.size()>1) 
			throw new IllegalStateException("Order confirmation has shipping items with same products");
		ShippingItem siToConfirm = shippingItems.get(0);
		InvoiceItem ii = siToConfirm.deliver(quantity, invoiceNumber);
		
		shippingItemRepository.saveAndFlush(siToConfirm);
		invoiceItemRepository.saveAndFlush(ii);
		return ii;

	}

	@Override
	public InvoiceItem withdraw(long orderConfirmationNumber, Product product,
			long invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchiveItem complete(long invoiceNumber, Product product,
			int quantity, long accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchiveItem decomplete(long invoiceNumber, Product product,
			long accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShippingItem deconfirm(long orderNumber, Product product,
			long orderConfirmationNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
