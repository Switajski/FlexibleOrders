package de.switajski.priebes.flexibleorders.web;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

@RequestMapping("/customers")
@Controller
@RooWebScaffold(path = "customers", formBackingObject = Customer.class)
public class CustomerController {

	private CustomerRepository customerRepo;

	private static Logger log = Logger.getLogger(CustomerController.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleException(IllegalArgumentException ex) {
		//TODO: Exception handling
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Fehler beim Server";
		return ex.getMessage();
	}
	
	@Autowired
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepo = customerRepository;
	}
	
	@RequestMapping(value = "/json", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts){
		JsonObjectResponse response = new JsonObjectResponse();
		Page<Customer> customer = customerRepo.findAll(new PageRequest(page-1, limit));
		response.setData(customer.getContent());
		response.setTotal(customer.getTotalElements());
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);

		return response;
	}


	//TODO: move to ReportController
	@RequestMapping(value = "listitems", produces = "text/html")
    public String confirm(Model uiModel) {
        return "customers/listitems";
    }
	
}
