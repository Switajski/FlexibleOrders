package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CancelReport;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.factory.WholesaleOrderHandlingFactory;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ForwardSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.HandlingEventRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@Service
public class OrderServiceImpl {

	private static final boolean TEST = false;
	private ReportRepository reportRepo;
	private OrderItemRepository itemRepo;
	private CatalogProductRepository cProductRepo;
	private HandlingEventRepository heRepo;
	private CustomerRepository customerRepo;
	
	private WholesaleOrderHandlingFactory orderFactory = new WholesaleOrderHandlingFactory();
	private OrderRepository orderRepo;
	
	@Autowired
	public OrderServiceImpl(ReportRepository reportRepo, CustomerRepository customerRepo,
			OrderItemRepository itemRepo, OrderRepository orderRepo,
			CatalogProductRepository cProductRepo, HandlingEventRepository heRepo) {
		this.reportRepo = reportRepo;
		this.itemRepo = itemRepo;
		this.cProductRepo = cProductRepo;
		this.orderRepo = orderRepo;
		this.heRepo = heRepo;
		this.customerRepo = customerRepo;
	}
	
//	/**
//	 * creates a new order with OrderItem with given product.
//	 *  If given orderNumber exists, adds a created order item to existing order.
//	 * @param customer not null
//	 * @param orderNumber not null
//	 * @param product not null
//	 * @param orderedQuantity not null and greater zero
//	 * @param priceNet
//	 * @return
//	 */
//	@Transactional
//	public OrderItem order(Customer customer, String orderNumber, 
//			Product product, Integer orderedQuantity, Amount priceNet){
//		if (customer == null || orderNumber == null || product == null || 
//				orderedQuantity == null || orderedQuantity < 1)
//			throw new IllegalArgumentException();
//		
//		if (orderRepo.findByOrderNumber(orderNumber) != null){
//			return this.order(orderNumber, product, orderedQuantity, priceNet);
//		}
//		
//		FlexibleOrder order = new FlexibleOrder(customer, OriginSystem.FLEXIBLE_ORDERS, orderNumber);
//		OrderItem item = orderFactory.addOrderItem(order, product, orderedQuantity, priceNet);
//		
//		if (TEST) return itemRepo.saveAndFlush(item);
//		return itemRepo.save(item);
//	}
	
	@Transactional
	public FlexibleOrder order(Long customerId, String orderNumber, List<ReportItem> ris){
		if (customerId == null || orderNumber == null || ris.isEmpty())
			throw new IllegalArgumentException();
		if (orderRepo.findByOrderNumber(orderNumber) != null)
			throw new IllegalArgumentException("Bestellnr existiert bereits");
		Customer customer = customerRepo.findOne(customerId);
		if (customer == null)
			throw new IllegalArgumentException("Keinen Kunden mit gegebener Kundennr. gefunden");
		
		FlexibleOrder order = new FlexibleOrder(customer, OriginSystem.FLEXIBLE_ORDERS, orderNumber);
		for (ReportItem ri:ris){
			CatalogProduct cProduct = cProductRepo.findByProductNumber(ri.getProduct());
			if (cProduct == null) throw new IllegalArgumentException("Artikelnr nicht gefunden");
			
			OrderItem oi = new OrderItem(order, cProduct.toProduct(), ri.getQuantity());
			oi.setNegotiatedPriceNet(new Amount(ri.getPriceNet(), Currency.EUR));
			oi.setCreated(new Date());
			order.addOrderItem(oi);
		}
		
		return orderRepo.save(order);
	}
	
	/**
	 * adds an orderItem to existing Order with given orderNumber
	 * @param orderNumber not null
	 * @param product not null
	 * @param orderedQuantity not null and greater than 0
	 * @return
	 */
	@Transactional
	public OrderItem addToOrder(String orderNumber, Product product, Integer orderedQuantity, Amount priceNet){
		if (orderNumber == null || orderedQuantity == null || orderedQuantity < 1 ||
				product == null)
			throw new IllegalArgumentException();
		FlexibleOrder order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null) 
			throw new IllegalArgumentException("Order with given OrderNumber not found");
		
		OrderItem item = orderFactory.addOrderItem(order, product, orderedQuantity, priceNet);
		
