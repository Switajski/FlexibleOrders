package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.factory.WholesaleOrderHandlingFactory;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ForwardSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.exception.BusinessException;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.HandlingEventRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.entities.JsonDeliverRequest;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@Service
public class OrderServiceImpl {

	private static final boolean TEST = false;
	private ReportRepository reportRepo;
	private OrderItemRepository itemRepo;
	private CatalogProductRepository cProductRepo;
	private HandlingEventRepository heRepo;
	
	private WholesaleOrderHandlingFactory orderFactory = new WholesaleOrderHandlingFactory();
	private OrderRepository orderRepo;
	
	@Autowired
	public OrderServiceImpl(ReportRepository reportRepo,
			OrderItemRepository itemRepo, OrderRepository orderRepo,
			CatalogProductRepository cProductRepo, HandlingEventRepository heRepo) {
		this.reportRepo = reportRepo;
		this.itemRepo = itemRepo;
		this.cProductRepo = cProductRepo;
		this.orderRepo = orderRepo;
		this.heRepo = heRepo;
	}
	
	/**
	 * creates a new order with OrderItem with given product.
	 *  If given orderNumber exists, adds a created order item to existing order.
	 * @param customer not null
	 * @param orderNumber not null
	 * @param product not null
	 * @param orderedQuantity not null and greater zero
	 * @param priceNet
	 * @return
	 */
	@Transactional
	public OrderItem order(Customer customer, String orderNumber, 
			Product product, Integer orderedQuantity, Amount priceNet){
		if (customer == null || orderNumber == null || product == null || 
				orderedQuantity == null || orderedQuantity < 1)
			throw new IllegalArgumentException();
		
		if (orderRepo.findByOrderNumber(orderNumber) != null){
//			check if parameters are equal
			return this.order(orderNumber, product, orderedQuantity, priceNet);
		}
		
		FlexibleOrder order = new FlexibleOrder(customer, OriginSystem.FLEXIBLE_ORDERS, orderNumber);
		OrderItem item = orderFactory.addOrderItem(order, product, orderedQuantity, priceNet);
		
		if (TEST) return itemRepo.saveAndFlush(item);
		return itemRepo.save(item);
	}
	
	/**
	 * adds an orderItem to existing Order with given orderNumber
	 * @param orderNumber not null
	 * @param product not null
	 * @param orderedQuantity not null and greater than 0
	 * @return
	 */
	@Transactional
	public OrderItem order(String orderNumber, Product product, Integer orderedQuantity, Amount priceNet){
		if (orderNumber == null || orderedQuantity == null || orderedQuantity < 1 ||
				product == null)
			throw new IllegalArgumentException();
		FlexibleOrder order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null) 
			throw new IllegalArgumentException("Order with given OrderNumber not found");
		
		OrderItem item = orderFactory.addOrderItem(order, product, orderedQuantity, priceNet);
		
