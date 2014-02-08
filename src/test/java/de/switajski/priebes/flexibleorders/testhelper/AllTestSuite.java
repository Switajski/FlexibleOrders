package de.switajski.priebes.flexibleorders.testhelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.domain.CategoryIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.CustomerIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ItemIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ProductIntegrationTest;
import de.switajski.priebes.flexibleorders.report.itextpdf.InvoicePdfFileTest;
import de.switajski.priebes.flexibleorders.report.itextpdf.OrderConfirmationPdfFileTest;
import de.switajski.priebes.flexibleorders.report.itextpdf.OrderPdfFileTest;
import de.switajski.priebes.flexibleorders.service.OrderServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	CategoryIntegrationTest.class,
	CustomerIntegrationTest.class,
	InvoicePdfFileTest.class,
	ItemIntegrationTest.class, 
	OrderPdfFileTest.class,
	OrderServiceTest.class,
	OrderConfirmationPdfFileTest.class,
	ProductIntegrationTest.class})
public class AllTestSuite {

}
