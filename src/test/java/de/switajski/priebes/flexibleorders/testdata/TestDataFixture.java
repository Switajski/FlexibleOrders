package de.switajski.priebes.flexibleorders.testdata;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryMethodBuilder;

public class TestDataFixture {

    public static final Customer YVONNE = new CustomerBuilder().yvonne().build();

    public static final Customer WYOMING = new CustomerBuilder().wyoming().build();

    public static final Customer NAIDA = new CustomerBuilder().naida().build();

    public static final Customer JEROME = new CustomerBuilder().jerome().build();

    public static final Customer EDWARD = new CustomerBuilder().edward().build();


    public static final CatalogProduct SALOME = new CatalogProductBuilder().salome().build();

    public static final CatalogProduct PAUL = new CatalogProductBuilder().paul().build();

    public static final CatalogProduct MILADKA = new CatalogProductBuilder().miladka().build();

    public static final CatalogProduct JUREK = new CatalogProductBuilder().jurek().build();

    public static final CatalogProduct AMY = new CatalogProductBuilder().amy().build();

    
    public static final DeliveryMethod UPS = new DeliveryMethodBuilder().ups().build();

    public static final DeliveryMethod DHL = new DeliveryMethodBuilder().dhl().build();

    
    public static _Order B11 = new _Order("B11", asList(AMY, 10), asList(MILADKA, 15), asList(PAUL, 30));
    
    public static _Order B12 = new _Order("B12", asList(SALOME, 12), asList(JUREK, 5));
    
    public static _Order B13 = new _Order("B13", asList(PAUL, 4), asList(JUREK, 27), asList(SALOME, 8), asList(MILADKA, 6));
    
    public static _Order B15 = new _Order("B15", asList(MILADKA, 5), asList(PAUL, 8), asList(SALOME, 3));
    
    public static _Order B21 = new _Order("B21", asList(SALOME, 17), asList(AMY, 3));
    
    public static _Order B22 = new _Order("B22", asList(JUREK, 13), asList(PAUL, 6));
    

    public static class _Order {

        public String orderNumber;

        public List<Object>[] items;
        
        @SafeVarargs
        public _Order(String orderNumber, List<Object>... items) {
            this.orderNumber = orderNumber;
            this.items = items;
        }

        public List<OrderItem> createOrderItems() {
            List<OrderItem> ois = new ArrayList<OrderItem>();
            for (List<Object> item : items) {
                OrderItem oi = new OrderItem();
                CatalogProduct product = (CatalogProduct) item.get(0);
                oi.setNegotiatedPriceNet(product.getRecommendedPriceNet());
                oi.setProduct(product.toProduct());
                oi.setOrderedQuantity((Integer) item.get(1));
                ois.add(oi);
            }
            return ois;
        }
    }

}
