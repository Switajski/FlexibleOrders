package de.switajski.priebes.flexibleorders.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.switajski.priebes.flexibleorders.repository.ReportRepository;

@Component
public class UniqueDocumentNumberValidator implements ConstraintValidator<UniqueDocumentNumber, String> {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public void initialize(UniqueDocumentNumber constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (null == reportRepository.findByDocumentNumber(value));
    }

}
