package de.switajski.priebes.flexibleorders.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;


@Service
@Transactional
public class OrderItemServiceImpl extends JpaRepositoryToServiceAdapter<OrderItem> implements OrderItemService {

	OrderItemRepository orderItemRepository;
	ShippingItemRepository shippingItemRepository;

	@Autowired
	public OrderItemServiceImpl(OrderItemRepository jpaRepository,
			OrderItemRepository orderItemRepository,
			ShippingItemRepository shippingItemRepository) {
		super(jpaRepository);
		this.orderItemRepository = orderItemRepository;
		this.shippingItemRepository = shippingItemRepository;
	}


	public long countAllOrderItems() {
		return orderItemRepository.count();
	}

	public void deleteOrderItem(OrderItem orderItem) {
		orderItemRepository.delete(orderItem);
	}

	public OrderItem findOrderItem(Long id) {
		return orderItemRepository.findOne(id);
	}

	public List<OrderItem> findAllOrderItems() {
		return orderItemRepository.findAll();
	}

	public List<OrderItem> findOrderItemEntries(int firstResult, int maxResults) {
		return orderItemRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
	}

	public void saveOrderItem(OrderItem orderItem) {
		orderItemRepository.save(orderItem);
	}

	public OrderItem updateOrderItem(OrderItem orderItem) {
		return orderItemRepository.save(orderItem);
	}

	@Override
	public List<OrderItem> findByOrderNumber(Long orderNumber) {
		return orderItemRepository.findByOrderNumber(orderNumber);
	}

	@Override
	public Page<OrderItem> findByOrderNumber(Long orderNumber, Pageable pageable) {
		return orderItemRepository.findByOrderNumber(orderNumber, pageable);
	}

	@Override
	public Page<OrderItem> findAll(Pageable pageable) {
		return orderItemRepository.findAll(pageable);
	}

	public Page<OrderItem> findOrdered(Pageable pageable){
		return orderItemRepository.findByStatus(Status.ORDERED, pageable);
	}

	@Override
	public OrderItem findCorresponding(ShippingItem shippingItem) {
		List<OrderItem> orderItems = orderItemRepository.findByOrderNumber(shippingItem.getOrderNumber());
		for (OrderItem oi:orderItems){
			if (oi.getProduct().equals(shippingItem.getProduct())){
				return oi;
			}
		}

		return null;
	}


}