		if (TEST) return itemRepo.saveAndFlush(item);
		return itemRepo.save(item);
	}
	
	@Transactional
	public ConfirmationReport confirm(String orderNumber, String orderConfirmationNumber,
			Date expectedDelivery, Map<Long, Integer> items){
		if (reportRepo.findByDocumentNumber(orderConfirmationNumber) != null)
			throw new IllegalStateException("ConfirmationReport already exists");
		
		OrderItem orderItem = itemRepo.findOne(items.keySet().iterator().next());
		Address address = orderItem.getOrder().getCustomer().getAddress();
		
		ConfirmationReport cr = new ConfirmationReport(orderConfirmationNumber, 
				address, address, new ConfirmedSpecification(false, false));
		cr.setExpectedDelivery(expectedDelivery);
		
		HandlingEvent he = null;
		for (Entry<Long, Integer> entry: items.entrySet()){
			OrderItem oi = itemRepo.findOne(entry.getKey());
			if (oi == null) 
				throw new IllegalArgumentException("Bestellposition nicht gefunden");
			he = new HandlingEvent(cr, HandlingEventType.CONFIRM, oi, 
					entry.getValue(), new Date());
			itemRepo.save(oi);
		}
		
		return he.getOrderConfirmation();
	}
	
	/**
	 * Adds an order item to an existing OrderConfirmation  
	 * 
	 * @param orderItemToConfirm not null
	 * @param quantity not null
	 * @param orderConfirmationNo must reference an existent OrderConfirmation
	 * @param negotiatedPriceNet if null, the recommended price from {@link CatalogProductRepository} is taken
	 * @return
	 */
	@Transactional
	public OrderItem confirm_old(OrderItem orderItemToConfirm, 
			int quantity,
			String orderConfirmationNo, 
			Amount negotiatedPriceNet) {
		if (orderItemToConfirm == null || quantity < 0 || orderConfirmationNo == null)
			throw new IllegalArgumentException();
		if (negotiatedPriceNet == null || negotiatedPriceNet.getValue() == null)
			negotiatedPriceNet = retrieveRecommendedPriceNet(orderItemToConfirm);
		Report report = reportRepo.findByDocumentNumber(orderConfirmationNo);
		if (report == null) 
			throw new IllegalArgumentException("Order confirmation number does not reference a valid order confirmation!");
		
		OrderItem oi = orderFactory.addToOrderConfirmation(report, orderItemToConfirm, quantity, negotiatedPriceNet, null); 
		
		if (TEST) return itemRepo.saveAndFlush(oi);
		return itemRepo.save(oi);
	}
	
	private Amount retrieveRecommendedPriceNet(OrderItem orderItemToConfirm) {
		CatalogProduct product = 
				cProductRepo.findByProductNumber(orderItemToConfirm.getProduct().getProductNumber());
		if (product == null)
			//TODO: find a more suitable Exception - something like NotFoundException
			throw new IllegalArgumentException("Product with given productno. not found in catalog");
		if (product.getRecommendedPriceNet() == null)
			throw new IllegalArgumentException("Price of product with given productno. not set in catalog");
		return product.getRecommendedPriceNet();
		
	}

	/**
	 * Creates an OrderConfirmation and adds the item to the given OrderConfirmation
	 * 
	 * @param itemToConfirm not null
	 * @param quantity greater than zero and less/equals ordered quantity
	 * @param negotiatedPriceNet if null recommended price from {@link CatalogProductRepository} will be retrieved
	 * @param confirmationReport not null
	 * @param spec
	 * @return
	 */
