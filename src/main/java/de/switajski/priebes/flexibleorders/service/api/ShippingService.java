package de.switajski.priebes.flexibleorders.service.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
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
    private PurchaseAgreementService purchaseAgreementService;
    @Autowired
    private ExpectedDeliveryService expectedDeliveryService;

    /**
     * 
     * @param deliverParameter
     * @return
     * @throws ContradictoryPurchaseAgreementException
     *             if saved purchase agreements are contradictory
     */
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
        Address shippingAddress = purchaseAgreementService.retrieveShippingAddressOrFail(deliveryNotes.getItems());
        deliveryNotes.setShippedAddress(shippingAddress);
        return reportRepo.save(deliveryNotes);
    }

    @Transactional
    public Set<DeliveryNotes> shipMany(DeliverParameter deliverParameter) {
        Set<DeliveryNotes> savedDeliveryNotes = new HashSet<DeliveryNotes>();
        String originalDeliveryNotesNumber = deliverParameter.deliveryNotesNumber;
        deliverParameter.packageNumber = null;

        Map<String, List<ItemDto>> packages = new HashMap<String, List<ItemDto>>();
        for (ItemDto itemToBeShipped : deliverParameter.itemsToBeShipped) {
            String packageNumber = itemToBeShipped.packageNumber;
            if (packageNumber == null) packageNumber = "";
            if (packages.get(packageNumber) == null) {
                packages.put(packageNumber, new ArrayList<ItemDto>());
            }
            packages.get(packageNumber).add(itemToBeShipped);
        }

        for (String packageNumber : packages.keySet()) {
            deliverParameter.itemsToBeShipped = packages.get(packageNumber);
            String suffix = packageNumber.equals("") ? "" : "-" + packageNumber;
            deliverParameter.deliveryNotesNumber = originalDeliveryNotesNumber.concat(suffix);
            deliverParameter.packageNumber = packageNumber;
            savedDeliveryNotes.add(ship(deliverParameter));
        }
        return savedDeliveryNotes;
    }

    private DeliveryNotes createDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.deliveryNotesNumber);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);
        deliveryNotes.setShippingCosts(deliverParameter.shipment);
        deliveryNotes.setDeliveryMethod(deliverParameter.deliveryMethod);
        deliveryNotes.setShowPrices(deliverParameter.showPricesInDeliveryNotes);
        deliveryNotes.setPackageNumber(deliverParameter.packageNumber);
        deliveryNotes.setTrackNumber(deliverParameter.trackNumber);
        return deliveryNotes;
    }

}
