package de.switajski.priebes.flexibleorders.validation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testconfiguration.SpringMvcWithTestDataTestConfiguration;

public class DeliverParameterValidationTest extends SpringMvcWithTestDataTestConfiguration {

    @Autowired
    Validator validator;

    Set<ConstraintViolation<DeliverParameter>> dpConstraintViolations;
    DeliverParameter deliverParameter = new DeliverParameter();

    @Test
    public void shouldNotAcceptDuplicateDocumentNumber() {
        deliverParameter.setDeliveryNotesNumber("L11");

        whenValidatingDeliverParameter();

        assertThat(
                dpConstraintViolations.stream()
                        .anyMatch(cv -> StringUtils.contains(cv.getMessage(), "Dokumentennr")),
                is(true));
    }

    private void whenValidatingDeliverParameter() {
        dpConstraintViolations = validator.validate(deliverParameter);
    }

}
