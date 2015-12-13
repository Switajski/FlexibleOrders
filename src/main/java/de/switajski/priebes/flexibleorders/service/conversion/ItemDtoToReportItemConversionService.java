package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ItemDtoToReportItemConversionService {

    @Autowired
    private ReportItemRepository reportItemRepo;

    public ItemDto createShippingCosts(final DeliveryNotes deliveryNotes) {
        ItemDto item = new ItemDto();
        item.created = deliveryNotes.getCreated();
        item.documentNumber = deliveryNotes.getDocumentNumber();
        item.deliveryNotesNumber = deliveryNotes.getDocumentNumber();
        item.priceNet = deliveryNotes.getShippingCosts().getValue();
        item.quantity = 1;
        item.quantityLeft = 1;

        DeliveryMethod deliveryMethod = deliveryNotes.getDeliveryMethod();
        if (deliveryMethod != null && deliveryMethod.getName() != null) item.productName = deliveryMethod.getName();
        else item.productName = "Versandkosten";
        Customer customer = deliveryNotes.getCustomerSafely();
        item.customerNumber = customer.getCustomerNumber();
        item.customerName = customer.getCompanyName();
        item.productType = ProductType.SHIPPING;

        return item;
    }

    @Transactional
    public void mapItemDtos(DeliveryNotes deliveryNotes, ItemDto itemDto) {
        if (itemDto.productType != ProductType.SHIPPING) {
            int qty = itemDto.quantityLeft;
            ReportItem itemToBeShipped = reportItemRepo.findOne(itemDto.id);
            if (itemToBeShipped == null) {
                throw new IllegalArgumentException("Angegebene Position nicht gefunden");
            }

            OrderItem orderItemToBeDelivered = itemToBeShipped.getOrderItem();
            QuantityUtility.validateQuantity(qty, itemToBeShipped);
            if (itemDto.pending == false) {
                ShippingItem shippingItem = new ShippingItem(
                        deliveryNotes,
                        orderItemToBeDelivered,
                        qty,
                        new Date());
                shippingItem.setPredecessor(itemToBeShipped);
                shippingItem.setPackageNumber(itemDto.packageNumber);
                shippingItem.setTrackNumber(itemDto.trackNumber);
                deliveryNotes.addItem(shippingItem);
            }
            else {
                deliveryNotes.addItem(new PendingItem(
                        deliveryNotes,
                        orderItemToBeDelivered,
                        qty,
                        new Date()));
            }
        }
    }

    @Transactional(readOnly = true)
    public Map<ReportItem, Integer> mapItemDtosToReportItemsWithQty(Collection<ItemDto> itemDtos) {
        Map<ReportItem, Integer> risWithQty = new HashMap<ReportItem, Integer>();
        for (ItemDto itemDtoToBeProcessed : itemDtos) {
            if (itemDtoToBeProcessed.productType != ProductType.SHIPPING) {
                ReportItem agreementItem = reportItemRepo.findOne(itemDtoToBeProcessed.id);
                if (agreementItem == null) throw new IllegalArgumentException("Angegebene Position nicht gefunden");
                risWithQty.put(
                        agreementItem,
                        itemDtoToBeProcessed.quantityLeft);
            }
        }
        return risWithQty;
    }

}
