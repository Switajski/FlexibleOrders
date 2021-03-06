package de.switajski.priebes.flexibleorders.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.application.AmountCalculatorTest;
import de.switajski.priebes.flexibleorders.application.PurchaseAgreementServiceTest;
import de.switajski.priebes.flexibleorders.domain.ProductTest;
import de.switajski.priebes.flexibleorders.itextpdf.DeliveryNotesPdfFileTest;
import de.switajski.priebes.flexibleorders.itextpdf.InvoicePdfFileTest;
import de.switajski.priebes.flexibleorders.itextpdf.OrderConfirmationPdfFileTest;
import de.switajski.priebes.flexibleorders.itextpdf.OrderPdfFileTest;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryServiceTest;
import de.switajski.priebes.flexibleorders.service.OrderingServiceTest;
import de.switajski.priebes.flexibleorders.service.api.InvoicingServiceTest;
import de.switajski.priebes.flexibleorders.service.api.DocumentNumberGeneratorServiceTest;
import de.switajski.priebes.flexibleorders.service.api.ShippingServiceTest;
import de.switajski.priebes.flexibleorders.service.helper.StatusFilterDispatcherTest;
import de.switajski.priebes.flexibleorders.web.JacksonDeserializationTest;
import de.switajski.priebes.flexibleorders.web.helper.ProcessStepTest;

@RunWith(Suite.class)
@SuiteClasses({
        AmountCalculatorTest.class,
        JacksonDeserializationTest.class,
        OrderingServiceTest.class,
        ProductTest.class,
        PurchaseAgreementServiceTest.class,
        InvoicingServiceTest.class,
        ShippingServiceTest.class,
        ProcessStepTest.class,
        StatusFilterDispatcherTest.class,
        DocumentNumberGeneratorServiceTest.class,
        DeliveryNotesPdfFileTest.class,
        InvoicePdfFileTest.class,
        OrderPdfFileTest.class,
        OrderConfirmationPdfFileTest.class,
        ExpectedDeliveryServiceTest.class,
})
public class UnitTestSuite {

}
