package de.switajski.priebes.flexibleorders.web.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.helper.StringUtil;

@JsonAutoDetect
public class DeliveryHistoryDto extends ArrayList<Map<String, Object>> {

    private static final long serialVersionUID = 1L;

    public DeliveryHistoryDto(Set<ReportItem> deliveryHistory) {
        this.add(createChild(
                StringUtil.concatWithCommas(orderNumbers(deliveryHistory)),
                createOrderString(deliveryHistory),
                true));

        for (ReportItem ri : sortByCreatedDate(deliveryHistory)) {
            Map<String, Object> child = createChild(
                    ri.getReport().getDocumentNumber(),
                    ri.getReport().getDocumentNumber() + ": "
                            + ri.getQuantity(),
                    true);
            this.add(child);
        }
    }

    private List<ReportItem> sortByCreatedDate(Set<ReportItem> deliveryHistory) {
        List<ReportItem> list = new ArrayList<>();
        list.addAll(deliveryHistory);
        Collections.sort(list, (ReportItem r1, ReportItem r2) -> r1.getCreated().compareTo(r2.getCreated()));
        return list;
    }

    private Set<String> orderNumbers(Set<ReportItem> deliveryHistory) {
        return deliveryHistory
                .stream()
                .map(ri -> ri.getOrderItem().getOrder().getOrderNumber())
                .collect(Collectors.toSet());
    }

    private Map<String, Object> createChild(String id, String text, boolean leaf) {
        Map<String, Object> child = new HashMap<String, Object>();
        child.put("id", id);
        child.put("text", text);
        child.put("leaf", true);
        return child;
    }

    public String createOrderString(Set<ReportItem> ris) {
        OrderItem oi = ris.iterator().next().getOrderItem();
        return StringUtil.concatWithCommas(orderNumbers(ris)) +
                ": " + oi.getOrderedQuantity() + " x " + oi.getProduct().getName();
    }

}
