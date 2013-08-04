package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;

public class InvoiceItemServiceImpl extends JpaRepositoryToServiceAdapter<InvoiceItem> implements InvoiceItemService {

	@Autowired
	public InvoiceItemServiceImpl(InvoiceItemRepository jpaRepository) {
		super(jpaRepository);
	}
	
	public Page<InvoiceItem> findShipped(Pageable pageable){
		return ((ItemRepository<InvoiceItem>) this.jpaRepository).findByStatus(Status.SHIPPED, pageable);
	}

	@Override
	public Page<InvoiceItem> findByInvoiceNumber(long parseLong,
			PageRequest pageRequest) {
		return ((ItemRepository<InvoiceItem>) this.jpaRepository).findByInvoiceNumber(parseLong, pageRequest);
	}
	
}
