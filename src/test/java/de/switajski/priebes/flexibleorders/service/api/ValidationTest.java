package de.switajski.priebes.flexibleorders.service.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.function.Predicate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class ValidationTest {

    static Validator validator;
    Set<ConstraintViolation<InvoicingParameter>> constraintViolations;

    InvoicingParameter invoicingParameter;

    @Before
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void shouldNotAcceptParameterWithEmptyItems() {
        whenValidating();

        assertConstraintsMatches(cv -> StringUtils.contains(cv.getMessage(), "empty"));
    }

    @Test
    public void shouldNotAcceptParameterWithNoDocumentNumber() {
        whenValidating();

        assertConstraintsMatches(cv -> StringUtils.contains(cv.getMessage(), "null"));
    }

    private void assertConstraintsMatches(Predicate<ConstraintViolation<InvoicingParameter>> predicate) {
        assertThat(constraintViolations.stream().anyMatch(predicate), is(true));
    }

    public void whenValidating() {
        constraintViolations = validator.validate(invoicingParameter);
    }

}
