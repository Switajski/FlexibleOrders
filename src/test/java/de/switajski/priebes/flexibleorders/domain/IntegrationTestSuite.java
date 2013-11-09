package de.switajski.priebes.flexibleorders.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArchiveItemIntegrationTest.class,
	CategoryIntegrationTest.class,
	InvoiceItemIntegrationTest.class,
	ProductIntegrationTest.class,
	OrderItemIntegrationTest.class})
public class IntegrationTestSuite {

}
