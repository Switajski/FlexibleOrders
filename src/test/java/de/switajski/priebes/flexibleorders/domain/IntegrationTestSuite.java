package de.switajski.priebes.flexibleorders.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ItemIntegrationTest.class,
	CustomerIntegrationTest.class,
	ProductIntegrationTest.class,
	CategoryIntegrationTest.class
	})
public class IntegrationTestSuite {

}
