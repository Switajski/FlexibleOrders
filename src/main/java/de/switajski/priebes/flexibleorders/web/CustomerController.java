package de.switajski.priebes.flexibleorders.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.service.CustomerService;

@RequestMapping("/customers")
@Controller
@RooWebScaffold(path = "customers", formBackingObject = Customer.class)
public class CustomerController extends JsonController<Customer>{

	@Autowired
	public CustomerController(CustomerService crudServiceAdapter) {
		super(crudServiceAdapter);
	}
	
}
