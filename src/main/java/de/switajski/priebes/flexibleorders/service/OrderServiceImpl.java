package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculator;
import de.switajski.priebes.flexibleorders.application.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.application.specification.ShippedSpecification;
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
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
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
	public Order order(Long customerId, String orderNumber,
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
		for (ItemDto ri : reportItems) {
			CatalogProduct cProduct = cProductRepo.findByProductNumber(ri
					.getProduct());
			if (cProduct == null)
				throw new IllegalArgumentException("Artikelnr nicht gefunden");

			OrderItem oi = new OrderItem(
					order,
					cProduct.toProduct(),
					ri.getQuantity());
			oi
					.setNegotiatedPriceNet(new Amount(
							ri.getPriceNet(),
							Currency.EUR));
			oi.setCreated(new Date());
			order.addOrderItem(oi);
		}

		return orderRepo.save(order);
	}

	@Transactional
	public ConfirmationReport confirm(String orderNumber, String confirmNumber,
			Date expectedDelivery, List<ItemDto> orderItems) {
		validateConfirm(orderNumber, confirmNumber, orderItems);

		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException("Bestellnr. nicht gefunden");

		Address address = order.getCustomer().getAddress();

		ConfirmationReport cr = new ConfirmationReport(confirmNumber,
				address, address, new ConfirmedSpecification(false, false));
		cr.setExpectedDelivery(expectedDelivery);
		// TODO: Refactor: DRY!
		cr.setCustomerNumber(order.getCustomer().getCustomerNumber());

		for (ItemDto entry : orderItems) {
			OrderItem oi = itemRepo.findOne(entry.getId());
			if (oi == null)
				throw new IllegalArgumentException(
						"Bestellposition nicht gefunden");
			cr.addItem(new ConfirmationItem(cr, ReportItemType.CONFIRM, oi,
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
			Address shippingAddress, Amount shipment,
			List<ItemDto> confirmEvents) {
		if (reportRepo.findByDocumentNumber(deliveryNotesNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

		DeliveryNotes deliveryNotes = new DeliveryNotes(
				deliveryNotesNumber,
				new ShippedSpecification(false, false),
				shippingAddress,
				shipment);

		Order firstOrder = null;
		for (ItemDto entry : confirmEvents) {
			ReportItem confirmEventToBeDelivered = heRepo
					.findOne(entry.getId());
			OrderItem orderItemToBeDelivered = confirmEventToBeDelivered
					.getOrderItem();

			validateQuantity(
					entry,
					orderItemToBeDelivered,
					ReportItemType.CONFIRM);

			deliveryNotes.addItem(new ShippingItem(
					deliveryNotes,
					ReportItemType.SHIP,
					orderItemToBeDelivered,
					entry.getQuantityLeft(), // TODO: GUI sets
												// quanitityToDeliver at this
												// nonsense parameter
					new Date()));

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
			OrderItem orderItemToBeDelivered,
			ReportItemType type) {
		Integer quantityToDeliver = entry.getQuantityLeft();
		if (quantityToDeliver == null)
			throw new IllegalArgumentException("Menge nicht angegeben");
		if (quantityToDeliver < 1)
			throw new IllegalArgumentException("Menge kleiner eins");
		if (quantityToDeliver > new QuantityLeftCalculator().calculate(
				orderItemToBeDelivered,
				type))
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
			ReportItem he = heRepo.findOne(ri.getId());
			receipt.addItem(
					new ReceiptItem(receipt, ReportItemType.PAID, he
							.getOrderItem(),
							he.getQuantity(), receivedPaymentDate));
		}
		// TODO: refactor
		receipt.setCustomerNumber(report
				.getItems()
				.iterator()
				.next()
				.getOrderItem()
				.getOrder()
				.getCustomer()
				.getCustomerNumber());
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

	public ConfirmationReport confirm(String orderNumber) {
		List<OrderItem> ois = itemRepo.findByOrderNumber(orderNumber);
		List<ItemDto> ris = convertToReportItems(ois);
		return confirm(orderNumber, "AB" + orderNumber, new Date(), ris);
	}

	private List<ItemDto> convertToReportItems(List<OrderItem> ois) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (OrderItem oi : ois) {
			ris.add(itemDtoConverterService.convert(oi));
		}
		return ris;
	}

	@Transactional
	public boolean deleteOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException(
					"Bestellnr. zum löschen nicht gefunden");
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
					ReportItemType.CANCEL,
					he.getOrderItem(),
					he.getQuantity(),
					new Date()));
		}
		return cancelReport;
	}

	@Transactional
	public boolean deleteReport(String invoiceNumber) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null)
			throw new IllegalArgumentException(
					"Bericht zum löschen nicht gefunden");
		reportRepo.delete(r);
		return true;
	}

	@Transactional
	public Receipt markAsPayed(String invoiceNumber, String receiptNumber,
			Date date) {
		Report r = reportRepo.findByDocumentNumber(invoiceNumber);
		if (r == null || !(r instanceof Invoice))
			throw new IllegalArgumentException("Rechnungsnr nicht gefunden");

		Invoice invoice = (Invoice) r;
		Receipt receipt = new Receipt(receiptNumber, date);
		ReportItem reportItem = null;
		for (ReportItem he : invoice.getItems()) {
			receipt.addItem(
					new ReceiptItem(receipt, ReportItemType.PAID, he
							.getOrderItem(), he.getQuantity(), new Date()));
			if (reportItem == null)
				reportItem = he;
		}
		receipt.setCustomerNumber(reportItem
				.getOrderItem()
				.getOrder()
				.getCustomer()
				.getCustomerNumber());
		return reportRepo.save(receipt);
	}

	/**
	 * 
	 * @param invoiceNumber
	 * @param paymentConditions
	 * @param invoiceAddress
	 * @param shippingItemDtos
	 * @return
	 */
	@Transactional
	public Invoice invoice(String invoiceNumber, String paymentConditions,
			Address invoiceAddress,
			List<ItemDto> shippingItemDtos) {
		if (reportRepo.findByDocumentNumber(invoiceNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

		Invoice invoice = new Invoice(invoiceNumber, paymentConditions,
				invoiceAddress);

		Order order = null;
		for (ItemDto entry : shippingItemDtos) {
			ReportItem shipEventToBeInvoiced = heRepo.findOne(entry.getId());

			OrderItem orderItemToBeInvoiced = shipEventToBeInvoiced
					.getOrderItem();

			validateQuantity(entry, orderItemToBeInvoiced, ReportItemType.SHIP);

			invoice.addItem(new InvoiceItem(
					invoice,
					ReportItemType.INVOICE,
					shipEventToBeInvoiced.getOrderItem(),
					entry.getQuantityLeft(), // TODO: GUI sets the quantity to
												// this nonsense place
					new Date()));

			if (order == null)
				order = orderItemToBeInvoiced.getOrder();
		}

		invoice.setShippingCosts(new ShippingCostsCalculator()
				.calculate(itemDtoConverterService.convertToShippingItems(shippingItemDtos)));
		// TODO: refactor DRY!
		invoice.setCustomerNumber(order.getCustomer().getCustomerNumber());

		return reportRepo.save(invoice);
	}

}
