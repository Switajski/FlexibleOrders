package de.switajski.priebes.flexibleorders.web;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;

@RequestMapping("/archiveitems")
@Controller
@RooWebScaffold(path = "archiveitems", formBackingObject = ArchiveItem.class)
public class ArchiveItemController extends JsonController<ArchiveItem> {

	@Autowired
	public ArchiveItemController(
			ArchiveItemService crudServiceAdapter) {
		super(crudServiceAdapter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Page<ArchiveItem> findByFilterable(PageRequest pageRequest,
			HashMap<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void resolveDependencies(ArchiveItem entity) {
		// TODO Auto-generated method stub
		
	}
}
