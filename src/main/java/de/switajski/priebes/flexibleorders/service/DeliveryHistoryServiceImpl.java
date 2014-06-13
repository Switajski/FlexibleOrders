package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;

@Service
public class DeliveryHistoryServiceImpl {

	@Autowired
	private ReportItemRepository riRepo;
	
	@Autowired
	private OrderItemRepository oiRepo;
	
	public DeliveryHistory retrieveByReportItemId(Long itemDtoId){
		ReportItem ri = riRepo.findOne(itemDtoId);
		if (ri == null){
			throw new IllegalArgumentException("ReportItem with given id " + itemDtoId + " not found");
		}
		return ri.getOrderItem().getDeliveryHistory();
	}
	
	public DeliveryHistory retrieveByOrderItemId(Long itemDtoId){
		OrderItem oi = oiRepo.findOne(itemDtoId);
		if (oi == null){
			throw new IllegalArgumentException("OrderItem with given id not found");
		}
		return oi.getDeliveryHistory();
	}
}
