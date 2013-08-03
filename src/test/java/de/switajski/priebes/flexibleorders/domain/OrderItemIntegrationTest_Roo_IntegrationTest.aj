// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.OrderItemDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.OrderItemIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect OrderItemIntegrationTest_Roo_IntegrationTest {
    
    declare @type: OrderItemIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: OrderItemIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: OrderItemIntegrationTest: @Transactional;
    
    @Autowired
    OrderItemDataOnDemand OrderItemIntegrationTest.dod;
    
    @Autowired
    OrderItemService OrderItemIntegrationTest.orderItemService;
    
    @Autowired
    OrderItemRepository OrderItemIntegrationTest.orderItemRepository;
    
    @Test
    public void OrderItemIntegrationTest.testCountAllOrderItems() {
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", dod.getRandomOrderItem());
        long count = orderItemService.countAllOrderItems();
        Assert.assertTrue("Counter for 'OrderItem' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void OrderItemIntegrationTest.testFindOrderItem() {
        OrderItem obj = dod.getRandomOrderItem();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to provide an identifier", id);
        obj = orderItemService.findOrderItem(id);
        Assert.assertNotNull("Find method for 'OrderItem' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'OrderItem' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void OrderItemIntegrationTest.testFindAllOrderItems() {
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", dod.getRandomOrderItem());
        long count = orderItemService.countAllOrderItems();
        Assert.assertTrue("Too expensive to perform a find all test for 'OrderItem', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<OrderItem> result = orderItemService.findAllOrderItems();
        Assert.assertNotNull("Find all method for 'OrderItem' illegally returned null", result);
        Assert.assertTrue("Find all method for 'OrderItem' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void OrderItemIntegrationTest.testFindOrderItemEntries() {
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", dod.getRandomOrderItem());
        long count = orderItemService.countAllOrderItems();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<OrderItem> result = orderItemService.findOrderItemEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'OrderItem' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'OrderItem' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void OrderItemIntegrationTest.testFlush() {
        OrderItem obj = dod.getRandomOrderItem();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to provide an identifier", id);
        obj = orderItemService.findOrderItem(id);
        Assert.assertNotNull("Find method for 'OrderItem' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyOrderItem(obj);
        Integer currentVersion = obj.getVersion();
        orderItemRepository.flush();
        Assert.assertTrue("Version for 'OrderItem' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void OrderItemIntegrationTest.testUpdateOrderItemUpdate() {
        OrderItem obj = dod.getRandomOrderItem();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to provide an identifier", id);
        obj = orderItemService.findOrderItem(id);
        boolean modified =  dod.modifyOrderItem(obj);
        Integer currentVersion = obj.getVersion();
        OrderItem merged = (OrderItem)orderItemService.updateOrderItem(obj);
        orderItemRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'OrderItem' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void OrderItemIntegrationTest.testSaveOrderItem() {
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", dod.getRandomOrderItem());
        OrderItem obj = dod.getNewTransientOrderItem(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'OrderItem' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'OrderItem' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void OrderItemIntegrationTest.testDeleteOrderItem() {
        OrderItem obj = dod.getRandomOrderItem();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OrderItem' failed to provide an identifier", id);
        obj = orderItemService.findOrderItem(id);
        orderItemService.deleteOrderItem(obj);
        orderItemRepository.flush();
        Assert.assertNull("Failed to remove 'OrderItem' with identifier '" + id + "'", orderItemService.findOrderItem(id));
    }
    
}
