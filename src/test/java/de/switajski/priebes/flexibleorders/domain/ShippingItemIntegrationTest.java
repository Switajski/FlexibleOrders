package de.switajski.priebes.flexibleorders.domain;

import static org.junit.Assert.assertNotEquals;

import java.util.Iterator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

@RooIntegrationTest(entity = ShippingItem.class)
public class ShippingItemIntegrationTest {

	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	ShippingItemRepository shippingItemRepository;
	
    @Test
    public void shouldHaveDifferentHashCodes() {
    	OrderItemDataOnDemand odod = new OrderItemDataOnDemand();
    	int oiHash = odod.getNewTransientOrderItem(7).hashCode();
    	
    	ShippingItem shippingItem = dod.getNewTransientShippingItem(7);
    	int siHash = shippingItem.hashCode();
    	
    	assertNotEquals(oiHash, siHash);
    }
    
    /** 
     * <b>This test is monitoring a core compentence of "FlexibleOrders".<b></br>
     * 
     * The IDs of OrderItems, ShippingItems, IvoiceItems and ArchiveItems
     * must not have same IDs (e.g. Composite Key of orderItemNo. and orderNo.)
     * The reports (invoices, orders, ...) are generated dynamically by finding 
     * items by orderNumber. This report generation enables piecemeal delivering
     * and flexibility in changing reports.
     * 
     */
    @Test
    public void equivalentItemsShouldHaveDifferentIdentifiers() {
    	int id = 72;
    	ShippingItem si = dod.getNewTransientShippingItem(id);
        try {
            shippingItemRepository.save(si);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        shippingItemRepository.flush();
        Assert.assertNotNull("Expected 'ShippingItem' identifier to no longer be null", si.getId());
    	
        OrderItemDataOnDemand odod = new OrderItemDataOnDemand();
        OrderItem oi = odod.getNewTransientOrderItem(id);
        orderItemRepository.save(oi);
    	
    	
    	assertNotEquals(oi.getId(), si.getId());
    }

}
