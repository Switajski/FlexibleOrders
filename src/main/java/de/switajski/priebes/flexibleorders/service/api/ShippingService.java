package de.switajski.priebes.flexibleorders.service.api;

import java.time.ZoneId;
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
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ShippingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoToReportItemConversionService convService;
    @Autowired
    private PurchaseAgreementReadService purchaseAgreementService;
    @Autowired
    private ExpectedDeliveryService expectedDeliveryService;

    /**
     * 
     * @param valid
     *            deliverParameter
     * @return created delivery notes if successful
     * @throws ContradictoryPurchaseAgreementException
     */
    @Transactional
    public DeliveryNotes ship(DeliverParameter deliverParameter) throws ContradictoryPurchaseAgreementException {

        DeliveryNotes deliveryNotes = createDeliveryNotes(deliverParameter);
        for (ItemDto itemDto : deliverParameter.getItems()) {
            convService.mapItemDtos(deliveryNotes, itemDto);
        }

        if (!deliverParameter.isIgnoreContradictoryExpectedDeliveryDates()) {
            expectedDeliveryService.validateExpectedDeliveryDates(deliveryNotes.getItems(), deliveryNotes.getCreated());
        }
        Address shippingAddress = purchaseAgreementService.retrieveShippingAddressOrFail(deliveryNotes.getItems());
        deliveryNotes.setShippedAddress(shippingAddress);
        return reportRepo.save(deliveryNotes);
    }

    @Transactional
    public Set<DeliveryNotes> shipMany(DeliverParameter deliverParameter) throws ContradictoryPurchaseAgreementException {
        Set<DeliveryNotes> savedDeliveryNotes = new HashSet<DeliveryNotes>();
        String originalDeliveryNotesNumber = deliverParameter.getDeliveryNotesNumber();
        deliverParameter.setPackageNumber(null);

        Map<String, List<ItemDto>> packages = new HashMap<String, List<ItemDto>>();
        for (ItemDto itemToBeShipped : deliverParameter.getItems()) {
            String packageNumber = itemToBeShipped.getPackageNumber();
            if (packageNumber == null) packageNumber = "";
            if (packages.get(packageNumber) == null) {
                packages.put(packageNumber, new ArrayList<ItemDto>());
            }
            packages.get(packageNumber).add(itemToBeShipped);
        }

        for (String packageNumber : packages.keySet()) {
            deliverParameter.setItems(packages.get(packageNumber));
            String suffix = packageNumber.equals("") ? "" : "-" + packageNumber;
            deliverParameter.setDeliveryNotesNumber(originalDeliveryNotesNumber.concat(suffix));
            deliverParameter.setPackageNumber(packageNumber);
            savedDeliveryNotes.add(ship(deliverParameter));
        }
        return savedDeliveryNotes;
    }

    private DeliveryNotes createDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.getDeliveryNotesNumber());
        deliveryNotes.setCreated(deliverParameter.getCreated() == null ? new Date() : Date.from(deliverParameter.getCreated().atStartOfDay().atZone(
                ZoneId.systemDefault()).toInstant()));
        deliveryNotes.setShippingCosts(new Amount(deliverParameter.getShipment(), Currency.EUR));
        deliveryNotes.setDeliveryMethod(deliverParameter.getDeliveryMethod());
        deliveryNotes.setShowPrices(deliverParameter.isShowPricesInDeliveryNotes());
        deliveryNotes.setPackageNumber(deliverParameter.getPackageNumber());
        deliveryNotes.setTrackNumber(deliverParameter.getTrackNumber());
        return deliveryNotes;
    }

}
