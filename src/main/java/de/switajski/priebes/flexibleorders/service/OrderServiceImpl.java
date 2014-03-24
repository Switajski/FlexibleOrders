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
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
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

	private ReportRepository reportRepo;
	private OrderItemRepository itemRepo;
	private CatalogProductRepository cProductRepo;
	private HandlingEventRepository heRepo;
	private CustomerRepository customerRepo;
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
					entry.getQuantityLeft(), new Date()));
		}
		return reportRepo.save(cr);
	}

	@Transactional
	public OrderItem deconfirm(OrderItem shippingItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Transactional
	public DeliveryNotes deliver(String deliveryNotesNumber, String trackNumber, String packageNumber,
			Address shippingAddress, Amount shipment, List<ReportItem> confirmEvents){
		if (reportRepo.findByDocumentNumber(deliveryNotesNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");
		
		DeliveryNotes deliveryNotes = new DeliveryNotes(deliveryNotesNumber,
				new ShippedSpecification(false, false), shippingAddress);
				
		FlexibleOrder firstOrder = null;
		for (ReportItem entry: confirmEvents){
			HandlingEvent confirmEventToBeDelivered = heRepo.findOne(entry.getId());
			OrderItem orderItemToBeDelivered = confirmEventToBeDelivered.getOrderItem();
			
			//TODO: move into validation layer
			Integer quantityToDeliver = validateQuantity(entry, orderItemToBeDelivered,
					HandlingEventType.CONFIRM);
			
			deliveryNotes.addEvent(new HandlingEvent(deliveryNotes, HandlingEventType.SHIP, 
					orderItemToBeDelivered, quantityToDeliver, new Date()));
			
			if (firstOrder == null)
				firstOrder = orderItemToBeDelivered.getOrder();
		}

		// add shipping costs as new HandlingEvent
		if (shipment != null && shipment.isGreaterZero())
			deliveryNotes.addEvent(
					new HandlingEvent(
							deliveryNotes, HandlingEventType.SHIP,
							createShippingCosts(shipment, firstOrder), 
							1, new Date())
					);

		return reportRepo.save(deliveryNotes);
	}

	private Integer validateQuantity(ReportItem entry, OrderItem orderItemToBeDelivered,
			HandlingEventType type) {
		Integer quantityToDeliver = entry.getQuantityLeft();
		if (quantityToDeliver == null || quantityToDeliver < 1 || 
				quantityToDeliver > orderItemToBeDelivered.calculateQuantityLeft(type))
			throw new IllegalArgumentException("Menge ist nicht valide");
		return quantityToDeliver;
	}

	@Transactional
	private OrderItem createShippingCosts(Amount shipment, FlexibleOrder order) {
		Product product = new Product();
		product.setProductType(ProductType.SHIPPING);
		product.setName("Versand");
		
		OrderItem shipOi = new OrderItem(order, product, 1);
		shipOi.setNegotiatedPriceNet(shipment);
		
		return itemRepo.save(shipOi);
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
		if (r instanceof DeliveryNotes){
			deleteShippingCosts(r);
		}
		reportRepo.delete(r);
		return true;
	}

	@Transactional
	private void deleteShippingCosts(Report r) {
		for (HandlingEvent he:r.getEvents()){
			OrderItem orderItem = he.getOrderItem();
			if (orderItem.isShippingCosts())
				orderItem.getOrder().remove(orderItem);
			orderRepo.save(orderItem.getOrder());
		}
	}

	@Transactional
	public Receipt markAsPayed(String invoiceNumber, String receiptNumber, Date date) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null || !(r instanceof Invoice))
			throw new IllegalArgumentException("Rechnungsnr nicht gefunden");
		
		Invoice invoice = (Invoice) r;
		Receipt receipt = new Receipt(receiptNumber, date);
		for (HandlingEvent he:invoice.getEvents()){
			receipt.addEvent(
					new HandlingEvent(receipt, HandlingEventType.PAID, he.getOrderItem(), he.getQuantity(), new Date()));
		}
		return reportRepo.save(receipt);
	}

	/**
	 * 
	 * @param invoiceNumber
	 * @param paymentConditions
	 * @param invoiceAddress
	 * @param shipEvents
	 * @return
	 */
	@Transactional
	public Invoice invoice(String invoiceNumber, String paymentConditions, Address invoiceAddress,
			List<ReportItem> shipEvents) {
		if (reportRepo.findByDocumentNumber(invoiceNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");
		
		Invoice invoice = new Invoice(invoiceNumber, paymentConditions,
				invoiceAddress);
				
		for (ReportItem entry: shipEvents){
			HandlingEvent shipEventToBeInvoiced = heRepo.findOne(entry.getId());
			
			OrderItem orderItemToBeInvoiced = shipEventToBeInvoiced.getOrderItem(); 
			
			//TODO: move into validation layer
			Integer quantityToDeliver = validateQuantity(entry, orderItemToBeInvoiced,
					HandlingEventType.SHIP);
			
			invoice.addEvent(new HandlingEvent(invoice, HandlingEventType.INVOICE, 
					shipEventToBeInvoiced.getOrderItem(), quantityToDeliver, new Date()));
		}
		
		return reportRepo.save(invoice);
	}

}
