package de.switajski.priebes.flexibleorders.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.service.StatisticsService;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@RequestMapping("/statistics")
@Controller
public class StatisticsController extends ExceptionController{

	@Autowired
	private StatisticsService statisticsService;
	
	@RequestMapping(value = "/openAmount", method = RequestMethod.GET)
	public @ResponseBody JsonObjectResponse shippedAmount(
			@RequestParam String state) throws Exception {
		return ExtJsResponseCreator.createResponse(statisticsService.calculateOpenAmount(state));
	}
}
