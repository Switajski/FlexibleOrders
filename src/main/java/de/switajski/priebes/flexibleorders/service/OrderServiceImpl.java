package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
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
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.entities.ItemDto;

/**
 * TODO: Add validation to service layer:</br>
 * see <a href="http://docs.spring.io/spring/docs/3.0.0.RC3/reference/html/ch05s07.html">
 * http://docs.spring.io/spring/docs/3.0.0.RC3/reference/html/ch05s07.html</a>
 * 
 * @author Marek Switajski
 *
 */
@Service
public class OrderServiceImpl {

	private ReportRepository reportRepo;
	private OrderItemRepository itemRepo;
	private CatalogProductRepository cProductRepo;
	private ReportItemRepository heRepo;
	private CustomerRepository customerRepo;
	private OrderRepository orderRepo;
	
	@Autowired
	public OrderServiceImpl(ReportRepository reportRepo, CustomerRepository customerRepo,
			OrderItemRepository orderItemRepo, OrderRepository orderRepo,
			CatalogProductRepository cProductRepo, ReportItemRepository heRepo) {
		this.reportRepo = reportRepo;
		this.itemRepo = orderItemRepo;
		this.cProductRepo = cProductRepo;
		this.orderRepo = orderRepo;
		this.heRepo = heRepo;
		this.customerRepo = customerRepo;
	}
	
	/**
	 * Creates initially an order with its order items
	 * 
	 * @param customerId
	 * @param orderNumber
	 * @param reportItems
	 * @return created order, when successfully persisted
	 */
	@Transactional
	public Order order(Long customerId, String orderNumber, List<ItemDto> reportItems){
		if (customerId == null || orderNumber == null || reportItems.isEmpty())
			throw new IllegalArgumentException();
		if (orderRepo.findByOrderNumber(orderNumber) != null)
			throw new IllegalArgumentException("Bestellnr existiert bereits");
		Customer customer = customerRepo.findOne(customerId);
		if (customer == null)
			throw new IllegalArgumentException("Keinen Kunden mit gegebener Kundennr. gefunden");
		
		Order order = new Order(customer, OriginSystem.FLEXIBLE_ORDERS, orderNumber);
		for (ItemDto ri:reportItems){
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
			Date expectedDelivery, List<ItemDto> orderItems){
		validateConfirm(orderNumber, confirmNumber, orderItems);
		
		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null) throw new IllegalArgumentException("Bestellnr. nicht gefunden");
		
		Address address = order.getCustomer().getAddress();
				
		ConfirmationReport cr = new ConfirmationReport(confirmNumber, 
				address, address, new ConfirmedSpecification(false, false));
		cr.setExpectedDelivery(expectedDelivery);
		//TODO: Refactor: DRY!
		cr.setCustomerNumber(order.getCustomer().getCustomerNumber());
		
		for (ItemDto entry: orderItems){
			OrderItem oi = itemRepo.findOne(entry.getId());
			if (oi == null) 
				throw new IllegalArgumentException("Bestellposition nicht gefunden");
			cr.addItem(new ReportItem(cr, ReportItemType.CONFIRM, oi, 
					entry.getQuantityLeft(), new Date()));
		}
		return reportRepo.save(cr);
	}

