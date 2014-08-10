package de.switajski.priebes.flexibleorders.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.application.AmountCalculatorTest;
import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculatorTest;
import de.switajski.priebes.flexibleorders.domain.ProductTest;
import de.switajski.priebes.flexibleorders.service.OrderServiceTest;
import de.switajski.priebes.flexibleorders.service.QuantityCalculatorTest;
import de.switajski.priebes.flexibleorders.service.ReportingServiceTest;
import de.switajski.priebes.flexibleorders.web.JacksonDeserializationTest;

@RunWith(Suite.class)
@SuiteClasses({
		AmountCalculatorTest.class,
		JacksonDeserializationTest.class,
		OrderServiceTest.class,
		ProductTest.class,
		ReportingServiceTest.class,
		ShippingCostsCalculatorTest.class,
		QuantityCalculatorTest.class
})
public class UnitTestSuite {

}
