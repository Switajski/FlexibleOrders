package de.switajski.priebes.flexibleorders.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.application.AmountCalculatorTest;
import de.switajski.priebes.flexibleorders.web.JacksonDeserializationTest;

@RunWith(Suite.class)
@SuiteClasses({
		AmountCalculatorTest.class,
		JacksonDeserializationTest.class
})
public class UnitTestSuite {

}
