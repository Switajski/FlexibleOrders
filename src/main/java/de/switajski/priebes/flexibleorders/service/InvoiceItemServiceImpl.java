package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;

public class InvoiceItemServiceImpl extends JpaRepositoryToServiceAdapter<InvoiceItem> implements InvoiceItemService {

	@Autowired
	public InvoiceItemServiceImpl(InvoiceItemRepository jpaRepository) {
		super(jpaRepository);
	}
	
}
