package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@RequestMapping("/reportitems")
@Controller
public class ReportItemController extends ExceptionController {

	/**
	 * retrieve reportItems by order or by orderItem 
	 */
	boolean BY_ORDER = true;
//	private static Logger log = Logger.getLogger(ReportItemController.class);

	private ReportItemServiceImpl reportItemService;
	private CustomerRepository customerRepo;
	private OrderServiceImpl orderService;
	
	@Autowired
	public ReportItemController(ReportItemServiceImpl reportitemService, 
			CustomerRepository customerRepo,
			OrderServiceImpl orderService) {
		this.reportItemService = reportitemService;
		this.customerRepo = customerRepo;
		this.orderService = orderService;
	}
	
	@RequestMapping(value = "/ordered", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllToBeConfirmed(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{

		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap = JsonSerializationHelper.deserializeFiltersJson(filters);
		Page<ReportItem> ordered;

		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportItemService.retrieveAllToBeConfirmedByCustomer(customer, pageable, BY_ORDER);
		} else {
			ordered = reportItemService.retrieveAllToBeConfirmed(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, true);
	}
	
	@RequestMapping(value = "/tobeshipped", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllToBeShipped(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
			
		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap = JsonSerializationHelper.deserializeFiltersJson(filters);
		Page<ReportItem> ordered;

		String docNr = "documentNumber";
		if (filterMap.containsKey(docNr))
			ordered = new PageImpl<ReportItem>(
					reportItemService.retrieveAllByDocumentNumber(filterMap.get(docNr)));
		
		else if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			else 
				ordered = reportItemService.retrieveAllToBeShipped(customer, pageable, BY_ORDER);
		} 
		else {
			ordered = reportItemService.retrieveAllToBeShipped(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, false);

	}
	
	@RequestMapping(value = "/tobepaid", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllToBePaid(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
			
		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap;
		if (filters != null)
			filterMap = JsonSerializationHelper.deserializeFiltersJson(filters);
		else filterMap = null;
		
		Page<ReportItem> reportItems;
		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			reportItems = reportItemService.retrieveAllToBePaid(customer, pageable, BY_ORDER);
		} else {
			reportItems = reportItemService.retrieveAllToBePaid(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(reportItems, BY_ORDER, false);

	}
	
	@RequestMapping(value = "/completed", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllCompleted(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
			
		Customer customer = null;
		PageRequest pageable = new PageRequest((page-1), limit);
		HashMap<String, String> filterMap;
		Page<ReportItem> ordered;
		if (filters != null)
			filterMap = JsonSerializationHelper.deserializeFiltersJson(filters);
		else filterMap = null;

		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportItemService.retrieveAllCompleted(customer, pageable, BY_ORDER);
		} else {
			ordered = reportItemService.retrieveAllCompleted(pageable, BY_ORDER);
		}
		return ExtJsResponseCreator.createResponse(ordered, BY_ORDER, false);

	}

	@RequestMapping(value = "/listInvoiceNumbers", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listInvoiceNumbers(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters) throws Exception{
		
		throw new NotImplementedException();
	}
	
}
