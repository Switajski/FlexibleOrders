package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;

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
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@RequestMapping("/reportitems")
@Controller
public class ReportItemController extends ExceptionController {

	/**
	 * retrieve reportItems by order or by orderItem 
	 */
	boolean BY_ORDER = true;

	private ReportItemServiceImpl reportItemService;
	private CustomerRepository customerRepo;
	
	@Autowired
	public ReportItemController(ReportItemServiceImpl reportitemService, 
			CustomerRepository customerRepo) {
		this.reportItemService = reportitemService;
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
		HashMap<String, String> filterMap = JsonSerializationHelper.deserializeFiltersJson(filters);
		Page<ItemDto> ordered;

		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportItemService.retrieveAllToBeConfirmedByCustomer(customer, pageable);
		} else {
			ordered = reportItemService.retrieveAllToBeConfirmed(pageable);
		}
		return ExtJsResponseCreator.createResponse(ordered);
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
		Page<ItemDto> ordered;

		String docNr = "documentNumber";
		if (filterMap.containsKey(docNr))
			ordered = new PageImpl<ItemDto>(
					reportItemService.retrieveAllByDocumentNumber(filterMap.get(docNr)));
		
		else if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			else 
				ordered = reportItemService.retrieveAllToBeShipped(customer, pageable);
		} 
		else {
			ordered = reportItemService.retrieveAllToBeShipped(pageable);
		}
		return ExtJsResponseCreator.createResponse(ordered);

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
		
		Page<ItemDto> reportItems;
		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			reportItems = reportItemService.retrieveAllToBePaid(customer, pageable );
		} else {
			reportItems = reportItemService.retrieveAllToBePaid(pageable );
		}
		return ExtJsResponseCreator.createResponse(reportItems);

	}
	
	@RequestMapping(value = "/tobeinvoiced", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAllToBeInvoiced(@RequestParam(value = "page", required = true) Integer page,
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
		
		Page<ItemDto> reportItems;
		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			reportItems = reportItemService.retrieveAllToBeInvoiced(customer, pageable );
		} else {
			reportItems = reportItemService.retrieveAllToBeInvoiced(pageable );
		}
		return ExtJsResponseCreator.createResponse(reportItems);

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
		Page<ItemDto> ordered;
		if (filters != null)
			filterMap = JsonSerializationHelper.deserializeFiltersJson(filters);
		else filterMap = null;

		if (filterMap != null && filterMap.containsKey("customer") && filterMap.get("customer")!=null){ 
			customer = customerRepo.findOne(Long.parseLong(filterMap.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException("Kunde mit gegebener Id nicht gefunden");
			ordered = reportItemService.retrieveAllCompleted(customer, pageable);
		} else {
			ordered = reportItemService.retrieveAllCompleted(pageable);
		}
		return ExtJsResponseCreator.createResponse(ordered);

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
