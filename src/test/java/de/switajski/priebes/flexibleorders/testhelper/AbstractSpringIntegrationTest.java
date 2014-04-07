package de.switajski.priebes.flexibleorders.testhelper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext-test.xml", 
		"classpath*:/applicationContext-jpa.xml"})
public abstract class AbstractSpringIntegrationTest {

}
