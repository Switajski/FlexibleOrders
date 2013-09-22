package de.switajski.priebes.flexibleorders.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public interface InvoiceItemService extends CrudServiceAdapter<InvoiceItem>{
	Page<InvoiceItem> findOpen(Pageable pageable);

	Page<InvoiceItem> findByInvoiceNumber(long parseLong,
			PageRequest pageRequest);

	List<InvoiceItem> findByInvoiceNumber(Long id);
}
