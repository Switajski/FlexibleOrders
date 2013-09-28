package de.switajski.priebes.flexibleorders.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;

//@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.ShippingItem.class })
public interface ShippingItemService extends CrudServiceAdapter<ShippingItem>{
	Page<ShippingItem> findByOrderNumber(Long orderNumber, Pageable pageable);
	Page<ShippingItem> findOpen(Pageable pageable);
	Page<ShippingItem> findByOrderConfirmationNumber(Long orderConfirmationNumber, Pageable pageable);
	List<ShippingItem> findByOrderConfirmationNumber(Long id);
	
}
