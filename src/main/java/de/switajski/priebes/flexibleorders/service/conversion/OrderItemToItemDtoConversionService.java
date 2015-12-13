package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class OrderItemToItemDtoConversionService {

    @Transactional(readOnly = true)
    public ItemDto convert(OrderItem orderItem) {
        ItemDto item = new ItemDto();
        Order order = orderItem.getOrder();
        item.documentNumber = order.getOrderNumber();
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

    @Transactional(readOnly = true)
    public List<ItemDto> convert(Order order) {
        List<ItemDto> ois = new ArrayList<ItemDto>();
        for (OrderItem orderItem : order.getItems()) {
            ois.add(convert(orderItem));
        }
        return ois;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> convertOrders(Collection<Order> orders) {
        List<ItemDto> items = new ArrayList<ItemDto>();
        for (Order order : orders) {
            items.addAll(convert(order));
        }
        return items;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> convertOrderItems(Collection<OrderItem> orderItems) {
        List<ItemDto> items = new ArrayList<ItemDto>();
        for (OrderItem oi : orderItems)
            items.add(convert(oi));
        return items;
    }

}
