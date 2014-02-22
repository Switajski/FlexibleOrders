package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.specification.ItemSpecification;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@Service
public class ReportItemServiceImpl {

	@PersistenceContext
	private EntityManager em;
	
	private OrderItemRepository itemRepo;
	private OrderRepository orderRepo;
	private ReportRepository reportRepo;

	@Autowired
	public ReportItemServiceImpl(OrderItemRepository itemRepo,
			OrderRepository orderRepo,
			ReportRepository reportRepo) {
		this.orderRepo = orderRepo;
		this.itemRepo = itemRepo;
		this.reportRepo = reportRepo;
	}

	/**
	 * 
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllCompleted(Pageable pageRequest, boolean byOrder){
		if (byOrder){
			return extractReportItemsFromOrders(
					orderRepo.findAllCompleted(pageRequest), pageRequest, HandlingEventType.PAID);
		}
		return extractReportItems( 
				itemRepo.findAllConfirmed(pageRequest), pageRequest); 
	}
	
	/**
	 * 
	 * @param customer
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllCompleted(Customer customer, PageRequest pageRequest, boolean byOrder){
		if (byOrder){
			return extractReportItemsFromOrders(
					orderRepo.findAllCompletedByCustomer(customer, pageRequest), pageRequest, HandlingEventType.PAID);
		}
		return extractReportItems(
				itemRepo.findAllCompletedByCustomer(customer, pageRequest), pageRequest);
	}
	
	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllToBeConfirmedByCustomer(Customer customer,
			Pageable pageable, boolean byOrder){
		if (byOrder){
			return extractReportItemsFromOrders(
					orderRepo.findAllToBeConfirmedByCustomer(customer, pageable), pageable, null);
		}
		return extractReportItems(itemRepo.findAllToBeConfirmedByCustomer(customer, pageable), pageable);
	}
	
//	private Page<ReportItem> extractSpecifiedReportItemsFromOrderItems(
//			HandlingEventType type, ToBeConfirmedSpecification spec, Pageable pageable,
//			Page<OrderItem> page) {
//
//		List<ReportItem> ris = new ArrayList<ReportItem>();
//		for (OrderItem oi:page){
//			if (spec.isSatisfiedBy(oi)){
//				if (type== null)
//					ris.add(oi.toReportItem());
//				else {
//					for (HandlingEvent he:oi.getDeliveryHistory())
//						if (he.getType()==type)
//							ris.add(he.toReportItem());
//				}
//
//			}
//		}
//		return new PageImpl<ReportItem>(ris, pageable, ris.size());
//	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllToBeConfirmed(PageRequest pageable,
			boolean byOrder) {
		if (byOrder){
			return extractReportItemsFromOrders(
					orderRepo.findAllToBeConfirmed(pageable), pageable, null);
		}
		return extractReportItems(itemRepo.findAllToBeConfirmed(pageable), 
				pageable);
	}
	
	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllToBeShipped(Customer customer,
			PageRequest pageable, boolean byOrder) {
		Page<FlexibleOrder> orders = orderRepo.findAllToBeShippedByCustomer(customer, pageable);
		if (byOrder){
			return extractReportItemsFromOrders(
					orders, pageable, HandlingEventType.CONFIRM);
		}
		return extractReportItems(itemRepo.findAllToBeShippedByCustomer(customer, pageable), pageable);
	}
	
	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllToBeShipped(PageRequest pageable,
			boolean byOrder) {
		if (byOrder){
			Page<FlexibleOrder> orders = orderRepo.findAllToBeShipped(pageable);
			return extractReportItemsFromOrders(
					orders, pageable, HandlingEventType.CONFIRM);
		}
		return extractReportItems(itemRepo.findAllToBeShipped(pageable), pageable);
	}
	
	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllToBePaid(Customer customer,
			PageRequest pageable, boolean byOrder) {
		
		em.createNamedQuery("");
		if (byOrder){
			return extractReportItemsFromOrders(
					orderRepo.findAllToBePaidByCustomer(customer, pageable), pageable, HandlingEventType.PAID);
		}
		Page<OrderItem> page = itemRepo.findAllToBePaidByCustomer(customer, pageable);
		return extractReportItems(page, pageable);
	}
	
	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	public Page<ReportItem> retrieveAllToBePaid(PageRequest pageable,
			boolean byOrder) {
		if (byOrder){
			return extractReportItemsFromOrders(
					orderRepo.findAllToBePaid(pageable), pageable, HandlingEventType.SHIP);
		}
		return extractReportItems(itemRepo.findAllToBePaid(pageable), pageable);
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
		//if no orders are found return empty list
		if (orders.getSize() < 1) 
			return new PageImpl<String>(new ArrayList<String>());
		
		List<String> ordersList = new ArrayList<String>();
		for (FlexibleOrder order:orders)
			ordersList.add(order.getOrderNumber());
		
		Page<String> result = new PageImpl<String>(
				ordersList, new PageRequest(orders.getSize(), orders.getNumber()+1),
				orders.getTotalElements()
				);
		return result;
	}

	public List<ReportItem> retrieveAllByDocumentNumber(String string) {
		if (string == null)
			throw new IllegalArgumentException("Dokumentennummer nicht angegeben");
		Report report = reportRepo.findByDocumentNumber(string);
		List<ReportItem> reportItems = new ArrayList<ReportItem>();
		for (HandlingEvent he : report.getEvents())
			reportItems.add(he.toReportItem());
		return reportItems;
	}

	public Page<Long> retrieveOrderNumbers() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
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
	
	/**
	 * 
	 * @param orders
	 * @param pageable
	 * @return
	 */
	public static Page<ReportItem> extractReportItemsFromOrders(
			Page<FlexibleOrder> orders, Pageable pageable, HandlingEventType heType) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (FlexibleOrder order:orders){
			for (OrderItem oi:order.getItems()){
				if (heType == null)
					ris.add(oi.toReportItem());
				else if (oi.getAllHesOfType(HandlingEventType.CANCEL).isEmpty())
					ris.addAll(extractRiOnlyWithHe(oi, heType));
				// mark cancelled items as cancelled
				//TODO is a workaround, as long as querying only for not canceled is not working
				else if (!oi.getAllHesOfType(HandlingEventType.CANCEL).isEmpty())
					ris.addAll(extractRiOnlyWihtHeCanceled(oi, heType));
			}
		}
		return new PageImpl<ReportItem>(ris, pageable, countOrders(ris));
	}
	
	private static int countOrders(List<ReportItem> ris){
		Set<String> set = new HashSet<String>();
		for (ReportItem ri : ris)
			set.add(ri.getOrderNumber());
		return set.size();
	}
	
	private static List<ReportItem> extractRiOnlyWihtHeCanceled(
			OrderItem oi, HandlingEventType heType) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (HandlingEvent hi: oi.getDeliveryHistory())
			if (hi.getType().equals(heType))
				ris.add(hi.toCancelledReportItem());
		return ris;
	}

	private static List<ReportItem> extractRiOnlyWithHe(OrderItem oi,
			HandlingEventType heType) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (HandlingEvent hi: oi.getDeliveryHistory())
			if (hi.getType().equals(heType))
				ris.add(hi.toReportItem());
		return ris;
	}

	/**
	 * 
	 * @param type if null, ReportItem will be generated from OrderItem only!
	 * @param spec
	 * @param pageable
	 * @param orders
	 * @return
	 */
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
		return new PageImpl<ReportItem>(ris, pageable, ris.size());
	}

}
