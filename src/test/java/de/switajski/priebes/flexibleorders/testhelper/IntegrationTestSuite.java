package de.switajski.priebes.flexibleorders.testhelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.domain.CategoryIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.CustomerIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ProductIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({
		CustomerIntegrationTest.class,
		ProductIntegrationTest.class,
		CategoryIntegrationTest.class
})
public class IntegrationTestSuite {

}
