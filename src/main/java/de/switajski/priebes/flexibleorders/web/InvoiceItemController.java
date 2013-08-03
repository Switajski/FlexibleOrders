package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.service.CrudServiceAdapter;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

	@Override
	protected Page<InvoiceItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
