package de.switajski.priebes.flexibleorders.testhelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.domain.CategoryIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.CustomerIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ProductIntegrationTest;
import de.switajski.priebes.flexibleorders.report.itextpdf.DeliveryNotesPdfFileTest;
import de.switajski.priebes.flexibleorders.report.itextpdf.ConfirmationReportPdfFileTest;
import de.switajski.priebes.flexibleorders.report.itextpdf.OrderPdfFileTest;
import de.switajski.priebes.flexibleorders.service.OrderServiceIntegrationTest;
import de.switajski.priebes.flexibleorders.web.JacksonDeserializationTest;

@RunWith(Suite.class)
@SuiteClasses({
	CategoryIntegrationTest.class,
	CustomerIntegrationTest.class,
	DeliveryNotesPdfFileTest.class,
	JacksonDeserializationTest.class,
	OrderPdfFileTest.class,
	OrderServiceIntegrationTest.class,
	ConfirmationReportPdfFileTest.class,
	ProductIntegrationTest.class})
public class AllTestSuite {

}
