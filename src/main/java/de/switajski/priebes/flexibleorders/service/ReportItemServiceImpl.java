package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.specification.CompletedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ItemSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ToBeConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ToBePaidSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ToBeShippedSpecification;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@Service
public class ReportItemServiceImpl {

	private OrderItemRepository itemRepo;
	private OrderRepository orderRepo;
	private CustomerRepository customerRepo;

	@Autowired
	public ReportItemServiceImpl(OrderItemRepository itemRepo,
			OrderRepository orderRepo,
			CustomerRepository customerRepo) {
		this.orderRepo = orderRepo;
		this.itemRepo = itemRepo;
		this.customerRepo = customerRepo;
	}

	public Page<ReportItem> retrieveAllCompleted(Pageable pageRequest, boolean byOrder){
		HandlingEventType confirmType = HandlingEventType.CONFIRM;
		CompletedSpecification confirmedSpec = new CompletedSpecification();
		
		if (byOrder){
			return extractSpecifiedReportItemsFromOrders(confirmType, 
					confirmedSpec, pageRequest, orderRepo.findAll(pageRequest));
		}
		return extractSpecifiedReportItems(
				HandlingEventType.CONFIRM, 
				new ConfirmedSpecification(false, false), 
				pageRequest, itemRepo.findAllConfirmed(pageRequest)); 
	}
	
	public Page<ReportItem> retrieveAllCompleted(Customer customer, PageRequest pageRequest, boolean byOrder){
		HandlingEventType confirmType = HandlingEventType.CONFIRM;
		CompletedSpecification confirmedSpec = new CompletedSpecification();
		
		if (byOrder){
			return extractSpecifiedReportItemsFromOrders(confirmType, 
					confirmedSpec, pageRequest, orderRepo.findByCustomer(customer, pageRequest));
		}
		return extractSpecifiedReportItems(
				HandlingEventType.CONFIRM, 
				new ConfirmedSpecification(false, false), 
				pageRequest, itemRepo.findByCustomer(customer, pageRequest));
	}
	
	public Page<ReportItem> retrieveAllToBeConfirmed(Customer customer,
			Pageable pageable, boolean byOrder){
		ToBeConfirmedSpecification spec = new ToBeConfirmedSpecification();
		if (byOrder){
			Page<FlexibleOrder> page = orderRepo.findByCustomer(customer, pageable);
			return extractSpecifiedReportItemsFromOrders(null, spec, pageable, page);
		}
		Page<OrderItem> page = itemRepo.findByCustomer(customer, pageable);
		return extractSpecifiedReportItems(null, spec, pageable, page);
	}
	
	public Page<ReportItem> retrieveAllToBeConfirmed(PageRequest pageable,
			boolean byOrder) {
		ToBeConfirmedSpecification spec = new ToBeConfirmedSpecification();
		if (byOrder){
			return extractSpecifiedReportItemsFromOrders(null, spec, pageable, orderRepo.findAll(pageable));
		}
		return extractSpecifiedReportItems(null, spec, pageable, itemRepo.findAll(pageable));
	}
	
	public Page<ReportItem> retrieveAllToBeShipped(Customer customer,
			PageRequest pageable, boolean byOrder) {
				
		ToBeShippedSpecification spec = new ToBeShippedSpecification();
		if (byOrder){
			Page<FlexibleOrder> page = orderRepo.findByCustomer(customer, pageable);
			return extractSpecifiedReportItemsFromOrders(null, spec, pageable, page);
		}
		Page<OrderItem> page = itemRepo.findByCustomer(customer, pageable);
		return extractSpecifiedReportItems(null, spec, pageable, page);
	}
	
	public Page<ReportItem> retrieveAllToBeShipped(PageRequest pageable,
			boolean byOrder) {
		ToBeShippedSpecification spec = new ToBeShippedSpecification();
		if (byOrder){
			return extractSpecifiedReportItemsFromOrders(null, spec, pageable, orderRepo.findAll(pageable));
		}
		return extractSpecifiedReportItems(null, spec, pageable, itemRepo.findAll(pageable));
	}
	
