package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.CancelReport;
import de.switajski.priebes.flexibleorders.domain.report.CancellationItem;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.DeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.ReportingService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
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
public class OrderService {

	@Autowired
	private DeliveryMethodRepository deliveryMethodRepo;
	@Autowired
	private ReportRepository reportRepo;
	@Autowired
	private OrderItemRepository orderItemRepo;
	@Autowired
	private CatalogProductRepository cProductRepo;
	@Autowired
	private ReportItemRepository reportItemRepo;
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private ItemDtoConverterService itemDtoConverterService;
	@Autowired
	private ReportingService reportingService;

	/**
	 * Creates initially an order with its order items
	 * @param orderParameter
	 * 
	 * @return created order, when successfully persisted
	 */
	@Transactional
	public Order order(OrderParameter orderParameter) {
		if (orderParameter.customerNumber == null || orderParameter.orderNumber == null || orderParameter.reportItems.isEmpty())
			throw new IllegalArgumentException();
		if (orderRepo.findByOrderNumber(orderParameter.orderNumber) != null)
			throw new IllegalArgumentException("Bestellnr existiert bereits");
		Customer customer = customerRepo.findByCustomerNumber(orderParameter.customerNumber);
		if (customer == null)
			throw new IllegalArgumentException(
					"Keinen Kunden mit gegebener Kundennr. gefunden");

		Order order = new Order(
				customer,
				OriginSystem.FLEXIBLE_ORDERS,
				orderParameter.orderNumber);
		order.setCreated((orderParameter.created == null) ? new Date() : orderParameter.created);
		if (orderParameter.expectedDelivery != null){
			AgreementDetails ad = new AgreementDetails();
			ad.setExpectedDelivery(orderParameter.expectedDelivery);
			order.setAgreementDetails(ad);
		}
		
		for (ItemDto ri : orderParameter.reportItems) {
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
	
	@Transactional
	public OrderConfirmation confirm(ConfirmParameter confirmParameter) {
		Address invoiceAddress = confirmParameter.invoiceAddress;
				Address shippingAddress = confirmParameter.shippingAddress;
		validateConfirm(confirmParameter.orderNumber, confirmParameter.confirmNumber, confirmParameter.orderItems, shippingAddress);

		Order order = orderRepo.findByOrderNumber(confirmParameter.orderNumber);
		if (order == null)
			throw new IllegalArgumentException("Bestellnr. nicht gefunden");

		Customer cust = order.getCustomer();
		Address address = (cust.getInvoiceAddress() == null) ? cust.getShippingAddress() : cust.getInvoiceAddress();
		shippingAddress = (shippingAddress.isComplete()) ? shippingAddress : address;
		invoiceAddress = (invoiceAddress.isComplete()) ? invoiceAddress : address;
		
		AgreementDetails agreeDet = new AgreementDetails();
		agreeDet.setShippingAddress(shippingAddress);
		agreeDet.setInvoiceAddress(invoiceAddress);
		agreeDet.setExpectedDelivery(confirmParameter.expectedDelivery);
		agreeDet.setDeliveryMethod(deliveryMethodRepo.findOne(confirmParameter.deliveryMethodNo));

		OrderConfirmation cr = new OrderConfirmation();
		cr.setDocumentNumber(confirmParameter.confirmNumber);
		cr.setAgreementDetails(agreeDet);
		cr.setCustomerNumber(cust.getCustomerNumber());
		cr.setCustomerDetails(cust.getDetails());

		for (ItemDto entry : confirmParameter.orderItems) {
			OrderItem oi = orderItemRepo.findOne(entry.getId());
			if (oi == null)
				throw new IllegalArgumentException(
						"Bestellposition nicht gefunden");
			cr.addItem(new ConfirmationItem(cr, oi,
					entry.getQuantityLeft(), new Date()));
		}
		return reportRepo.save(cr);
	}
	
	@Transactional
	public OrderAgreement agree(String orderConfirmationNo, String orderAgreementNo){
		OrderConfirmation oc = reportingService.retrieveOrderConfirmation(orderConfirmationNo);
		if (oc == null)
			throw new IllegalArgumentException("Auftragsbest"+Unicode.aUml+"tigung mit angegebener Nummer nicht gefunden");
		
		OrderAgreement oa = new OrderAgreement();
		oa.setDocumentNumber(orderAgreementNo);
		oa.setAgreementDetails(oc.getAgreementDetails());
		oa.setCustomerDetails(oc.getCustomerDetails());
		oa.setOrderConfirmationNumber(orderConfirmationNo);
		oa = takeOverConfirmationItems(oc, oa);
		
		return reportRepo.save(oa);
	}

	private OrderAgreement takeOverConfirmationItems(OrderConfirmation oc,
			OrderAgreement oa) {
		for (ReportItem ri:oc.getItems()){
			AgreementItem ai = new AgreementItem();
			ai.setQuantity(QuantityCalculator.calculateLeft(ri));
			ai.setOrderItem(ri.getOrderItem());
			//TODO: bidirectional management of relationship
			ai.setReport(oa);
			oa.addItem(ai);
		}
		return oa;
	}

	private void validateConfirm(String orderNumber, String confirmNumber,
			List<ItemDto> orderItems, Address shippingAddress) {
		if (reportRepo.findByDocumentNumber(confirmNumber) != null)
			throw new IllegalArgumentException("Auftragsnr. " + confirmNumber
					+ " besteht bereits");
		if (orderItems.isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben");
		if (orderNumber == null)
			throw new IllegalArgumentException("Keine Bestellnr angegeben");
		if (confirmNumber == null)
			throw new IllegalArgumentException("Keine AB-nr angegeben");
		if (shippingAddress == null)
			throw new IllegalArgumentException("Keine Lieferadresse angegeben");
		for (ItemDto item : orderItems) {
			if (item.getId() == null)
				throw new IllegalArgumentException("Position hat keine Id");
		}
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
	private OrderItem createShippingCosts(Amount shipment, Order order) {
		Product product = new Product();
		product.setProductType(ProductType.SHIPPING);
		product.setName("Versand");

		OrderItem shipOi = new OrderItem(order, product, 1);
		shipOi.setNegotiatedPriceNet(shipment);

		return orderItemRepo.save(shipOi);
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
			ReportItem reportItem = reportItemRepo.findOne(ri.getId());
			receipt.addItem(
					new ReceiptItem(receipt, reportItem
							.getOrderItem(),
							reportItem.getQuantity(), receivedPaymentDate));
		}
		receipt.setCustomerNumber(report.getCustomerNumber());
		return reportRepo.save(receipt);
	}

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
	public CancelReport cancelReport(String reportNo) {
		Report cr = reportRepo.findByDocumentNumber(reportNo);
		if (cr == null)
			throw new IllegalArgumentException("Angegebene Dokumentennummer nicht gefunden");
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

	@Transactional(readOnly = true)
	public Order retrieveOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		order.getCustomer();
		order.getItems();
		return orderRepo.findByOrderNumber(orderNumber);
	}

}
