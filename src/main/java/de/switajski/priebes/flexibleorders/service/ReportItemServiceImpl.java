package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.repository.specification.ReceiptItemCompletedSpec;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportItemServiceImpl {

	private OrderRepository orderRepo;
	private ReportRepository reportRepo;
	private ReportItemRepository reportItemRepo;
	private ReportItemToItemDtoPageConverterService pageConverterService;
	@Autowired
	private ItemDtoConverterService itemDtoConverterService;

	@Autowired
	public ReportItemServiceImpl(
			OrderRepository orderRepo,
			ReportRepository reportRepo,
			ReportItemRepository reportItemRepo,
			ReportItemToItemDtoPageConverterService reportItemPageService) {
		this.orderRepo = orderRepo;
		this.reportRepo = reportRepo;
		this.reportItemRepo = reportItemRepo;
		this.pageConverterService = reportItemPageService;
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
		return pageConverterService.createPage(
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
		return pageConverterService.createPage(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
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

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieve(PageRequest pageRequest, Specification<ReportItem> spec){
		Page<ReportItem> openReportItems = reportItemRepo.findAll(spec, pageRequest);
		if (spec instanceof ReceiptItemCompletedSpec)
			return pageConverterService.createWithWholeReports(pageRequest, openReportItems);
		return pageConverterService.createWithWholeNonCompletedReports(pageRequest, openReportItems);
	}

}
