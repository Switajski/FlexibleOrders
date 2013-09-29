package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.component.ItemTransition;
import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.AccountParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ShippingParameter;
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
	private ArchiveItemRepository archiveItemRepository;
	private static Logger log = Logger.getLogger(TransitionServiceImpl.class);

	@Autowired
	public TransitionServiceImpl(
			OrderItemRepository orderItemRepo,
			ShippingItemRepository shippingItemRepo,
			InvoiceItemRepository invoiceItemRepo,
			ArchiveItemRepository archiveItemRepository) {
		this.orderItemRepository = orderItemRepo;
		this.shippingItemRepository = shippingItemRepo;
		this.invoiceItemRepository = invoiceItemRepo;
		this.archiveItemRepository = archiveItemRepository;
	}

	@Override
	public ShippingItem confirm(OrderItem orderItemToConfirm, ConfirmationParameter confirmationParameter) {
		log.debug("confirm");

		ShippingItem si = new ItemTransition().confirm(orderItemToConfirm, confirmationParameter);

		orderItemRepository.save(orderItemToConfirm);
		shippingItemRepository.save(si);
		return si;
	}

	@Override
	public ShippingItem deconfirm(ShippingItem shippingItem) {
		log.debug("deconfirm");

		List<OrderItem> orderItems = orderItemRepository.findByOrderNumberAndProduct(shippingItem.getOrderNumber(), 
				shippingItem.getProduct());
		if (orderItems.size()==0)
			throw new IllegalStateException("no shipping item found!");
		if (orderItems.size()!=1)
			throw new IllegalStateException("shipping item has more than one equivalent order item");
		
		new ItemTransition().deconfirm(shippingItem, orderItems.get(0));
		shippingItemRepository.delete(shippingItem);
		shippingItemRepository.flush();
		orderItemRepository.save(orderItems.get(0));

		return shippingItem;
	}

	@Override
	public InvoiceItem deliver(ShippingItem shippingItemToDeliver, ShippingParameter parameter) {
		log.debug("deliver");
		InvoiceItem ii = new ItemTransition().
				deliver(
						shippingItemToDeliver, 
						parameter
						);

		shippingItemRepository.save(shippingItemToDeliver);
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
		new ItemTransition().withdraw(shippingItem, invoiceItem);
		shippingItemRepository.save(shippingItem);
		invoiceItemRepository.delete(invoiceItem);

		return invoiceItem;
	}

	@Override
	public ArchiveItem complete(InvoiceItem invoiceItem, AccountParameter accountParameter) {
		log.debug("complete");
		ArchiveItem archiveItem = new ItemTransition().complete(invoiceItem, accountParameter);
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
		new ItemTransition().decomplete(archiveItem, invoiceItem);

		invoiceItemRepository.save(invoiceItem);
		archiveItemRepository.delete(archiveItem);

		return archiveItem;
	}


}
