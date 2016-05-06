package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class OrderItemToItemDtoConversionService {

    @Transactional(readOnly = true)
    public ItemDto convert(OrderItem orderItem) {
        ItemDto item = new ItemDto();
        Order order = orderItem.getOrder();
        item.setDocumentNumber(order.getOrderNumber());
        if (order != null) {
            item.setCustomer(order.getCustomer().getId());
            item.setCustomerNumber(order
                    .getCustomer()
                    .getCustomerNumber());
            item.setCustomerName(order.getCustomer().getCompanyName());
            item.setOrderNumber(order.getOrderNumber());
        }
        item.setCreated(orderItem.getCreated());
        item.setId(orderItem.getId());
        if (orderItem.getNegotiatedPriceNet() != null) item.setPriceNet(orderItem.getNegotiatedPriceNet().getValue());
        item.setProduct(orderItem.getProduct().getProductNumber());
        item.setProductName(orderItem.getProduct().getName());
        item.setProductType(orderItem.getProduct().getProductType());
        item.setQuantity(orderItem.getOrderedQuantity());
        item.setQuantityLeft(orderItem.toBeConfirmed());
        item.setAdditionalInfo(orderItem.getAdditionalInfo());
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
