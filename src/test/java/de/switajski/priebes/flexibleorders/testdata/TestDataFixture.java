package de.switajski.priebes.flexibleorders.testdata;

import static de.switajski.priebes.flexibleorders.testdata.ConfirmParameterShorthand.confirm;
import static de.switajski.priebes.flexibleorders.testdata.ItemDtoShorthand.item;
import static de.switajski.priebes.flexibleorders.testdata.OrderParameterShorthand.order;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryMethodBuilder;

public class TestDataFixture {
    
    private static final String B11_STR = "B11", B12_STR = "B12", B13_STR = "B13", B22_STR = "B22", B21_STR = "B21", B15_STR = "B15";

    private static final String AB15_STR = "AB15", AB11_STR = "AB11", AB13_STR = "AB13";


    public static final LocalDate NOW = new DateTime().toLocalDate();
    
    public static final DateTime DT = new DateTime();
    
    
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

    
    public static OrderParameter B11 = order(B11_STR, YVONNE, NOW, 
            item(AMY,       10), 
            item(MILADKA,   15), 
            item(PAUL,      30));
    
    public static OrderParameter B12 = order(B12_STR, YVONNE, NOW, 
            item(SALOME,    12), 
            item(JUREK,     5));
    
    public static OrderParameter B13    = order(B13_STR, YVONNE, NOW,                 
            item(PAUL, 4), 
            item(JUREK, 27), 
            item(SALOME, 8), 
            item(MILADKA, 6));
    
    public static OrderParameter B15    = order(B15_STR, YVONNE, delay(2),            
            item(MILADKA, 5), 
            item(PAUL, 8), 
            item(SALOME, 3));
    
    public static OrderParameter B21 = order(B21_STR, NAIDA, NOW, 
            item(SALOME, 17), 
            item(AMY, 3));
    
    public static OrderParameter B22 = order(B22_STR, NAIDA, NOW, 
            item(JUREK, 13), 
            item(PAUL, 6));
    

    public static ConfirmParameter AB11 = confirm(B11_STR, AB11_STR, YVONNE, delay(10), 
            item(AMY,       10, B11_STR), 
            item(MILADKA,   15, B11_STR), 
            item(PAUL,      30, B11_STR),   
            item(SALOME,    12, B12_STR), 
            item(JUREK,     5 , B12_STR));
    
    public static ConfirmParameter AB13 = confirm(B13_STR, AB13_STR, YVONNE, delay(2),  
            item(PAUL, 4, B13_STR), 
            item(JUREK, 27, B13_STR), 
            item(SALOME, 8, B13_STR), 
            item(MILADKA, 6, B13_STR));
    
    public static ConfirmParameter AB15 = confirm(B15_STR, AB15_STR, YVONNE, delay(10), 
            item(MILADKA, 5, B15_STR), 
            item(PAUL, 8, B15_STR), 
            item(SALOME, 3, B15_STR));

    public static LocalDate delay(int days){
        return DT.plusDays(days).toLocalDate();
    }

}
