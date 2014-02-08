package de.switajski.priebes.flexibleorders.web.helper;

import java.util.HashMap;

import org.springframework.data.domain.Page;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

public class ExtJsResponseCreator {
	
	public static JsonObjectResponse createResponse(Page<ReportItem> reportItems, boolean byOrder, boolean byOrderNumber) throws Exception{
		JsonObjectResponse response = new JsonObjectResponse();
		long total;
		if (byOrder)
			total = getTotalElementsByOrder(reportItems, byOrderNumber);
		else 
			total = reportItems.getTotalElements();
		
		response.setData(reportItems.getContent());
		response.setTotal(total);
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		return response;
	}

	private static long getTotalElementsByOrder(Page<ReportItem> reportItems, boolean byOrderNumber) {
		HashMap<String, ReportItem> ris = new HashMap<String, ReportItem>();
		for (ReportItem ri :reportItems){
			if (byOrderNumber)
				ris.put(ri.getOrderNumber(), ri);
			else
				ris.put(ri.getDocumentNumber(), ri);

		}
		return (long) ris.size();
	}

	public static JsonObjectResponse createFailedReponse(Exception e) {
		JsonObjectResponse response = new JsonObjectResponse();
		response.setData(e.getMessage());
		response.setMessage(e.getMessage());
		response.setSuccess(false);
		response.setTotal(0);
		return response;
	}

	
}
