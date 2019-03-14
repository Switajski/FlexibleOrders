package de.switajski.priebes.flexibleorders.testconfiguration;

import de.switajski.priebes.flexibleorders.SpringBootApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootApplication.class)
public abstract class AbstractSpringContextTestConfiguration {

}
