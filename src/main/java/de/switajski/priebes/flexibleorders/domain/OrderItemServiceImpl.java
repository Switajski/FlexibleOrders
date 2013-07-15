package de.switajski.priebes.flexibleorders.domain;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class OrderItemServiceImpl implements OrderItemService {

	@Override
	public List<OrderItem> findByOrderNumber(long orderNumber) {
		return orderItemRepository.findByOrderNumber(orderNumber);
	}

	@Override
	public Page<OrderItem> findByOrderNumber(long orderNumber, Pageable pageable) {
		return orderItemRepository.findByOrderNumber(orderNumber, pageable);
	}
	
}
