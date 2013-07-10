// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OrderItemServiceImpl;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect OrderItemServiceImpl_Roo_Service {
    
    declare @type: OrderItemServiceImpl: @Service;
    
    declare @type: OrderItemServiceImpl: @Transactional;
    
    @Autowired
    OrderItemRepository OrderItemServiceImpl.orderItemRepository;
    
    public long OrderItemServiceImpl.countAllOrderItems() {
        return orderItemRepository.count();
    }
    
    public void OrderItemServiceImpl.deleteOrderItem(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }
    
    public OrderItem OrderItemServiceImpl.findOrderItem(Long id) {
        return orderItemRepository.findOne(id);
    }
    
    public List<OrderItem> OrderItemServiceImpl.findAllOrderItems() {
        return orderItemRepository.findAll();
    }
    
    public List<OrderItem> OrderItemServiceImpl.findOrderItemEntries(int firstResult, int maxResults) {
        return orderItemRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void OrderItemServiceImpl.saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
    
    public OrderItem OrderItemServiceImpl.updateOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
    
}