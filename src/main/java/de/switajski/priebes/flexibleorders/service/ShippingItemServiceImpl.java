package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

public class ShippingItemServiceImpl extends JpaRepositoryToServiceAdapter<ShippingItem> implements ShippingItemService {

	@Autowired
	public ShippingItemServiceImpl(ShippingItemRepository jpaRepository) {
		super(jpaRepository);
	}

	@Override
	public Page<ShippingItem> findByOrderNumber(Long orderNumber,
			Pageable pageable) {
		return ((ShippingItemRepository) this.jpaRepository).findByOrderNumber( orderNumber,
				pageable);
		
	}
	
}
