package de.switajski.priebes.flexibleorders.domain;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.OrderItem.class })
public interface OrderItemService {

	List<OrderItem> findByOrderNumber(long bestellnr);
}
