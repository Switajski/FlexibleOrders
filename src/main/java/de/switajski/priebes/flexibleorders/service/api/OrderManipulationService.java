package de.switajski.priebes.flexibleorders.service.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.service.conversion.OrderItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ManipulateOrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class OrderManipulationService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderItemToItemDtoConversionService convService;

    @Transactional
    public Set<ItemDto> manipulateOrder(ManipulateOrderParameter param) {
        UpdateOrder order = new UpdateOrder(orderRepo.findByOrderNumber(param.getOrderNumber()));

        order.removeOthers(param.getItems());
        for (ItemDto newItem : param.getItems()) {
            if (order.has(newItem)) {
                order.update(newItem);
            }
            else {
                order.add(newItem);
            }
        }
        orderRepo.save(order.order);

        return order.order.getItems().stream()
                .map(i -> convService.convert(i))
                .collect(Collectors.toSet());
    }

    private class UpdateOrder {
        Order order;

        UpdateOrder(Order order) {
            this.order = order;
        }

        public void add(ItemDto newItem) {
            order.addOrderItem(new OrderItem(newItem));
        }

        public void update(ItemDto newItem) {
            OrderItem oi = getById(newItem.getId());
            oi.copyValuesFrom(newItem);
        }

        private OrderItem getById(Long id) {
            return order.getItems().stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst()
                    .get();
        }

        boolean has(ItemDto item) {
            for (OrderItem oi : order.getItems()) {
                if (item.getId() == null) return false;
                if (item.getId().equals(oi.getId())) {
                    return true;
                }
            }
            return false;
        }

        void removeOthers(Collection<ItemDto> others) {
            Iterator<OrderItem> itr = order.getItems().iterator();
            while (itr.hasNext()) {
                OrderItem oi = itr.next();
                if (!extractIds(others).contains(oi.getId())) {
                    itr.remove();
                }
            }
        }

        private Set<Long> extractIds(Collection<ItemDto> others) {
            return others.stream().map(i -> i.getId()).collect(Collectors.toSet());
        }
    }

}
