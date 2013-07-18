package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Override
	public long countAll() {
		return orderItemRepository.countAllOrders();
	}
	
	@Override
	public Page<Long> getOrderNumbersByCustomer(Customer customer, Pageable pageable) {
		return orderItemRepository.getAllOrderNumbers(customer, pageable);
	}

	public Page<Order> findAll(Pageable pageable){
		Page<Long> orderNumbers = orderItemRepository.getAllOrderNumbers(pageable);
		
		List<Order> orders = new ArrayList<Order>();
		for (Long orderNumber:orderNumbers){
			orders.add(new Order(orderItemRepository.findByOrderNumber(orderNumber)));
		}
		Page<Order> ordersPage = new PageImpl<Order>(orders, pageable, orderNumbers.getTotalElements());
		return ordersPage;
		
	}

	@Override
	public Order find(Long orderNumber) {
		return getOrder(orderNumber);
	}
	
	private Order getOrder(Long orderNumber){
		List<OrderItem> ois = orderItemRepository.findByOrderNumber(orderNumber);
		Order order = new Order(ois);
		return order;
	}

	@Override
	public List<Order> findAll() {
		ArrayList<Order> orders = new ArrayList<Order>();
		for (Long orderNumber: orderItemRepository.getAllOrderNumbers())
			orders.add(getOrder(orderNumber));
		return orders;
	}

	@Override
	public void save(Order t) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void delete(Order t) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void update(Order t) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public List<Order> findByCustomer(Customer customer) {
		ArrayList<Order> orders = new ArrayList<Order>();
		for (Long orderNumber: orderItemRepository.getAllOrderNumbers(customer))
			orders.add(getOrder(orderNumber));
		return orders;
	}

	@Override
	public Page<Order> findByCustomer(Customer customer, Pageable pageable) {
		ArrayList<Order> orders = new ArrayList<Order>();
		Page<Long> orderNumbers = orderItemRepository.getAllOrderNumbers(customer, pageable);
		for (Long orderNumber: orderNumbers)
			orders.add(getOrder(orderNumber));
		Page<Order> pages = new PageImpl<Order>(orders, pageable, orderNumbers.getSize());
		return pages;
	}

}
