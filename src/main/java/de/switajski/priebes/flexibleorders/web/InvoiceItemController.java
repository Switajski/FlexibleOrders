package de.switajski.priebes.flexibleorders.web;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/invoiceitems")
@Controller
@RooWebScaffold(path = "invoiceitems", formBackingObject = InvoiceItem.class)
public class InvoiceItemController extends JsonController<InvoiceItem>{

	@Autowired
	public InvoiceItemController(InvoiceItemService readService) {
		super(readService);
	}

	@Override
	protected void resolveDependencies(InvoiceItem entity) {
		// TODO Auto-generated method stub
		
	}
}
