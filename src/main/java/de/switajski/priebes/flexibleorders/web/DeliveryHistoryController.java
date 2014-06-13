package de.switajski.priebes.flexibleorders.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.service.DeliveryHistoryServiceImpl;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryHistoryDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
@RequestMapping("/deliveryHistory")
public class DeliveryHistoryController extends ExceptionController {

	@Autowired
	DeliveryHistoryServiceImpl deliveryHistoryService;
	
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
		return ExtJsResponseCreator.createResponse(
				deliveryHistoryService.retrieveByReportItemId(id));
	}

	@RequestMapping(value = "/byOrderItemId/{id}")
	public @ResponseBody
	JsonObjectResponse byOrderItemId(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts) {
		return ExtJsResponseCreator.createResponse(deliveryHistoryService
				.retrieveByOrderItemId(id));
	}
	
	@RequestMapping(value = "/test")
	public @ResponseBody
	JsonObjectResponse test(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts) {
		return ExtJsResponseCreator.createResponse(new DeliveryHistoryDto(
				orderRepo.findAll().iterator().next().getItems().iterator().next().getDeliveryHistory()));
	}
	
	@RequestMapping(value = "/test2")
	public @ResponseBody JsonObjectResponse test2(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts) {
		
		ArrayList<Map<String,Object>> treeList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> child = new HashMap<String, Object>();
        child.put("id", "id");
        child.put("text", "text");
        child.put("leaf", true);
        
        ArrayList<Map<String,Object>> treeList2 = new ArrayList<Map<String, Object>>();
        Map<String, Object> child3 = new HashMap<String, Object>();
        child3.put("id", "id");
        child3.put("text", "text");
        child3.put("leaf", true);
        treeList2.add(child3);
        
		Map<String, Object> child2 = new HashMap<String, Object>();
        child2.put("id", "id2");
        child2.put("text", "text2");
        child2.put("leaf", false);
        child2.put("expanded", true);
        child2.put("children", treeList2);

        treeList.add(child2);
        treeList.add(child);
		
		return ExtJsResponseCreator.createResponse(treeList);
				
//				"{'success': true,"
//				+ "'data': "
//				+ "["
//				+ "{'text': 'AAA', 'leaf': true},"
//				+ "{'text': 'BBB', 'leaf': true},"
//				+ "{'text': 'CCC', 'leaf': false, "
//				+ "'children': [{'text': 'DDD', 'leaf': false}]"
//				+ "}"
//				+ "]"
//				+ "}";
	}
}
