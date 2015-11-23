package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class DeliveryNotesToDtoConversionService {

    @Autowired
    ReportToDtoConversionService reportToDtoConversionService;
    @Autowired
    ShippingAddressService shippingAddressService;
    @Autowired
    CustomerDetailsService customerDetailsService;
    @Autowired
    PurchaseAgreementService purchaseAgreementService;

    @Transactional(readOnly = true)
    public ReportDto toDto(DeliveryNotes report) {
        ReportDto dto = reportToDtoConversionService.toDto(report);
        dto.shippingSpecific_trackNumber = report.getTrackNumber();
        dto.shippingSpecific_packageNumber = report.getPackageNumber();
        dto.showPricesInDeliveryNotes = report.isShowPrices();
        return dto;
    }

}