	private void validateConfirm(String orderNumber, String confirmNumber,
			List<ItemDto> orderItems) {
		if (reportRepo.findByDocumentNumber(confirmNumber) != null)
			throw new IllegalArgumentException("Auftragsnr. "+ confirmNumber +" besteht bereits");
		if (orderItems.isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben");
		if (orderNumber == null)
			throw new IllegalArgumentException("Keine Bestellnr angegeben");
		if (confirmNumber == null)
			throw new IllegalArgumentException("Keine AB-nr angegeben");
		for (ItemDto item: orderItems){
			if (item.getId() == null)
				throw new IllegalArgumentException("Position hat keine Id");
		}
	}

	@Transactional
	public OrderItem deconfirm(OrderItem shippingItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Transactional
	public DeliveryNotes deliver(String deliveryNotesNumber, String trackNumber, String packageNumber,
			Address shippingAddress, Amount shipment, List<ItemDto> confirmEvents){
		if (reportRepo.findByDocumentNumber(deliveryNotesNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");
		
		DeliveryNotes deliveryNotes = new DeliveryNotes(deliveryNotesNumber,
				new ShippedSpecification(false, false), shippingAddress, shipment);
				
		Order firstOrder = null;
		for (ItemDto entry: confirmEvents){
			ReportItem confirmEventToBeDelivered = heRepo.findOne(entry.getId());
			OrderItem orderItemToBeDelivered = confirmEventToBeDelivered.getOrderItem();
			
			//TODO: move into validation layer
			Integer quantityToDeliver = validateQuantity(entry, orderItemToBeDelivered,
					ReportItemType.CONFIRM);
			
			deliveryNotes.addItem(new ReportItem(deliveryNotes, ReportItemType.SHIP, 
					orderItemToBeDelivered, quantityToDeliver, new Date()));
			
			if (firstOrder == null)
				firstOrder = orderItemToBeDelivered.getOrder();
		}

		//TODO:Refactor: DRY!
		deliveryNotes.setCustomerNumber(firstOrder.getCustomer().getCustomerNumber());
		return reportRepo.save(deliveryNotes);
	}

	private Integer validateQuantity(ItemDto entry, OrderItem orderItemToBeDelivered,
			ReportItemType type) {
		Integer quantityToDeliver = entry.getQuantityLeft();
		if (quantityToDeliver == null)
			throw new IllegalArgumentException("Menge nicht angegeben");
		if (quantityToDeliver < 1)
			throw new IllegalArgumentException("Menge kleiner eins");
		if (quantityToDeliver > orderItemToBeDelivered.calculateQuantityLeft(type))
			throw new IllegalArgumentException("angeforderte Menge ist zu gross");
		return quantityToDeliver;
	}

	@Transactional
	private OrderItem createShippingCosts(Amount shipment, Order order) {
		Product product = new Product();
		product.setProductType(ProductType.SHIPPING);
		product.setName("Versand");
		
		OrderItem shipOi = new OrderItem(order, product, 1);
		shipOi.setNegotiatedPriceNet(shipment);
		
		return itemRepo.save(shipOi);
	}

	@Transactional
	public Receipt markAsPayed(String documentNumber, String receiptNumber, 
			Date receivedPaymentDate, List<ItemDto> ris) {
		if (receivedPaymentDate == null) 
			receivedPaymentDate = new Date();
		
		Report report = reportRepo.findByDocumentNumber(documentNumber);
		if (report == null) 
			throw new IllegalArgumentException("Bericht mit gegebener Nummer nicht gefunden");
		
		Receipt receipt = new Receipt(receiptNumber, receivedPaymentDate);
		
		for (ItemDto ri: ris){
			ReportItem he = heRepo.findOne(ri.getId());
			receipt.addItem(
					new ReportItem(receipt, ReportItemType.PAID, he.getOrderItem(), 
							he.getQuantity(), receivedPaymentDate));
		}
		//TODO: refactor
		receipt.setCustomerNumber(report.getItems().iterator().next().getOrderItem().getOrder().getCustomer().getCustomerNumber());
		return reportRepo.save(receipt);
	}

	@Transactional
	public OrderItem cancelReceivedPayment(String invoiceNo){
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public ReportItem withdrawPayment(Report item) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public boolean existsOrderNumber(String orderNumber) {
		return (orderRepo.findByOrderNumber(orderNumber) != null);
	}

	public ConfirmationReport confirm(String orderNumber) {
		List<OrderItem> ois = itemRepo.findByOrderNumber(orderNumber);
		List<ItemDto> ris = convertToReportItems(ois);
		return confirm(orderNumber, "AB"+orderNumber, new Date(), ris);
	}

	private List<ItemDto> convertToReportItems(List<OrderItem> ois) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (OrderItem oi:ois){
			ris.add(oi.toItemDto());
		}
		return ris;
	}

	@Transactional
	public boolean deleteOrder(String orderNumber) { 
		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException("Bestellnr. zum l�schen nicht gefunden");
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
		for (ReportItem he :cr.getItems()){
			cancelReport.addItem(new ReportItem(cancelReport, ReportItemType.CANCEL, 
					he.getOrderItem(), he.getQuantity(), new Date()));
		}
		return cancelReport;
	}

	@Transactional
	public boolean deleteReport(String invoiceNumber) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null)
			throw new IllegalArgumentException("Bericht zum l�schen nicht gefunden");
		reportRepo.delete(r);
		return true;
	}

	@Transactional
	public Receipt markAsPayed(String invoiceNumber, String receiptNumber, Date date) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null || !(r instanceof Invoice))
			throw new IllegalArgumentException("Rechnungsnr nicht gefunden");
		
		Invoice invoice = (Invoice) r;
		Receipt receipt = new Receipt(receiptNumber, date);
		for (ReportItem he:invoice.getItems()){
			receipt.addItem(
					new ReportItem(receipt, ReportItemType.PAID, he.getOrderItem(), he.getQuantity(), new Date()));
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
			List<ItemDto> shipEvents) {
		if (reportRepo.findByDocumentNumber(invoiceNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");
		
		Invoice invoice = new Invoice(invoiceNumber, paymentConditions,
				invoiceAddress);
		
		Order order = null;
		HashMap<String, DeliveryNotes> deliveryNotes = new HashMap<String, DeliveryNotes>();
		for (ItemDto entry: shipEvents){
			ReportItem shipEventToBeInvoiced = heRepo.findOne(entry.getId());
			
			OrderItem orderItemToBeInvoiced = shipEventToBeInvoiced.getOrderItem(); 
			
			//TODO: move into validation layer
			Integer quantityToDeliver = validateQuantity(entry, orderItemToBeInvoiced,
					ReportItemType.SHIP);
			
			invoice.addItem(new ReportItem(invoice, ReportItemType.INVOICE, 
					shipEventToBeInvoiced.getOrderItem(), quantityToDeliver, new Date()));

			for (ReportItem he :orderItemToBeInvoiced.getAllHesOfType(ReportItemType.SHIP)){
				DeliveryNotes dn = he.getDeliveryNotes();
				deliveryNotes.put(dn.getDocumentNumber(), dn); 
			}
			
			if (order == null) order = orderItemToBeInvoiced.getOrder();
		}
		
		Amount summedShippingCosts = new Amount();
		for (Entry<String, DeliveryNotes> entry2:deliveryNotes.entrySet()){
			if (entry2.getValue().hasShippingCosts())
			summedShippingCosts = summedShippingCosts.add(entry2.getValue().getShippingCosts());
		}
		invoice.setShippingCosts(summedShippingCosts);
		//TODO: refactor DRY!
		invoice.setCustomerNumber(order.getCustomer().getCustomerNumber());
		
		return reportRepo.save(invoice);
	}
	
}
