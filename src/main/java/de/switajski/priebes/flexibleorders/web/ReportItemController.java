package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.service.helper.ReportItemFilterDispatcher;
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

	@Autowired
	private ReportItemServiceImpl reportItemService;
	//TODO: on Controller layer only Services are allowed
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private ReportItemFilterDispatcher filterDispatcher;

	@RequestMapping(value = "/ordered", method = RequestMethod.GET)
	public @ResponseBody
	JsonObjectResponse listAllToBeConfirmed(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters)
			throws Exception {

		Customer customer = null;
		PageRequest pageable = new PageRequest((page - 1), limit);
		HashMap<String, String> filterMap = JsonSerializationHelper
				.deserializeFiltersJson(filters);
		Page<ItemDto> ordered;

		if (filterMap != null && filterMap.containsKey("customer")
				&& filterMap.get("customer") != null) {
			customer = customerRepo.findOne(Long.parseLong(filterMap
					.get("customer")));
			if (customer == null)
				throw new IllegalArgumentException(
						"Kunde mit gegebener Id nicht gefunden");
			ordered = reportItemService.retrieveAllToBeConfirmedByCustomer(
					customer,
					pageable);
		} else {
			ordered = reportItemService.retrieveAllToBeConfirmed(pageable);
		}
		return ExtJsResponseCreator.createResponse(ordered);
	}

	@RequestMapping(value = "/listAllToBeProcessed", method = RequestMethod.GET)
	public @ResponseBody
	JsonObjectResponse listAllToBeProcessed(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters)
			throws Exception {

		Specifications<ReportItem> spec = filterDispatcher
				.dispatchToSpecifications(filters);

		Page<ItemDto> openItems = reportItemService.retrieve(
				new PageRequest((page - 1), limit), spec);
		return ExtJsResponseCreator.createResponse(openItems);

	}

	@RequestMapping(value = "/listInvoiceNumbers", method = RequestMethod.GET)
	public @ResponseBody
	JsonObjectResponse listInvoiceNumbers(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts,
			@RequestParam(value = "filter", required = false) String filters)
			throws Exception {

		throw new NotImplementedException();
	}

}
