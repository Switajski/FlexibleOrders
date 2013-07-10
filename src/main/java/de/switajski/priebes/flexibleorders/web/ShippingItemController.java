package de.switajski.priebes.flexibleorders.web;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shippingitems")
@Controller
@RooWebScaffold(path = "shippingitems", formBackingObject = ShippingItem.class)
public class ShippingItemController {
}
