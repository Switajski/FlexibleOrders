package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
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
	private ArchiveItemRepository archiveItemRepository;
	private OrderItemService orderItemService;
	private static Logger log = Logger.getLogger(TransitionServiceImpl.class);

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
		log.debug("confirm");
		List<OrderItem> orderItems = 
				orderItemRepository.findByOrderNumberAndProduct(orderNumber, product);
		if (orderItems.size()>1) 
			throw new IllegalStateException("Order has order items with same products");
		OrderItem oiToConfirm = orderItems.get(0);
		ShippingItem si = oiToConfirm.confirm(toSupplier, quantity, orderConfirmationNumber);

		orderItemRepository.save(oiToConfirm);
		shippingItemRepository.save(si);
		return si;
	}

	@Override
	public ShippingItem deconfirm(long orderNumber, 
			Product product,
			long orderConfirmationNumber) {
		log.debug("deconfirm");
		List<ShippingItem> sis = 
				shippingItemRepository.findByOrderConfirmationNumberAndProduct(orderConfirmationNumber, product);
		List<OrderItem> ois = 
				orderItemRepository.findByOrderNumberAndProduct(orderNumber, product);

		if (sis.size()>1)
			throw new IllegalStateException("Order confirmation has shipping items with same products");
		if (sis.size()>1)
			throw new IllegalStateException("Order has order items with same products");

		ShippingItem siToDelete = sis.get(0);
		OrderItem orderItemToDeconfirm = ois.get(0);

		siToDelete.deconfirm(orderItemToDeconfirm);

		shippingItemRepository.delete(siToDelete);
		shippingItemRepository.flush();
		orderItemRepository.save(orderItemToDeconfirm);

		return sis.get(0);
	}

	@Override
	public InvoiceItem deliver(long orderConfirmationNumber, Product product,
			int quantity, long invoiceNumber, String trackNumber,
			String packageNumber) {
		log.debug("deliver");
		List<ShippingItem> shippingItems = 
				shippingItemRepository.findByOrderConfirmationNumberAndProduct(orderConfirmationNumber, product);
		if (shippingItems.size()>1) 
			throw new IllegalStateException("Order confirmation has shipping items with same products");
		ShippingItem siToConfirm = shippingItems.get(0);
		InvoiceItem ii = siToConfirm.deliver(quantity, invoiceNumber);

		shippingItemRepository.save(siToConfirm);
		invoiceItemRepository.save(ii);
		return ii;

	}

	@Override
	public InvoiceItem withdraw(InvoiceItem invoiceItem) {
		log.debug("withdraw");

		List<ShippingItem> shippingItems = 
				shippingItemRepository.findByOrderConfirmationNumberAndProduct(
						invoiceItem.getOrderConfirmationNumber(), invoiceItem.getProduct());
		
		if (shippingItems.size()!=1)
			throw new IllegalStateException("An order confirmation should have "
					+ "only one shipping item with given product and orderConfirmationNumber");
		
		ShippingItem shippingItem = shippingItems.get(0);
		invoiceItem.withdraw(shippingItem);
		shippingItemRepository.save(shippingItem);
		invoiceItemRepository.delete(invoiceItem);
		
		return invoiceItem;
	}

	@Override
	public ArchiveItem complete(InvoiceItem invoiceItem, Long accountNumber) {
		log.debug("complete");
		ArchiveItem archiveItem = invoiceItem.complete(invoiceItem.getQuantity(), accountNumber);
		archiveItemRepository.save(archiveItem);
		invoiceItemRepository.save(invoiceItem);
		
		return archiveItem;
	}

	@Override
	public ArchiveItem decomplete(ArchiveItem archiveItem) {
		log.debug("decomplete");
		
		List<InvoiceItem> invoiceItems = 
				invoiceItemRepository.findByInvoiceNumberAndProduct(
						archiveItem.getInvoiceNumber(), archiveItem.getProduct());
		
		if (invoiceItems.size()!=1)
			throw new IllegalStateException("An invoice should have "
					+ "only one invoice item with given product and invoiceNumber");
		InvoiceItem invoiceItem = invoiceItems.get(0);
		archiveItem.decomplete(invoiceItem);
		
		invoiceItemRepository.save(invoiceItem);
		archiveItemRepository.delete(archiveItem);
		
		return archiveItem;
	}


}
