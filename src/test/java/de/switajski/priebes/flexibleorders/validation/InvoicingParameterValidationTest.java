package de.switajski.priebes.flexibleorders.validation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.function.Predicate;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;

public class InvoicingParameterValidationTest extends ValidationStaticTestConfiguration {

    Set<ConstraintViolation<InvoicingParameter>> constraintViolations;

    InvoicingParameter invoicingParameter = new InvoicingParameter();

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
    public void shouldRejectInvoicingIfDocumentNumberAlreadyExists() throws Exception {
        invoicingParameter.setInvoiceNumber("R11");
        when(reportRepository.findByDocumentNumber(invoicingParameter.getInvoiceNumber()))
                .thenReturn(new Invoice());

        whenValidating();

        assertAnyConstraintContainsInMessage("Dokumentennr");
    }

    @Test
    public void shouldNotAcceptParameterWithEmptyItems() {
        whenValidating();

        assertAnyConstraintContainsInMessage("empty");
    }

    @Test
    public void shouldNotAcceptParameterWithNoDocumentNumber() {
        whenValidating();

        assertAnyConstraintContainsInMessage("null");
    }

    protected void assertAnyConstraintContainsInMessage(String pattern) {
        assertAnyConstraintsMatch(cv -> StringUtils.contains(cv.getMessage(), pattern));
    }

    protected void assertAnyConstraintsMatch(Predicate<ConstraintViolation<InvoicingParameter>> predicate) {
        assertThat(constraintViolations.stream().anyMatch(predicate), is(true));
    }

    private void whenValidating() {
        constraintViolations = validator.validate(invoicingParameter);
    }

}
