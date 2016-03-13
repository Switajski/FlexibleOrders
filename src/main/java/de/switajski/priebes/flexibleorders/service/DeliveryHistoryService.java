package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryHistoryDto;

@Service
public class DeliveryHistoryService {

    @Autowired
    private ReportItemRepository riRepo;

    @Transactional(readOnly = true)
    public DeliveryHistoryDto retrieveByReportItemId(Long itemDtoId) {
        ReportItem ri = riRepo.findOne(itemDtoId);
        if (ri == null) {
            throw new IllegalArgumentException("ReportItem with given id " + itemDtoId + " not found");
        }
        return new DeliveryHistoryDto(ri.getOrderItem().getReportItems());
    }

}
