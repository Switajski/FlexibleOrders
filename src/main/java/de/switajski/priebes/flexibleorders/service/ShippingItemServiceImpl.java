package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

public class ShippingItemServiceImpl extends JpaRepositoryToServiceAdapter<ShippingItem> implements ShippingItemService {

	ShippingItemRepository sir;
	
	@Autowired
	public ShippingItemServiceImpl(ShippingItemRepository jpaRepository) {
		super(jpaRepository);
		this.sir = jpaRepository;
	}

	@Override
	public Page<ShippingItem> findByOrderNumber(Long orderNumber,
			Pageable pageable) {
		return this.sir.findByOrderNumber( orderNumber,
				pageable);
		
	}
	
	public Page<ShippingItem> findOpen(Pageable pageable){
		return sir.findOpen(pageable);
	}

	@Override
	public Page<ShippingItem> findByOrderConfirmationNumber(
			Long orderConfirmationNumber, Pageable pageable) {
		return sir.findByOrderConfirmationNumber(orderConfirmationNumber, pageable);
	}

	@Override
	public List<ShippingItem> findByOrderConfirmationNumber(
			Long orderConfirmationNumber) {
		return sir.findByOrderConfirmationNumber(orderConfirmationNumber);
	}

	
}
