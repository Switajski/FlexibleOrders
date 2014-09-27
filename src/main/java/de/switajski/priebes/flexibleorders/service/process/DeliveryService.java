package de.switajski.priebes.flexibleorders.service.process;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class DeliveryService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemRepository reportItemRepo;

    @Transactional
    public DeliveryNotes deliver(DeliverParameter deliverParameter) {
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) throw new IllegalArgumentException(
                "Lieferscheinnummer existiert bereits");

        Map<ReportItem, Integer> risWithQty = mapItemDtosToReportItemsWithQty(deliverParameter.agreementItemDtos);
        Address shippingAddress = retrieveOneShippingAddressOrFail(risWithQty.keySet());

        DeliveryNotes deliveryNotes = createNewDeliveryNotes(deliverParameter);

        for (Entry<ReportItem, Integer> riWithQty : risWithQty.entrySet()) {
            ReportItem agreementItem = riWithQty.getKey();
            int qty = riWithQty.getValue();
            OrderItem orderItemToBeDelivered = agreementItem.getOrderItem();

            ServiceHelper.validateQuantity(qty, agreementItem);

            deliveryNotes.addItem(new ShippingItem(
                    deliveryNotes,
                    orderItemToBeDelivered,
                    qty,
                    new Date()));

            deliveryNotes.setShippedAddress(shippingAddress);
        }

        return reportRepo.save(deliveryNotes);
    }

    private Address retrieveOneShippingAddressOrFail(Set<ReportItem> risWithQty) {
        DeliveryHistory dh = new DeliveryHistory(risWithQty);
        if (!dh.hasEqualPurchaseAgreements()) throw new IllegalStateException("Unterschiedliche Kaufvertr" + Unicode.aUml + "ge vorhanden");
        Address shippingAddress = dh.getPurchaseAgreements().iterator().next().getShippingAddress();
        return shippingAddress;
    }

    private DeliveryNotes createNewDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.deliveryNotesNumber);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);
        deliveryNotes.setCustomerNumber(deliverParameter.customerNumber);
        return deliveryNotes;
    }

    private Map<ReportItem, Integer> mapItemDtosToReportItemsWithQty(Collection<ItemDto> itemDtos) {
        Map<ReportItem, Integer> risWithQty = new HashMap<ReportItem, Integer>();
        for (ItemDto agreementItemDto : itemDtos) {
            ReportItem agreementItem = reportItemRepo.findOne(agreementItemDto.id);
            if (agreementItem == null) throw new IllegalArgumentException("Angegebene Position nicht gefunden");
            risWithQty.put(
                    agreementItem,
                    agreementItemDto.quantityLeft); // TODO: GUI sets
                                                    // quanitityToDeliver at
                                                    // this nonsense
                                                    // parameter
        }
        return risWithQty;
    }

}
