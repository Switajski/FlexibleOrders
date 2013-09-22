package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;

public class InvoiceItemServiceImpl extends JpaRepositoryToServiceAdapter<InvoiceItem> implements InvoiceItemService {

	private InvoiceItemRepository iir;
	
	@Autowired
	public InvoiceItemServiceImpl(InvoiceItemRepository jpaRepository) {
		super(jpaRepository);
		this.iir = jpaRepository;
	}
	
	public Page<InvoiceItem> findOpen(Pageable pageable){
		return iir.findOpen(pageable);
	}

	@Override
	public Page<InvoiceItem> findByInvoiceNumber(long invoiceNumber,
			PageRequest pageRequest) {
		return iir.findByInvoiceNumber(invoiceNumber, pageRequest);
	}

	@Override
	public List<InvoiceItem> findByInvoiceNumber(Long invoiceNumber) {
		return iir.findByInvoiceNumber(invoiceNumber);
	}
	
}
