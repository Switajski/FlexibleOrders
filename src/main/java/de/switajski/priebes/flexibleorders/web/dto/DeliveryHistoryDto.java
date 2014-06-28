package de.switajski.priebes.flexibleorders.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

@JsonAutoDetect
public class DeliveryHistoryDto extends ArrayList<Map<String,Object>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeliveryHistoryDto(DeliveryHistory deliveryHistory) {
		this.add(createChild(null, deliveryHistory.toString(), true));
		for (ReportItem ri :deliveryHistory.getItemsSorted()){
			Map<String, Object> child = createChild(
					ri.getId(), 
					ri.getReport().getDocumentNumber() + ": " + ri.getQuantity(), 
					true);
			this.add(child);
		}
	}

	private Map<String, Object> createChild(Long id, String text, boolean leaf) {
		Map<String, Object> child = new HashMap<String, Object>();
		child.put("id", id);
		child.put("text", text);
		child.put("leaf", true);
		return child;
	}
	
}
