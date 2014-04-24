package de.switajski.priebes.flexibleorders.testhelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		IntegrationTestSuite.class,
		UnitTestSuite.class})
public class AllTestSuite {

}
