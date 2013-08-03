package de.switajski.priebes.flexibleorders.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;

public interface InvoiceItemService extends CrudServiceAdapter<InvoiceItem>{
	Page<InvoiceItem> findShipped(Pageable pageable);
}
