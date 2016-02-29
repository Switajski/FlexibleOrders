package de.switajski.priebes.flexibleorders.validation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.function.Predicate;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;

public class InvoicingParameterValidationTest extends ValidationStaticTestConfiguration {

    Set<ConstraintViolation<InvoicingParameter>> constraintViolations;

    InvoicingParameter invoicingParameter = new InvoicingParameter();

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

    protected void assertConstraintsMatches(Predicate<ConstraintViolation<InvoicingParameter>> predicate) {
        assertThat(constraintViolations.stream().anyMatch(predicate), is(true));
    }

    private void whenValidating() {
        constraintViolations = validator.validate(invoicingParameter);
    }

}
