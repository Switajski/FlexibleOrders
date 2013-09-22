// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.CustomerDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OrderItemDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ProductDataOnDemand;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect OrderItemDataOnDemand_Roo_DataOnDemand {
    
    declare @type: OrderItemDataOnDemand: @Component;
    
    private Random OrderItemDataOnDemand.rnd = new SecureRandom();
    
    private List<OrderItem> OrderItemDataOnDemand.data;
    
    @Autowired
    CustomerDataOnDemand OrderItemDataOnDemand.customerDataOnDemand;
    
    @Autowired
    ProductDataOnDemand OrderItemDataOnDemand.productDataOnDemand;
    
    @Autowired
    OrderItemService OrderItemDataOnDemand.orderItemService;
    
    @Autowired
    OrderItemRepository OrderItemDataOnDemand.orderItemRepository;
    
    public OrderItem OrderItemDataOnDemand.getNewTransientOrderItem(int index) {
        OrderItem obj = new OrderItem();
        setAccountNumber(obj, index);
        setCreated(obj, index);
        setCustomer(obj, index);
        setExpectedDelivery(obj, index);
        setInvoiceNumber(obj, index);
        setOrderConfirmationNumber(obj, index);
        setOrderItemNumber(obj, index);
        setOrderNumber(obj, index);
        setPriceNet(obj, index);
        setProduct(obj, index);
        setProductName(obj, index);
        setProductNumber(obj, index);
        setQuantity(obj, index);
        setQuantityLeft(obj, index);
        return obj;
    }
    
    public void OrderItemDataOnDemand.setAccountNumber(OrderItem obj, int index) {
        Long accountNumber = new Integer(index).longValue();
        obj.setAccountNumber(accountNumber);
    }
    
    public void OrderItemDataOnDemand.setCreated(OrderItem obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }
    
    public void OrderItemDataOnDemand.setCustomer(OrderItem obj, int index) {
        Customer customer = customerDataOnDemand.getRandomCustomer();
        obj.setCustomer(customer);
    }
    
    public void OrderItemDataOnDemand.setExpectedDelivery(OrderItem obj, int index) {
        Date expectedDelivery = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setExpectedDelivery(expectedDelivery);
    }
    
    public void OrderItemDataOnDemand.setInvoiceNumber(OrderItem obj, int index) {
        Long invoiceNumber = new Integer(index).longValue();
        obj.setInvoiceNumber(invoiceNumber);
    }
    
    public void OrderItemDataOnDemand.setOrderConfirmationNumber(OrderItem obj, int index) {
        Long orderConfirmationNumber = new Integer(index).longValue();
        obj.setOrderConfirmationNumber(orderConfirmationNumber);
    }
    
    public void OrderItemDataOnDemand.setOrderItemNumber(OrderItem obj, int index) {
        int orderItemNumber = index;
        obj.setOrderItemNumber(orderItemNumber);
    }
    
    public void OrderItemDataOnDemand.setOrderNumber(OrderItem obj, int index) {
        Long orderNumber = new Integer(index).longValue();
        obj.setOrderNumber(orderNumber);
    }
    
    public void OrderItemDataOnDemand.setPriceNet(OrderItem obj, int index) {
        BigDecimal priceNet = BigDecimal.valueOf(index);
        obj.setPriceNet(priceNet);
    }
    
    public void OrderItemDataOnDemand.setProduct(OrderItem obj, int index) {
        Product product = productDataOnDemand.getSpecificProduct(index);
        obj.setProduct(product);
    }
    
    public void OrderItemDataOnDemand.setProductName(OrderItem obj, int index) {
        String productName = "productName_" + index;
        obj.setProductName(productName);
    }
    
    public void OrderItemDataOnDemand.setProductNumber(OrderItem obj, int index) {
        Long productNumber = new Integer(index).longValue();
        obj.setProductNumber(productNumber);
    }
    
    public void OrderItemDataOnDemand.setQuantity(OrderItem obj, int index) {
        int quantity = index;
        obj.setQuantity(quantity);
    }
    
    public void OrderItemDataOnDemand.setQuantityLeft(OrderItem obj, int index) {
        Integer quantityLeft = new Integer(index);
        obj.setQuantityLeft(quantityLeft);
    }
    
    public OrderItem OrderItemDataOnDemand.getSpecificOrderItem(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        OrderItem obj = data.get(index);
        Long id = obj.getId();
        return orderItemService.findOrderItem(id);
    }
    
    public OrderItem OrderItemDataOnDemand.getRandomOrderItem() {
        init();
        OrderItem obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return orderItemService.findOrderItem(id);
    }
    
    public boolean OrderItemDataOnDemand.modifyOrderItem(OrderItem obj) {
        return false;
    }
    
    public void OrderItemDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = orderItemService.findOrderItemEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'OrderItem' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<OrderItem>();
        for (int i = 0; i < 10; i++) {
            OrderItem obj = getNewTransientOrderItem(i);
            try {
                orderItemService.saveOrderItem(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            orderItemRepository.flush();
            data.add(obj);
        }
    }
    
}
