package de.switajski.priebes.flexibleorders.testdata;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.api.AgreeingService;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.InvoicingService;
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.conversion.OrderItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;

@Component
public class StandardTestDataRule extends ExternalResource {

    @PersistenceUnit
    EntityManagerFactory emf;

    @Autowired
    CatalogProductRepository cpRepo;

    @Autowired
    CustomerRepository cRepo;

    @Autowired
    TransitionsService orderingService;

    @Autowired
    OrderItemToItemDtoConversionService oi2ItemDtoConversionService;

    @Autowired
    ConfirmingService confirmingService;

    @Autowired
    ReportItemToItemDtoConversionService riToItemConversionService;

    @Autowired
    ShippingService shippingService;

    @Autowired
    InvoicingService invoicingService;

    @Autowired
    CatalogDeliveryMethodRepository deliveryMethodRepo;

    @Autowired
    AgreeingService agreeingService;

    @Autowired
    JpaTransactionManager tm;

    @Override
    protected void before() {
        TransactionTemplate template = new TransactionTemplate(tm);
        template.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus status) {

                TestDataCreator tdc = new TestDataCreator(
                        cpRepo,
                        cRepo,
                        orderingService,
                        confirmingService,
                        oi2ItemDtoConversionService,
                        riToItemConversionService,
                        shippingService,
                        invoicingService,
                        deliveryMethodRepo,
                        agreeingService);

                if (!tdc.isTestDataPersisted()) {
                    try {
                        tdc.createTestData();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

        });

    }
}
