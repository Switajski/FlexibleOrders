package de.switajski.priebes.flexibleorders.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.domain.CategoryIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.CustomerIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ProductIntegrationTest;
import de.switajski.priebes.flexibleorders.itextpdf.DeliveryNotesPdfFileTest;
import de.switajski.priebes.flexibleorders.itextpdf.InvoicePdfFileTest;
import de.switajski.priebes.flexibleorders.itextpdf.OrderConfirmationPdfFileTest;
import de.switajski.priebes.flexibleorders.itextpdf.OrderPdfFileTest;
import de.switajski.priebes.flexibleorders.service.SpecificationIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({
		CategoryIntegrationTest.class,
		OrderConfirmationPdfFileTest.class,
		CustomerIntegrationTest.class,
		DeliveryNotesPdfFileTest.class,
		InvoicePdfFileTest.class,
		OrderPdfFileTest.class,
		ProductIntegrationTest.class,
		SpecificationIntegrationTest.class
})
public class IntegrationTestSuite {

}
