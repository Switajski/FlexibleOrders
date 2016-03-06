package de.switajski.priebes.flexibleorders.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

@Component
public class ReportNumberValidator implements ConstraintValidator<ReportNumber, String> {

    @Autowired
    private ReportRepository reportRepository;

    private boolean shouldExist;

    @Override
    public void initialize(ReportNumber constraintAnnotation) {
        shouldExist = constraintAnnotation.shouldExist();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Report report = reportRepository.findByDocumentNumber(value);
        if (shouldExist) return (report != null);
        else return (report == null);
    }

    public void setReportRepository(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

}
