package de.switajski.priebes.flexibleorders.service;
import org.springframework.roo.addon.layers.service.RooService;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;

@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.InvoiceItem.class })
public interface InvoiceItemService extends CrudServiceAdapter<InvoiceItem>{
	
}
