package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.SerializationHelper;

@RequestMapping("/reportitems")
@Controller
public class ReportItemController {

	/**
	 * retrieve reportItems by order or by orderItem 
	 */
	boolean BY_ORDER = true;
	private static Logger log = Logger.getLogger(ReportItemController.class);

	private OrderItemRepository itemRepo;
	private ReportRepository reportRepo;
	private ReportItemServiceImpl reportService;
	private CustomerRepository customerRepo;
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleException(Exception ex) {
		//TODO: Exception handling
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Fehler beim Server";
		return ex.getMessage();
	}
	
	@Autowired
	public ReportItemController(ReportRepository reportRepo, 
			OrderItemRepository itemRepo, ReportItemServiceImpl reportService, CustomerRepository customerRepo) {
		this.itemRepo = itemRepo;
		this.reportRepo = reportRepo;
		this.reportService = reportService;
		this.customerRepo = customerRepo;
	}
	
	@RequestMapping(value = "/ordered", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllToBeConfirmed(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{

		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap = SerializationHelper.deserializeFiltersJson(filters);
		Page<ReportItem> ordered;

		if (filterMap.containsKey("customer")){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportService.retrieveAllToBeConfirmed(customer, pageable, BY_ORDER);
		} else {
			ordered = reportService.retrieveAllToBeConfirmed(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, true);
	}
	
	@RequestMapping(value = "/tobeshipped", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllConfirmed(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
			
		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap = SerializationHelper.deserializeFiltersJson(filters);
		Page<ReportItem> ordered;

		if (filterMap.containsKey("customer")){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportService.retrieveAllToBeShipped(customer, pageable, BY_ORDER);
		} else {
			ordered = reportService.retrieveAllToBeShipped(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, false);

	}
	
	@RequestMapping(value = "/tobepaid", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllShipped(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
			
		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap = SerializationHelper.deserializeFiltersJson(filters);
		Page<ReportItem> ordered;

		if (filterMap.containsKey("customer")){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportService.retrieveAllToBePaid(customer, pageable, BY_ORDER);
		} else {
			ordered = reportService.retrieveAllToBePaid(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, false);

	}
	
	@RequestMapping(value = "/completed", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllCompleted(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
			
		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap = SerializationHelper.deserializeFiltersJson(filters);
		Page<ReportItem> ordered;

		if (filterMap.containsKey("customer")){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportService.retrieveAllCompleted(customer, pageable, BY_ORDER);
		} else {
			ordered = reportService.retrieveAllCompleted(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, false);

	}



	
}
