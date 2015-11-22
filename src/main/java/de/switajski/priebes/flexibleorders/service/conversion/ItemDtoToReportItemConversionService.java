package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
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

    public List<ItemDto> convertOrderItems(Collection<OrderItem> orderItems) {
        List<ItemDto> items = new ArrayList<ItemDto>();
        for (OrderItem oi : orderItems)
            items.add(convert(oi));
        return items;
    }

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

    @Transactional(readOnly = true)
    public ItemDto convert(OrderItem orderItem) {
        ItemDto item = new ItemDto();
        Order order = orderItem.getOrder();
        if (order != null) {
            item.customer = order.getCustomer().getId();
            item.customerNumber = order
                    .getCustomer()
                    .getCustomerNumber();
            item.customerName = order.getCustomer().getCompanyName();
            item.orderNumber = order.getOrderNumber();
        }
        item.created = orderItem.getCreated();
        item.id = orderItem.getId();
        if (orderItem.getNegotiatedPriceNet() != null) item.priceNet = orderItem.getNegotiatedPriceNet().getValue();
        item.product = orderItem.getProduct().getProductNumber();
        item.productName = orderItem.getProduct().getName();
        item.productType = orderItem.getProduct().getProductType();
        item.status = DeliveryHistory.of(orderItem).provideStatus();
        item.quantity = orderItem.getOrderedQuantity();
        item.quantityLeft = orderItem.toBeConfirmed();
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

    @Transactional(readOnly = true)
    public List<ItemDto> convert(Order order) {
        List<ItemDto> ois = new ArrayList<ItemDto>();
        for (OrderItem orderItem : order.getItems()) {
            ois.add(convert(orderItem));
        }
        return ois;
    }

    public List<ItemDto> convertOrders(Collection<Order> orders) {
        List<ItemDto> items = new ArrayList<ItemDto>();
        for (Order order : orders) {
            items.addAll(convert(order));
        }
        return items;
    }

}
