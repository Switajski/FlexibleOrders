package de.switajski.priebes.flexibleorders.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.service.RooService;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;

//@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.ShippingItem.class })
public interface ShippingItemService extends CrudServiceAdapter<ShippingItem>{
	Page<ShippingItem> findByOrderNumber(Long orderNumber, Pageable pageable);
	Page<ShippingItem> findConfirmed(Pageable pageable);
}
