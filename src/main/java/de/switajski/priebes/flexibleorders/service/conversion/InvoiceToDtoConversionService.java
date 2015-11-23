package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class InvoiceToDtoConversionService {

    @Autowired
    ReportToDtoConversionService reportToDtoConversionService;

    @Autowired
    PurchaseAgreementService purchaseAgreementService;

    @Transactional(readOnly = true)
    public ReportDto toDto(Invoice invoice) {
        ReportDto dto = reportToDtoConversionService.toDto(invoice);
        dto.shippingSpecific_shippingCosts = invoice.getShippingCosts();
        dto.orderConfirmationSpecific_paymentConditions = purchaseAgreementService.retrieveSingle(invoice).getPaymentConditions();
        dto.invoiceSpecific_billing = invoice.getBilling();
        dto.invoiceSpecific_discountText = invoice.getDiscountText();
        dto.invoiceSpecific_discountRate = invoice.getDiscountRate();
        return dto;
    }

}
