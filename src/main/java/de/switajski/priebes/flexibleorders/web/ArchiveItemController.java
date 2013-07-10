package de.switajski.priebes.flexibleorders.web;
import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/archiveitems")
@Controller
@RooWebScaffold(path = "archiveitems", formBackingObject = ArchiveItem.class)
public class ArchiveItemController {
}
