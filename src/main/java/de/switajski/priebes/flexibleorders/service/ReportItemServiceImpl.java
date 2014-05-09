package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Collections;
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

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportItemServiceImpl {

	private OrderRepository orderRepo;
	private ReportRepository reportRepo;
	private ItemDtoConverterService itemDtoConverterService;
	private ReportItemRepository reportItemRepo;

	@Autowired
	public ReportItemServiceImpl(
			OrderRepository orderRepo,
			ReportRepository reportRepo,
			ReportItemRepository reportItemRepo,
			ItemDtoConverterService itemDtoConverterService) {
		this.orderRepo = orderRepo;
		this.reportRepo = reportRepo;
		this.itemDtoConverterService = itemDtoConverterService;
		this.reportItemRepo = reportItemRepo;
	}

	/**
	 * 
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllCompleted(PageRequest pageRequest) {
		Page<ReportItem> toBeCompleted = reportItemRepo.findAllCompleted(
				pageRequest);
		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageRequest,
				itemDtoConverterService
						.convertReportItems(complementMissingReportItems(toBeCompleted.getContent())));
	}

	@Transactional(readOnly=true)
	private List<ReportItem> complementMissingReportItems(
			List<ReportItem> risToBeComplemented) {

		List<ReportItem> allReportItems = new ArrayList<ReportItem>();
		for (Report report:getReportsByReportItems(risToBeComplemented))
			allReportItems.addAll(report.getItems());
		
		return allReportItems;
	}

	private Set<Report> getReportsByReportItems(List<ReportItem> reportItems) {
		if (reportItems.isEmpty()) 
			return Collections.<Report>emptySet();

		Class<?> type = reportItems.iterator().next().getClass();
		
		Set<Report> reportsToBeComplemented = new HashSet<Report>();
		for (ReportItem ri:reportItems) {
			if (!type.isInstance(ri))
				throw new IllegalStateException("ReportItems to complement have different types");
			reportsToBeComplemented.add(ri.getReport());
		}
		return reportsToBeComplemented;
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
		Page<ReportItem> toBeCompleted = reportItemRepo
				.findAllCompletedByCustomer(
						customer.getCustomerNumber(),
						pageRequest);

		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageRequest,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBeCompleted
						.getContent())));

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
		Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmedByCustomer(
				customer,
				pageable);
		return createPageImpl(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmed(PageRequest pageable) {
		Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmed(pageable);
		return createPageImpl(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
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
		Page<ReportItem> toBeShipped = reportItemRepo
				.findAllToBeShippedByCustomerNumber(
						customer.getCustomerNumber(), pageable);
		return createPageImpl(
				toBeShipped.getTotalElements(),
				pageable,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBeShipped
						.getContent())));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeShipped(PageRequest pageable) {
		Page<ReportItem> toBeShipped = reportItemRepo
				.findAllToBeShipped(pageable);
		return createPageImpl(
				toBeShipped.getTotalElements(),
				pageable,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBeShipped
						.getContent())));
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
		Page<ReportItem> toBePaid = reportItemRepo.findAllToBePaidByCustomer(
				customer.getCustomerNumber(),
				pageable);
		return createPageImpl(
				toBePaid.getTotalElements(),
				pageable,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBePaid
						.getContent())));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBePaid(PageRequest pageable) {
		Page<ReportItem> toBePaid = reportItemRepo.findAllToBePaid(pageable);
		return createPageImpl(
				toBePaid.getTotalElements(),
				pageable,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBePaid
						.getContent())));
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
			reportItems.add(itemDtoConverterService.convert(he));
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

	private PageImpl<ItemDto> createPageImpl(Long totalElements,
			Pageable pageable, List<ItemDto> ris) {
		return new PageImpl<ItemDto>(ris, pageable, totalElements);
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeInvoiced(PageRequest pageable) {
		Page<ReportItem> toBeCompleted = reportItemRepo.findAllToBeInvoiced(
				pageable);

		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageable,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBeCompleted
						.getContent())));
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeInvoiced(Customer customer,
			PageRequest pageable) {
		Page<ReportItem> toBeCompleted = reportItemRepo
				.findAllToBeInvoicedByCustomer(
						customer.getCustomerNumber(),
						pageable);

		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageable,
				itemDtoConverterService.convertReportItems(complementMissingReportItems(toBeCompleted
						.getContent())));
	}

}
