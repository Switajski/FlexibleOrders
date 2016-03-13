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
        item.setCreated(deliveryNotes.getCreated());
        item.setDocumentNumber(deliveryNotes.getDocumentNumber());
        item.setDeliveryNotesNumber(deliveryNotes.getDocumentNumber());
        item.setPriceNet(deliveryNotes.getShippingCosts().getValue());
        item.setQuantity(1);
        item.setQuantityLeft(1);

        DeliveryMethod deliveryMethod = deliveryNotes.getDeliveryMethod();
        if (deliveryMethod != null && deliveryMethod.getName() != null) item.setProductName(deliveryMethod.getName());
        else item.setProductName("Versandkosten");
        Customer customer = deliveryNotes.getCustomerSafely();
        item.setCustomerNumber(customer.getCustomerNumber());
        item.setCustomerName(customer.getCompanyName());
        item.setProductType(ProductType.SHIPPING);

        return item;
    }

    @Transactional
    public ReportItem createReportItem(ItemDto itemDto) {
        if (itemDto.getProductType() != ProductType.SHIPPING) {
            int qty = itemDto.getQuantityLeft();
            ReportItem itemToBeShipped = reportItemRepo.findOne(itemDto.getId());
            if (itemToBeShipped == null) {
                throw new IllegalArgumentException("Angegebene Position nicht gefunden");
            }

            OrderItem orderItemToBeDelivered = itemToBeShipped.getOrderItem();
            QuantityUtility.validateQuantity(qty, itemToBeShipped);
            if (itemDto.isPending() == false) {
                ShippingItem shippingItem = new ShippingItem(
                        null,
                        orderItemToBeDelivered,
                        qty,
                        new Date());
                shippingItem.setPredecessor(itemToBeShipped);
                shippingItem.setPackageNumber(itemDto.getPackageNumber());
                shippingItem.setTrackNumber(itemDto.getTrackNumber());
                return shippingItem;
            }
            else {
                return new PendingItem(
                        null,
                        orderItemToBeDelivered,
                        qty,
                        new Date());
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Map<ReportItem, Integer> mapItemDtosToReportItemsWithQty(Collection<ItemDto> itemDtos) {
        Map<ReportItem, Integer> risWithQty = new HashMap<ReportItem, Integer>();
        for (ItemDto itemDtoToBeProcessed : itemDtos) {
            if (itemDtoToBeProcessed.getProductType() != ProductType.SHIPPING) {
                ReportItem agreementItem = reportItemRepo.findOne(itemDtoToBeProcessed.getId());
                if (agreementItem == null) throw new IllegalArgumentException("Angegebene Position nicht gefunden");
                risWithQty.put(
                        agreementItem,
                        itemDtoToBeProcessed.getQuantityLeft());
            }
        }
        return risWithQty;
    }
}
