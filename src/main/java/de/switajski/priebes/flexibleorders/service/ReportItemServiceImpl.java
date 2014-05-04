package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.specification.ItemSpecification;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportItemServiceImpl {

	private OrderRepository orderRepo;
	private ReportRepository reportRepo;
	private ItemDtoConverterService itemDtoConverterService;

	@Autowired
	public ReportItemServiceImpl(
			OrderRepository orderRepo,
			ReportRepository reportRepo,
			ItemDtoConverterService itemDtoConverterService) {
		this.orderRepo = orderRepo;
		this.reportRepo = reportRepo;
		this.itemDtoConverterService = itemDtoConverterService;
	}

	/**
	 * 
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllCompleted(PageRequest pageRequest) {
		return extractReportItemsFromReports(
				reportRepo.findAllCompleted(pageRequest),
				pageRequest,
				ReportItemType.PAID);
	}

	/**
	 * 
	 * @param customer
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllCompleted(Customer customer,
			PageRequest pageRequest) {
		return extractReportItemsFromReports(
				reportRepo.findAllCompletedByCustomer(
						customer.getCustomerNumber(),
						pageRequest),
				pageRequest,
				ReportItemType.PAID);
	}

	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmedByCustomer(Customer customer,
			Pageable pageable) {
		return extractReportItemsFromOrders(
				orderRepo.findAllToBeConfirmedByCustomer(customer, pageable),
				pageable,
				null);
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmed(PageRequest pageable) {
		return extractReportItemsFromOrders(
				orderRepo.findAllToBeConfirmed(pageable), pageable, null);
	}

	/**
	 * retrieves all report item to be shipped. Paging is fixed on reports.
	 * 
	 * @param customer
	 * @param pageable
	 * @return empty page if none found
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeShipped(Customer customer,
			PageRequest pageable) {
		Page<Report> reports = reportRepo.findAllToBeShippedByCustomerNumber(
				customer.getCustomerNumber(), pageable);
		return extractReportItemsFromReports(
				reports, pageable, ReportItemType.CONFIRM);
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeShipped(PageRequest pageable) {
		Page<Report> orders = reportRepo.findAllToBeShipped(pageable);
		return extractReportItemsFromReports(
				orders, pageable, ReportItemType.CONFIRM);
	}

	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBePaid(Customer customer,
			PageRequest pageable) {
		return extractReportItemsFromReports(
				reportRepo.findAllToBePaidByCustomer(
						customer.getCustomerNumber(),
						pageable),
				pageable,
				ReportItemType.INVOICE);
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBePaid(PageRequest pageable) {
		// TODO: I messed it up with types - move it to Specification
		return extractReportItemsFromReports(
				reportRepo.findAllToBePaid(pageable),
				pageable,
				ReportItemType.INVOICE);
	}

	@Transactional(readOnly = true)
	public List<String> retrieveOrderNumbersLike(String orderNumber) {
		List<Order> orders = orderRepo.findByOrderNumberLike(orderNumber);
		return extractOrderNumbers(orders);
	}

	private List<String> extractOrderNumbers(List<Order> orders) {
		List<String> orderNumbers = new ArrayList<String>();
		for (Order order : orders) {
			orderNumbers.add(order.getOrderNumber());
		}
		return orderNumbers;
	}

	@Transactional(readOnly = true)
	public Page<String> retrieveOrderNumbersByCustomer(Customer customer,
			PageRequest pageRequest) {

		Page<Order> orders = orderRepo.findByCustomer(customer, pageRequest);
		Page<String> result = extractOrderNumber(orders);
		return result;
	}

	private Page<String> extractOrderNumber(Page<Order> orders) {
		// if no orders are found return empty list
		if (orders.getSize() < 1)
			return new PageImpl<String>(new ArrayList<String>());

		List<String> ordersList = new ArrayList<String>();
		for (Order order : orders)
			ordersList.add(order.getOrderNumber());

		Page<String> result = new PageImpl<String>(
				ordersList, new PageRequest(
						orders.getSize(),
						orders.getNumber() + 1),
				orders.getTotalElements()
				);
		return result;
	}

	@Transactional(readOnly = true)
	public List<ItemDto> retrieveAllByDocumentNumber(String string) {
		if (string == null)
			throw new IllegalArgumentException(
					"Dokumentennummer nicht angegeben");
		Report report = reportRepo.findByDocumentNumber(string);
		List<ItemDto> reportItems = new ArrayList<ItemDto>();
		for (ReportItem he : report.getItems())
			reportItems.add(he.toItemDto());
		return reportItems;
	}

	// TODO: move to OrderServiceImpl
	@Transactional(readOnly = true)
	public Order retrieveOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		order.getCustomer();
		order.getItems();
		return orderRepo.findByOrderNumber(orderNumber);
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> extractReportItems(
			Page<OrderItem> ois, Pageable pageable) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (OrderItem oi : ois) {
			ris.add(itemDtoConverterService.convert(oi));
		}
		return new PageImpl<ItemDto>(ris, pageable, ois.getTotalElements());
	}

	@Transactional(readOnly = true)
	public static Page<ItemDto> extractSpecifiedReportItems(
			ReportItemType type, ItemSpecification spec,
			Pageable pageable, Page<OrderItem> ois) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (OrderItem oi : ois) {
			if (spec.isSatisfiedBy(oi)) {
				for (ReportItem he : oi.getReportItems())
					if (he.getType() == type)
						ris.add(he.toItemDto());
			}
		}
		return new PageImpl<ItemDto>(ris, pageable, ois.getTotalElements());
	}

	/**
	 * 
	 * @param orders
	 * @param pageable
	 * @return empty page if orders empty
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> extractReportItemsFromOrders(
			Page<Order> orders, Pageable pageable, ReportItemType heType) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (Order order : orders) {
			for (OrderItem oi : order.getItems()) {
				if (heType == null)
					ris.add(itemDtoConverterService.convert(oi));
				else
					ris.addAll(itemDtoConverterService.convertToReportItems(
							oi,
							heType));
			}
		}
		return new PageImpl<ItemDto>(ris, pageable, orders.getTotalElements());
	}

	private Page<ItemDto> extractReportItemsFromReports(Page<Report> reports,
			PageRequest pageable, ReportItemType heType) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (Report report : reports) {
			for (ReportItem ri : report.getItems()) {
				if (heType == null)
					ris.add(ri.toItemDto());
				else
					ris.addAll(itemDtoConverterService.convertToReportItems(
							ri.getOrderItem(),
							heType));
			}
		}
		return new PageImpl<ItemDto>(ris, pageable, reports.getTotalElements());
	}

	/**
	 * 
	 * @param type
	 *            if null, ReportItem will be generated from OrderItem only!
	 * @param spec
	 * @param pageable
	 * @param orders
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> extractSpecifiedReportItemsFromOrders(
			ReportItemType type, ItemSpecification spec,
			Pageable pageable, Page<Order> orders) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (Order order : orders)
			for (OrderItem oi : order.getItems()) {
				if (spec.isSatisfiedBy(oi)) {
					if (type == null)
						ris.add(itemDtoConverterService.convert(oi));
					else {
						for (ReportItem he : oi.getReportItems())
							if (he.getType() == type)
								ris.add(he.toItemDto());
					}

				}
			}
		return new PageImpl<ItemDto>(ris, pageable, ris.size());
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeInvoiced(PageRequest pageable) {
		return extractReportItemsFromReports(
				reportRepo.findAllToBeInvoiced(pageable),
				pageable,
				ReportItemType.SHIP);
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeInvoiced(Customer customer,
			PageRequest pageable) {
		return extractReportItemsFromReports(
				reportRepo.findAllToBeInvoicedByCustomer(
						customer.getCustomerNumber(),
						pageable),
				pageable,
				ReportItemType.SHIP);
	}

}
