package de.switajski.priebes.flexibleorders.web;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.service.DeliveryHistoryService;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
@RequestMapping("/deliveryHistory")
public class DeliveryHistoryController extends ExceptionController {

	@Autowired
	DeliveryHistoryService deliveryHistoryService;

	@Autowired
	OrderRepository orderRepo;

	@RequestMapping(value = "/byReportItemId/{id}")
	public @ResponseBody
	JsonObjectResponse byReportItemId(
			@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts) {
		//TODO: extjs must not call deliveryhistory on application load
		if (id.equals(0L)) 
			return ExtJsResponseCreator.createResponse(Collections.emptyList());
		return ExtJsResponseCreator.createResponse(
				deliveryHistoryService.retrieveByReportItemId(id));
	}

}
