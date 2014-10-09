package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryHistoryDto;

@Service
public class DeliveryHistoryService {

	@Autowired
	private ReportItemRepository riRepo;
	
	@Autowired
	private OrderItemRepository oiRepo;
	
	@Transactional(readOnly=true)
	public DeliveryHistoryDto retrieveByReportItemId(Long itemDtoId){
		ReportItem ri = riRepo.findOne(itemDtoId);
		if (ri == null){
			throw new IllegalArgumentException("ReportItem with given id " + itemDtoId + " not found");
		}
		return new DeliveryHistoryDto(DeliveryHistory.of(ri));
	}
	
	@Transactional(readOnly=true)
	public DeliveryHistoryDto retrieveByOrderItemId(Long itemDtoId){
		OrderItem oi = oiRepo.findOne(itemDtoId);
		if (oi == null){
			throw new IllegalArgumentException("OrderItem with given id not found");
		}
		return new DeliveryHistoryDto(DeliveryHistory.of(oi));
	}

	/**
	 * FIXME this method returns also DeliveryNotes of 
	 * @param reportItems
	 * @return
	 */
	@Transactional(readOnly=true)
    public Collection<DeliveryNotes> retrieveDeliveryNotesFrom(Report report) {
        DeliveryHistory dh = DeliveryHistory.of(report);
        HashSet<DeliveryNotes> dns = new HashSet<DeliveryNotes>();
        for (ReportItem ri:dh.getReportItems(ShippingItem.class)){
            dns.add((DeliveryNotes) ri.getReport());
        }
        return dns;
    }
	
}
