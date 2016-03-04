package de.switajski.priebes.flexibleorders.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.switajski.priebes.flexibleorders.repository.OrderRepository;

@Component
public class UniqueOrderNumberValidator implements ConstraintValidator<UniqueOrderNumber, String> {

    @Autowired
    private OrderRepository reportRepository;

    @Override
    public void initialize(UniqueOrderNumber constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return (null == reportRepository.findByOrderNumber(value));
    }

}
