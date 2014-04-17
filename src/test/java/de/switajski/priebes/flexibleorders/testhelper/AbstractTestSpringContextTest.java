package de.switajski.priebes.flexibleorders.testhelper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext-test.xml",
		"classpath*:/applicationContext-jpa-test.xml" })
public abstract class AbstractTestSpringContextTest {

}