	public Page<ReportItem> retrieveAllToBePaid(Customer customer,
			PageRequest pageable, boolean byOrder) {
				
		ToBePaidSpecification spec = new ToBePaidSpecification();
		if (byOrder){
			Page<FlexibleOrder> page = orderRepo.findByCustomer(customer, pageable);
			return extractSpecifiedReportItemsFromOrders(null, spec, pageable, page);
		}
		Page<OrderItem> page = itemRepo.findByCustomer(customer, pageable);
		return extractSpecifiedReportItems(null, spec, pageable, page);
	}
	
	public Page<ReportItem> retrieveAllToBePaid(PageRequest pageable,
			boolean byOrder) {
		ToBePaidSpecification spec = new ToBePaidSpecification();
		if (byOrder){
			return extractSpecifiedReportItemsFromOrders(null, spec, pageable, orderRepo.findAll(pageable));
		}
		return extractSpecifiedReportItems(null, spec, pageable, itemRepo.findAll(pageable));
	}

	public List<String> retrieveOrderNumbersLike(String orderNumber) {
		List<FlexibleOrder> orders = orderRepo.findByOrderNumberLike(orderNumber);
		return extractOrderNumbers(orders);
	}

	private List<String> extractOrderNumbers(List<FlexibleOrder> orders) {
		List<String> orderNumbers = new ArrayList<String>();
		for (FlexibleOrder order:orders){
			orderNumbers.add(order.getOrderNumber());
		}
		return orderNumbers;
	}

	public Page<String> retrieveOrderNumbersByCustomer(Customer customer,
			PageRequest pageRequest) {
				
		Page<FlexibleOrder> orders = orderRepo.findByCustomer(customer, pageRequest);
		Page<String> result = extractOrderNumber(orders);
		return result;
	}

	private Page<String> extractOrderNumber(Page<FlexibleOrder> orders) {
		List<String> ordersList = new ArrayList<String>();
		for (FlexibleOrder order:orders)
			ordersList.add(order.getOrderNumber());
		Page<String> result = new PageImpl<String>(
				ordersList, 
				new PageRequest(orders.getNumber(), orders.getTotalPages()),
				orders.getTotalElements()
				);
		return result;
	}

	public Page<ReportItem> retrieveAllByDocumentNumber(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Long> retrieveOrderNumbers() {
		// TODO Auto-generated method stub
		return null;
	}

	public FlexibleOrder retrieveOrder(String orderNumber) {
		FlexibleOrder order = orderRepo.findByOrderNumber(orderNumber);
		order.getCustomer();
		order.getItems();
		return orderRepo.findByOrderNumber(orderNumber);
	}
	
	public static Page<ReportItem> extractReportItems(
			Page<OrderItem> ois, Pageable pageable) {			
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (OrderItem oi:ois){
			ris.add(oi.toReportItem());
		}
		return new PageImpl<ReportItem>(ris, pageable, ois.getTotalElements());
	}
	
	public static Page<ReportItem> extractSpecifiedReportItems(HandlingEventType type, ItemSpecification spec,
			Pageable pageable, Page<OrderItem> ois) {			
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (OrderItem oi:ois){
			if (spec.isSatisfiedBy(oi)){
				for (HandlingEvent he:oi.getDeliveryHistory())
					if (he.getType()==type)
						ris.add(he.toReportItem());
			}
		}
		return new PageImpl<ReportItem>(ris, pageable, ois.getTotalElements());
	}
	
	public static Page<ReportItem> extractReportItemsFromOrders(
			Page<FlexibleOrder> orders, Pageable pageable) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (FlexibleOrder order:orders){
			for (OrderItem oi:order.getItems()){
				ris.add(oi.toReportItem());
			}
		}
		return new PageImpl<ReportItem>(ris, pageable, orders.getTotalElements());
	}
	
	public static Page<ReportItem> extractSpecifiedReportItemsFromOrders(HandlingEventType type, ItemSpecification spec,
			Pageable pageable, Page<FlexibleOrder> orders) {			
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (FlexibleOrder order:orders)
			for (OrderItem oi:order.getItems()){
				if (spec.isSatisfiedBy(oi)){
					if (type== null)
						ris.add(oi.toReportItem());
					else {
						for (HandlingEvent he:oi.getDeliveryHistory())
							if (he.getType()==type)
								ris.add(he.toReportItem());
					}

				}
			}
		return new PageImpl<ReportItem>(ris, pageable, orders.getTotalElements());
	}

}
