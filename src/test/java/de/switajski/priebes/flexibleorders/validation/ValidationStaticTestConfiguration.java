package de.switajski.priebes.flexibleorders.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;

/**
 * Static, because the validator is initialized statically. FlexibleOrders also
 * uses validators with dependencies, that are injected by spring (e.g.
 * {@link ReportNumberValidator}).
 * 
 * @author switajski
 *
 */
public abstract class ValidationStaticTestConfiguration {

    static Validator validator;

    @Before
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

}
