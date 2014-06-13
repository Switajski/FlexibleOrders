package de.switajski.priebes.flexibleorders.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

@JsonAutoDetect
public class DeliveryHistoryDto extends ArrayList<Map<String,Object>>{

	public DeliveryHistoryDto(DeliveryHistory deliveryHistory) {
		for (ReportItem ri :deliveryHistory.getItems()){
			Map<String, Object> child = new HashMap<String, Object>();
			child.put("id", ri.getId());
			child.put("text", ri.getReport().getDocumentNumber() + ": " + ri.getQuantity());
			child.put("leaf", true);
			this.add(child);
		}
	}
	
}
