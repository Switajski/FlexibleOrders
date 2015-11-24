package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderConfirmationToDtoConversionService {

    @Autowired
    ReportToDtoConversionService reportToDtoConversionService;
    @Autowired
    PurchaseAgreementService puchaseAgreementService;

    @Transactional(readOnly = true)
    public ReportDto toDto(OrderConfirmation orderConfirmation) {
        ReportDto dto = reportToDtoConversionService.toDto(orderConfirmation);
        if (orderConfirmation.isAgreed()) dto.orderConfirmationNumber = orderConfirmation.getOrderAgreementNumber();
        dto.orderConfirmationSpecific_paymentConditions = puchaseAgreementService.retrieveSingle(orderConfirmation.getItems()).getPaymentConditions();
        dto.orderConfirmationSpecific_oldestOrderDate = oldestOrderDate(orderConfirmation.getItems());
        return dto;
    }

    private Date oldestOrderDate(Set<ReportItem> items) {
        Date oldestOrderDate = null;
        for (ReportItem item : items) {
            Date created = item.getOrderItem().getOrder().getCreated();
            if (oldestOrderDate == null) {
                oldestOrderDate = created;
            }
            else {
                if (oldestOrderDate.after(created)) {
                    oldestOrderDate = created;
                }
            }
        }
        return oldestOrderDate;
    }

}
