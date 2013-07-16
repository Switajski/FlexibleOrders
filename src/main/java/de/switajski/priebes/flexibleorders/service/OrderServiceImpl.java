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
	public Long countAll() {
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
	public Page<Order> findByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Order> findByCustomer(Customer customer, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
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

}
