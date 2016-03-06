package de.switajski.priebes.flexibleorders.validation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;

public class DeliverParameterValidationTest extends ValidationStaticTestConfiguration {

    Set<ConstraintViolation<DeliverParameter>> dpConstraintViolations;
    DeliverParameter deliverParameter = new DeliverParameter();

    @Mock
    ReportRepository reportRepository;
    @InjectMocks
    ReportNumberValidator reportNumberValidator = new ReportNumberValidator();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        inject(ReportNumberValidator.class, reportNumberValidator);
    }

    @Test
    public void shouldNotAcceptDuplicateDocumentNumber() {
        deliverParameter.setDeliveryNotesNumber("L11");
        when(reportRepository.findByDocumentNumber(deliverParameter.getDeliveryNotesNumber()))
                .thenReturn(new DeliveryNotes());

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
