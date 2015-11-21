package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ShippingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemRepository reportItemRepo;
    @Autowired
    private ItemDtoToReportItemConversionService convService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private ExpectedDeliveryService expectedDeliveryService;

    @Transactional
    public DeliveryNotes ship(DeliverParameter deliverParameter) {
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) throw new IllegalArgumentException(
                "Lieferscheinnummer existiert bereits");

        DeliveryNotes deliveryNotes = createDeliveryNotes(deliverParameter);
        for (ItemDto itemDto : deliverParameter.itemsToBeShipped) {
            convService.mapItemDtos(deliveryNotes, itemDto);
        }

        if (!deliverParameter.ignoreContradictoryExpectedDeliveryDates) {
            expectedDeliveryService.validateExpectedDeliveryDates(deliveryNotes.getItems(), deliveryNotes.getCreated());
        }
        Address shippingAddress = shippingAddressService.retrieveShippingAddressOrFail(deliveryNotes.getItems());
        deliveryNotes.setShippedAddress(shippingAddress);

        return reportRepo.save(deliveryNotes);
    }

    private DeliveryNotes createDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.deliveryNotesNumber);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);
        deliveryNotes.setShippingCosts(deliverParameter.shipment);
        deliveryNotes.setDeliveryMethod(deliverParameter.deliveryMethod);
        deliveryNotes.setShowPrices(deliverParameter.showPricesInDeliveryNotes);
        return deliveryNotes;
    }

}
