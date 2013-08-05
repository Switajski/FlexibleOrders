package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
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
	
	public Page<ShippingItem> findConfirmed(Pageable pageable){
		return ((ItemRepository<ShippingItem>) jpaRepository).findByStatus(Status.CONFIRMED, pageable);
	}

	@Override
	public Page<ShippingItem> findByOrderConfirmationNumber(
			Long orderConfirmationNumber, Pageable pageable) {
		return ((ItemRepository<ShippingItem>) jpaRepository).findByOrderConfirmationNumber(orderConfirmationNumber, pageable);
	}

	@Override
	public List<ShippingItem> findByOrderConfirmationNumber(
			Long orderConfirmationNumber) {
		return ((ItemRepository<ShippingItem>) jpaRepository).findByOrderConfirmationNumber(orderConfirmationNumber);
	}

	
}