		return itemRepo.save(item);
	}
	
	@Transactional
	public ConfirmationReport confirm(String orderNumber, String confirmNumber,
			Date expectedDelivery, List<ReportItem> orderItems){
		if (reportRepo.findByDocumentNumber(confirmNumber) != null)
			throw new IllegalArgumentException("Auftragsnr. "+ confirmNumber +" besteht bereits");
		if (orderItems.isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben");
		
		FlexibleOrder order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null) throw new IllegalArgumentException("Bestellnr. nicht gefunden");
		
		Address address = order.getCustomer().getAddress();
				
		ConfirmationReport cr = new ConfirmationReport(confirmNumber, 
				address, address, new ConfirmedSpecification(false, false));
		cr.setExpectedDelivery(expectedDelivery);
		
		for (ReportItem entry: orderItems){
			OrderItem oi = itemRepo.findOne(entry.getId());
			if (oi == null) 
				throw new IllegalArgumentException("Bestellposition nicht gefunden");
			cr.addEvent(new HandlingEvent(cr, HandlingEventType.CONFIRM, oi, 
					entry.getQuantity(), new Date()));
		}
		return reportRepo.save(cr);
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
	public DeliveryNotes deliver(String invoiceNumber, String trackNumber, String packageNumber,
			Address shippingAddress, Amount shipment, List<ReportItem> confirmEvents){
		if (reportRepo.findByDocumentNumber(invoiceNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");
		
		DeliveryNotes deliveryNotes = new DeliveryNotes(invoiceNumber, createShippingCosts(shipment),
				new ShippedSpecification(false, false), shippingAddress);
				
		for (ReportItem entry: confirmEvents){
			if (entry.getQuantity() == null || entry.getQuantity() < 1)
				throw new IllegalArgumentException("Menge ist nicht valide");
			
			HandlingEvent confirmEvent = heRepo.findOne(entry.getId());
			deliveryNotes.addEvent(new HandlingEvent(deliveryNotes, HandlingEventType.SHIP, 
					confirmEvent.getOrderItem(), entry.getQuantity(), new Date()));
		}
		
		return reportRepo.save(deliveryNotes);
	}

	private Product createShippingCosts(Amount shipment) {
		// private Product createShipment(Amount shipment2) {
		Product product = new Product();
		product.setProductType(ProductType.SHIPPING);
		product.setName("Versand");
		product.setShippingCosts(shipment);
		return product;
	}

	@Transactional
	public OrderItem withdrawInvoiceItemAndShipment(OrderItem invoiceItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
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
	public Receipt markAsPayed(String documentNumber, String receiptNumber, 
			Date receivedPaymentDate, List<ReportItem> ris) {
		if (receivedPaymentDate == null) 
			receivedPaymentDate = new Date();
		
		Report report = reportRepo.findByDocumentNumber(documentNumber);
		if (report == null) 
			throw new IllegalArgumentException("Bericht mit gegebener Nummer nicht gefunden");
		
		Receipt receipt = new Receipt(receiptNumber, receivedPaymentDate);
		
		for (ReportItem ri: ris){
			HandlingEvent he = heRepo.findOne(ri.getId());
			receipt.addEvent(
					new HandlingEvent(receipt, HandlingEventType.PAID, he.getOrderItem(), 
							he.getQuantity(), receivedPaymentDate));
		}
		return reportRepo.save(receipt);
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

	public ConfirmationReport confirm(String orderNumber) {
		List<OrderItem> ois = itemRepo.findByOrderNumber(orderNumber);
		List<ReportItem> ris = convertToReportItems(ois);
		return confirm(orderNumber, "AB"+orderNumber, new Date(), ris);
	}

	private List<ReportItem> convertToReportItems(List<OrderItem> ois) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (OrderItem oi:ois){
			ris.add(oi.toReportItem());
		}
		return ris;
	}

	@Transactional
	public boolean deleteOrder(String orderNumber) { 
		FlexibleOrder order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException("Bestellnr. zum löschen nicht gefunden");
		orderRepo.delete(order);
		return true;
	}
	
	@Transactional
	public CancelReport cancelConfirmationReport(String orderConfirmationNumber){
		Report cr = reportRepo.findByDocumentNumber(orderConfirmationNumber);
		if (cr == null || !(cr instanceof ConfirmationReport))
			throw new IllegalArgumentException("Auftragsnr. nicht gefunden");
		CancelReport cancelReport = createCancelReport(cr);
		return reportRepo.save(cancelReport);
	}
	
	@Transactional
	public CancelReport cancelDeliveryNotes(String invoiceNumber){
		Report cr = reportRepo.findByDocumentNumber(invoiceNumber);
		if (cr == null || !(cr instanceof DeliveryNotes))
			throw new IllegalArgumentException("Rechnungs/Lieferscheinnr. nicht gefunden");
		CancelReport cancelReport = createCancelReport(cr);
		return reportRepo.save(cancelReport);
	}

	private CancelReport createCancelReport(Report cr) {
		CancelReport cancelReport = new CancelReport("ABGEBROCHEN-"+cr.getDocumentNumber());
		for (HandlingEvent he :cr.getEvents()){
			cancelReport.addEvent(new HandlingEvent(cancelReport, HandlingEventType.CANCEL, 
					he.getOrderItem(), he.getQuantity(), new Date()));
		}
		return cancelReport;
	}

	@Transactional
	public boolean deleteReport(String invoiceNumber) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null)
			throw new IllegalArgumentException("Bericht zum löschen nicht gefunden");
		reportRepo.delete(r);
		return true;
	}
	
}