//	@Transactional
//	public OrderItem confirm(OrderItem itemToConfirm, Integer quantity, 
//			Amount negotiatedPriceNet, ConfirmationReport confirmationReport, 
//			ConfirmedSpecification spec) throws IllegalArgumentException {
//		//TODO: confirm has an report as parameter - shipAndInvoice a documentNumber. 
//		//When a Report is given outside transaction it will be probably be detached.
//		if (quantity == null || quantity < 1) throw new IllegalArgumentException("Menge nicht angegeben");
//		if (itemToConfirm == null || negotiatedPriceNet == null || confirmationReport == null)
//			throw new IllegalArgumentException();
//		if (negotiatedPriceNet.getValue() == null)
//			throw new IllegalArgumentException("Preis nicht angegeben");
//		if (spec == null) 
//			spec = new ConfirmedSpecification(false, false);
//		if (quantity > itemToConfirm.getOrderedQuantity())
//			throw new IllegalArgumentException("Quantity to confirm is greater than ordered!");
//		if (negotiatedPriceNet == null || negotiatedPriceNet.getValue() == null)
//			negotiatedPriceNet = retrieveRecommendedPriceNet(itemToConfirm);
//		
//		if (reportRepo.findByDocumentNumber(confirmationReport.getDocumentNumber()) != null){
//			//	TODO check if parameters are equal to existing confirmationReport
//			return this.confirm(itemToConfirm, quantity, confirmationReport.getDocumentNumber(), negotiatedPriceNet);
//		}
//		
//		OrderItem item = orderFactory.createOrderConfirmation(itemToConfirm, quantity, 
//				negotiatedPriceNet, null, confirmationReport, null);
//		
//		if (TEST) return itemRepo.saveAndFlush(item);
//		return itemRepo.save(item);
//	}

	@Transactional
	public OrderItem deconfirm(OrderItem shippingItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Transactional
	public List<ReportItem> deliverJsonCommitted(JsonDeliverRequest deliverRequest){
		new Address(deliverRequest.getName1(), deliverRequest.getName2(),
				deliverRequest.getStreet(), deliverRequest.getPostalCode(), 
				deliverRequest.getCity(), Country.GERMANY);
		
		Map<Long, Integer> hes = new HashMap<Long, Integer>();
		for (ReportItem ri :deliverRequest.getItems()){
			if (heRepo.findOne(ri.getId()) == null)
				throw new IllegalArgumentException("Auftragsbestätigung mit gegebener Id nicht gefunden");
			hes.put(ri.getId(), ri.getQuantity());
		}
		
		DeliveryNotes deliveryNotes = deliver(deliverRequest.getInvoiceNumber(), deliverRequest.getTrackNumber(), deliverRequest.getPackageNumber(),
				new Address(deliverRequest.getName1(), deliverRequest.getName2(),
						deliverRequest.getStreet(), deliverRequest.getPostalCode(), 
						deliverRequest.getCity(), Country.GERMANY),
						hes);
		
		List<ReportItem> response = new ArrayList<ReportItem>();
		for (HandlingEvent he :deliveryNotes.getEvents()){
			response.add(he.toReportItem());
		}
		
		return response;
	}
	
	public DeliveryNotes deliver(String invoiceNumber, String trackNumber, String packageNumber,
			Address shippingAddress, Map<Long, Integer> confirmEvents){
		
		DeliveryNotes deliveryNotes;
		
		if (reportRepo.findByDocumentNumber(invoiceNumber) == null){
			deliveryNotes = new DeliveryNotes(invoiceNumber, 
					new ShippedSpecification(false, false), shippingAddress);
		} else {
			deliveryNotes = (DeliveryNotes) reportRepo.findByDocumentNumber(invoiceNumber);
		}
		
		for (Entry<Long, Integer> entry: confirmEvents.entrySet()){
			OrderItem orderItem = itemRepo.findOne(entry.getKey());
			deliveryNotes.addEvent(new HandlingEvent(deliveryNotes, HandlingEventType.SHIP, 
					orderItem, entry.getValue(), new Date()));
		}
		
		for (HandlingEvent he: deliveryNotes.getEvents())
			itemRepo.save(he.getOrderItem());
		return reportRepo.save(deliveryNotes);
	}

	/**
	 * creates an invoice with given invoiceno and adds shippingItemToDeliver to it. 
	 * If invoice already exists, then adds the item to existing invoice.
	 * 
	 * @param shippingItemToDeliver not null
	 * @param quantity not null
	 * @param invoiceNo not null
	 * @param expectedDelivery
	 * @param spec 
	 * @param shippedAddress
	 * @return
	 */
	@Transactional
	public OrderItem shipAndInvoice(OrderItem shippingItemToDeliver, Integer quantity, 
			String invoiceNo, ShippedSpecification spec, Address shippedAddress) {
		//TODO: provide this checks in a specification like "Shippable-Specification" 
		// or: "ConfirmedSpecification" or do i mess it up with validation?
		if (!new ConfirmedSpecification(false, false).isSatisfiedBy(shippingItemToDeliver))
			throw new IllegalArgumentException();
		if (shippingItemToDeliver == null)
			throw new IllegalArgumentException();
		if (quantity == null || quantity < 1)
			throw new IllegalArgumentException("Menge ist nicht valide");
		if (invoiceNo == null)
			throw new IllegalArgumentException("Rechnungsnummer nicht angegeben");
		if (spec == null) 
			spec = new ShippedSpecification(false, false);
		
		if (shippedAddress == null)
			shippedAddress = retrieveAddressFromCustomer(shippingItemToDeliver);
		
		Integer confirmedQuantity = shippingItemToDeliver.getHandledQuantity(HandlingEventType.CONFIRM);
		if (confirmedQuantity == null)
			throw new IllegalArgumentException("Given shipping item has no order confirmation");
		else if (quantity > confirmedQuantity)
			throw new IllegalArgumentException("Quantity to be shipped is greater than confirmed");
		
		if (reportRepo.findByDocumentNumber(invoiceNo) != null){
			// check if parameters are equal to found invoice
			return this.shipAndInvoice(shippingItemToDeliver, quantity, invoiceNo);
		}
		DeliveryNotes dn = new DeliveryNotes(invoiceNo, new ShippedSpecification(false, false), shippedAddress);
		OrderItem item = orderFactory.createDeliveryNotes(shippingItemToDeliver, dn, quantity, null);
		
		if (TEST) return itemRepo.saveAndFlush(item);
		return itemRepo.save(item);
	}

	private Address retrieveAddressFromCustomer(OrderItem shippingItemToDeliver) {
		if (shippingItemToDeliver.getOrder() == null || 
			shippingItemToDeliver.getOrder().getCustomer() == null || 
			shippingItemToDeliver.getOrder().getCustomer().getAddress() == null)
			throw new BusinessException("Shipped address not provided");
		return shippingItemToDeliver.getOrder().getCustomer().getAddress();
	}

	@Transactional
	public OrderItem withdrawInvoiceItemAndShipment(OrderItem invoiceItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	/**
	 * adds a shipping item to deliver to an existing invoice with given invoiceno.
	 * @param shippingItemToDeliver not null
	 * @param quantity not null and greater than zero
	 * @param invoiceNo not null
	 * @return
	 */
	@Transactional
	public OrderItem shipAndInvoice(OrderItem shippingItemToDeliver, Integer quantity,
			String invoiceNo) {
		if (shippingItemToDeliver == null || quantity == null || quantity < 1 || invoiceNo == null)
			throw new IllegalArgumentException();
		
		Report confirmationReport = reportRepo.findByDocumentNumber(invoiceNo);
		
		OrderItem item = orderFactory
				.addToInvoice(confirmationReport, shippingItemToDeliver, quantity, invoiceNo, null);
		
		if (TEST) return itemRepo.saveAndFlush(item);
		return itemRepo.save(item);
	}

	@Transactional
	public HandlingEvent forwardToThirdParty(OrderItem orderItemToForward,
			int quantity, String forwardNo, ForwardSpecification forwardSpec) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public OrderItem forwardToThirdParty(OrderItem orderItemToForward,
			int quantity, String forwardNo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * adds a HandlingEvent of type PAID to each item of given document
	 * 
	 * @param documentNumber not null
	 * @param receivedPaymentDate
	 * @return
	 */
	@Transactional
	public Set<HandlingEvent> receivePayment(String documentNumber, Date receivedPaymentDate) {
//		FIXME: does not work
		if (receivedPaymentDate == null) 
			receivedPaymentDate = new Date();
		
		Report report = reportRepo.findByDocumentNumber(documentNumber);
		if (report == null) 
			throw new IllegalArgumentException("given documentNumber not found");
		Set<HandlingEvent> hes = new HashSet<HandlingEvent>(); 
		for (HandlingEvent he: report.getEvents()){
			he.getOrderItem().addHandlingEvent(
					new HandlingEvent(report, HandlingEventType.PAID, he.getOrderItem(), 
							he.getOrderItem().getOrderedQuantity(), receivedPaymentDate)
					);
			hes.add(he);
			itemRepo.save(he.getOrderItem());
		}
		return hes;
	}

	@Transactional
	public OrderItem cancelReceivedPayment(String invoiceNo){
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public HandlingEvent withdrawPayment(Report item) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public boolean existsOrderNumber(String orderNumber) {
		return (orderRepo.findByOrderNumber(orderNumber) != null);
	}
	
}
