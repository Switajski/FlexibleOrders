package de.switajski.priebes.flexibleorders.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.OrderItem.class })
public interface OrderItemService {

	List<OrderItem> findByOrderNumber(long orderNumber);

	Page<OrderItem> findByOrderNumber(long orderNumber, Pageable pageable);
}
