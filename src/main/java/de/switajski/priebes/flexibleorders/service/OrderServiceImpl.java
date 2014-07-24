package de.switajski.priebes.flexibleorders.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculator;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CancelReport;
import de.switajski.priebes.flexibleorders.domain.CancellationItem;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * TODO: Add validation to service layer:</br> see <a href=
 * "http://docs.spring.io/spring/docs/3.0.0.RC3/reference/html/ch05s07.html">
 * http://docs.spring.io/spring/docs/3.0.0.RC3/reference/html/ch05s07.html</a>
 * </br> and converters from request object to ItemDto
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
	private ItemDtoConverterService itemDtoConverterService;

	@Autowired
	public OrderServiceImpl(ReportRepository reportRepo,
			CustomerRepository customerRepo,
			OrderItemRepository orderItemRepo, OrderRepository orderRepo,
			CatalogProductRepository cProductRepo, ReportItemRepository heRepo,
			ItemDtoConverterService itemDtoConverterService) {
		this.reportRepo = reportRepo;
		this.itemRepo = orderItemRepo;
		this.cProductRepo = cProductRepo;
		this.orderRepo = orderRepo;
		this.heRepo = heRepo;
		this.customerRepo = customerRepo;
		this.itemDtoConverterService = itemDtoConverterService;
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
	public Order order(Long customerId, String orderNumber, Date created,
			List<ItemDto> reportItems) {
		if (customerId == null || orderNumber == null || reportItems.isEmpty())
			throw new IllegalArgumentException();
		if (orderRepo.findByOrderNumber(orderNumber) != null)
			throw new IllegalArgumentException("Bestellnr existiert bereits");
		Customer customer = customerRepo.findOne(customerId);
		if (customer == null)
			throw new IllegalArgumentException(
					"Keinen Kunden mit gegebener Kundennr. gefunden");

		Order order = new Order(
				customer,
				OriginSystem.FLEXIBLE_ORDERS,
				orderNumber);
		order.setCreated((created == null) ? new Date() : created);
		for (ItemDto ri : reportItems) {

			Product product = (ri.getProduct().equals(0L)) ? createCustomProduct(ri) : createProductFromCatalog(ri);

			OrderItem oi = new OrderItem(
					order,
					product,
					ri.getQuantity());
			oi.setNegotiatedPriceNet(new Amount(
					ri.getPriceNet(),
					Currency.EUR));
			order.addOrderItem(oi);
		}

		return orderRepo.save(order);
	}

	private Product createProductFromCatalog(ItemDto ri) {
		Product product;
		CatalogProduct cProduct = cProductRepo.findByProductNumber(
				ri.getProduct());
		if (cProduct == null)
			throw new IllegalArgumentException("Artikelnr nicht gefunden");
		product = cProduct.toProduct();
		return product;
	}

	private Product createCustomProduct(ItemDto ri) {
		Product p = new Product();
		p.setName(ri.getProductName());
		p.setProductType(ProductType.CUSTOM);
		p.setProductNumber(0L);
		return p;
	}

	@Transactional
	public ConfirmationReport confirm(String orderNumber, String confirmNumber,
			Date expectedDelivery, Address shippingAddress, Address invoiceAddress, 
			List<ItemDto> orderItems) {
		validateConfirm(orderNumber, confirmNumber, orderItems);

		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException("Bestellnr. nicht gefunden");

		Customer cust = order.getCustomer();
		Address address = (cust.getInvoiceAddress() == null) ? cust.getShippingAddress() : cust.getInvoiceAddress();
		shippingAddress = (shippingAddress.isComplete()) ? shippingAddress : address;
		invoiceAddress = (invoiceAddress.isComplete()) ? invoiceAddress : address;

		ConfirmationReport cr = new ConfirmationReport(confirmNumber,
				invoiceAddress, shippingAddress);
		cr.setExpectedDelivery(expectedDelivery);
		// TODO: Refactor: DRY!
		cr.setCustomerNumber(cust.getCustomerNumber());
		cr.setCustomerDetails(cust.getDetails());

		for (ItemDto entry : orderItems) {
			OrderItem oi = itemRepo.findOne(entry.getId());
			if (oi == null)
				throw new IllegalArgumentException(
						"Bestellposition nicht gefunden");
			cr.addItem(new ConfirmationItem(cr, oi,
					entry.getQuantityLeft(), new Date()));
		}
		return reportRepo.save(cr);
	}

	private void validateConfirm(String orderNumber, String confirmNumber,
			List<ItemDto> orderItems) {
		if (reportRepo.findByDocumentNumber(confirmNumber) != null)
			throw new IllegalArgumentException("Auftragsnr. " + confirmNumber
					+ " besteht bereits");
		if (orderItems.isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben");
		if (orderNumber == null)
			throw new IllegalArgumentException("Keine Bestellnr angegeben");
		if (confirmNumber == null)
			throw new IllegalArgumentException("Keine AB-nr angegeben");
		for (ItemDto item : orderItems) {
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
	public DeliveryNotes deliver(String deliveryNotesNumber,
			String trackNumber, String packageNumber,
			Amount shipment, Date created, List<ItemDto> confirmEvents) {
		if (reportRepo.findByDocumentNumber(deliveryNotesNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

		DeliveryNotes deliveryNotes = new DeliveryNotes(
				deliveryNotesNumber,
				null,
				shipment);
		deliveryNotes.setCreated(created== null ? new Date() : created);
		
		Order firstOrder = null;
		
		Address shippedAddress = null;
		for (ItemDto entry : confirmEvents) {
			ReportItem confirmEventToBeDelivered = heRepo
					.findOne(entry.getId());
			OrderItem orderItemToBeDelivered = confirmEventToBeDelivered
					.getOrderItem();

			validateQuantity(entry,
					(ConfirmationItem) confirmEventToBeDelivered);

			deliveryNotes.addItem(new ShippingItem(
					deliveryNotes,
					orderItemToBeDelivered,
					entry.getQuantityLeft(), // TODO: GUI sets
												// quanitityToDeliver at this
												// nonsense parameter
					new Date()));
			
			//validate addresses DRY!
			Address temp = orderItemToBeDelivered.getDeliveryHistory().getShippingAddress();
			if (shippedAddress == null)
				shippedAddress = temp;
			else if (!shippedAddress.equals(temp))
				throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");
			
			deliveryNotes.setShippedAddress(shippedAddress);

			if (firstOrder == null)
				firstOrder = orderItemToBeDelivered.getOrder();
		}

		// TODO:Refactor: DRY!
		deliveryNotes.setCustomerNumber(firstOrder
				.getCustomer()
				.getCustomerNumber());
		return reportRepo.save(deliveryNotes);
	}

	private void validateQuantity(ItemDto entry,
			ReportItem reportItem) {
		Integer quantityToDeliver = entry.getQuantityLeft();
		if (quantityToDeliver == null)
			throw new IllegalArgumentException("Menge nicht angegeben");
		if (quantityToDeliver < 1)
			throw new IllegalArgumentException("Menge kleiner eins");
		if (quantityToDeliver > new QuantityLeftCalculator().calculate(
				reportItem))
			throw new IllegalArgumentException(
					"angeforderte Menge ist zu gross");
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
			throw new IllegalArgumentException(
					"Bericht mit gegebener Nummer nicht gefunden");

		Receipt receipt = new Receipt(receiptNumber, receivedPaymentDate);

		for (ItemDto ri : ris) {
			ReportItem reportItem = heRepo.findOne(ri.getId());
			receipt.addItem(
					new ReceiptItem(receipt, reportItem
							.getOrderItem(),
							reportItem.getQuantity(), receivedPaymentDate));
		}
		receipt.setCustomerNumber(report.getCustomerNumber());
		return reportRepo.save(receipt);
	}

	@Transactional
	public OrderItem cancelReceivedPayment(String invoiceNo) {
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

//	public ConfirmationReport confirm(String orderNumber) {
//		List<OrderItem> ois = itemRepo.findByOrderNumber(orderNumber);
//		List<ItemDto> ris = convertToReportItems(ois);
//		return confirm(orderNumber, "AB" + orderNumber,  new Date(), ris);
//	}

	@Transactional
	public boolean deleteOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException(
					"Bestellnr. zum l"+Unicode.oUml+"schen nicht gefunden"); 
		orderRepo.delete(order);
		return true;
	}

	@Transactional
	public CancelReport cancelConfirmationReport(String orderConfirmationNumber) {
		Report cr = reportRepo.findByDocumentNumber(orderConfirmationNumber);
		if (cr == null || !(cr instanceof ConfirmationReport))
			throw new IllegalArgumentException("Auftragsnr. nicht gefunden");
		CancelReport cancelReport = createCancelReport(cr);
		return reportRepo.save(cancelReport);
	}

	@Transactional
	public CancelReport cancelDeliveryNotes(String invoiceNumber) {
		Report cr = reportRepo.findByDocumentNumber(invoiceNumber);
		if (cr == null || !(cr instanceof DeliveryNotes))
			throw new IllegalArgumentException(
					"Rechnungs/Lieferscheinnr. nicht gefunden");
		CancelReport cancelReport = createCancelReport(cr);
		return reportRepo.save(cancelReport);
	}

	private CancelReport createCancelReport(Report cr) {
		CancelReport cancelReport = new CancelReport("ABGEBROCHEN-"
				+ cr.getDocumentNumber());
		for (ReportItem he : cr.getItems()) {
			cancelReport.addItem(new CancellationItem(
					cancelReport,
					he.getOrderItem(),
					he.getQuantity(),
					new Date()));
		}
		return cancelReport;
	}

	@Transactional
	public boolean deleteReport(String reportNumber) {
		Report r = reportRepo.findByDocumentNumber(reportNumber);
		if (r == null)
			throw new IllegalArgumentException(
					"Bericht zum l"+Unicode.oUml+"schen nicht gefunden");
		reportRepo.delete(r);
		return true;
	}

	@Transactional
	public Receipt markAsPayed(String invoiceNumber, String receiptNumber,
			Date date) {
		if (reportRepo.findByDocumentNumber(receiptNumber) != null)
			receiptNumber.concat("-2");

		Invoice invoice = retrieveInvoiceSavely(invoiceNumber);
		Receipt receipt = new Receipt(receiptNumber, date);
		ReportItem reportItem = null;
		for (ReportItem ri : invoice.getItems()) {
			receipt.addItem(
					new ReceiptItem(receipt, ri.getOrderItem(), ri
							.getQuantity(), new Date()));
			if (reportItem == null)
				reportItem = ri;
		}
		receipt.setCustomerNumber(reportItem
				.getOrderItem()
				.getOrder()
				.getCustomer()
				.getCustomerNumber());
		return reportRepo.save(receipt);
	}

	private Invoice retrieveInvoiceSavely(String invoiceNumber) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null || !(r instanceof Invoice))
			throw new IllegalArgumentException("Rechnungsnr nicht gefunden");
		return (Invoice) r;
	}

	/**
	 * 
	 * @param invoiceNumber
	 * @param paymentConditions
	 * @param created 
	 * @param shippingItemDtos
	 * @return
	 */
	@Transactional
	public Invoice invoice(String invoiceNumber, String paymentConditions,
			Date created, List<ItemDto> shippingItemDtos) {
		if (reportRepo.findByDocumentNumber(invoiceNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

		Invoice invoice = new Invoice(invoiceNumber, paymentConditions, null);

		Order order = null;
		Address invoiceAddress = null;
		for (ItemDto entry : shippingItemDtos) {
			ReportItem shipEventToBeInvoiced = heRepo.findOne(entry.getId());

			OrderItem orderItemToBeInvoiced = shipEventToBeInvoiced
					.getOrderItem();

			validateQuantity(entry, (ShippingItem) shipEventToBeInvoiced);

			// validate addresses - DRY at deliver method
			Address temp = orderItemToBeInvoiced.getDeliveryHistory().getShippingAddress();
			if (invoiceAddress == null)
				invoiceAddress = temp;
			else if (!invoiceAddress.equals(temp))
				throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");

			invoice.setInvoiceAddress(invoiceAddress);
			
			invoice.addItem(new InvoiceItem(
					invoice,
					shipEventToBeInvoiced.getOrderItem(),
					entry.getQuantityLeft(), // TODO: GUI sets the quantity to
												// this nonsense place
					new Date()));

			if (order == null)
				order = orderItemToBeInvoiced.getOrder();
		}

		invoice.setShippingCosts(new ShippingCostsCalculator()
				.calculate(itemDtoConverterService
						.convertToShippingItems(shippingItemDtos)));
		invoice.setCreated((created == null) ? new Date() : created); 
		// TODO: refactor DRY!
		invoice.setCustomerNumber(order.getCustomer().getCustomerNumber());

		return reportRepo.save(invoice);
	}
	
	// TODO: move to OrderServiceImpl
	@Transactional(readOnly = true)
	public Order retrieveOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		order.getCustomer();
		order.getItems();
		return orderRepo.findByOrderNumber(orderNumber);
	}


}
