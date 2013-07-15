package de.switajski.priebes.flexibleorders.web;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import flexjson.JSONSerializer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/orderitems")
@Controller
@RooWebScaffold(path = "orderitems", formBackingObject = OrderItem.class)
public class OrderItemController {

	@RequestMapping(params = "find=ByBestellung", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindBestellpositionsByBestellung(@RequestParam("bestellnr") String bestellnrString) {
		HttpStatus returnStatus = HttpStatus.OK;
		JsonObjectResponse response = new JsonObjectResponse();

		try {
			long bestellnr =Long.parseLong(bestellnrString); 
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			List<OrderItem> records = orderItemService.findByOrderNumber(bestellnr);
			returnStatus = HttpStatus.OK;
			response.setMessage("All entities retrieved.");
			response.setSuccess(true);
			response.setTotal(records.size());
			response.setData(records);
		} catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			response.setTotal(0L);
			e.printStackTrace();
		}
		//		BestellpositionTransformerSet ts = new BestellpositionTransformerSet();
		JSONSerializer jsonser = new JSONSerializer();
		String returnString = jsonser.serialize(response);
		// Return list of retrieved performance areas
		return new ResponseEntity<String>(returnString, returnStatus);
	}	


}
