package de.switajski.priebes.flexibleorders.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

@Component
public class CustomerNumberExistsInRepositoryValidator implements ConstraintValidator<CustomerNumberExistsInRepository, Long> {

    @Autowired
    CustomerRepository customerRepo;

    @Override
    public void initialize(CustomerNumberExistsInRepository constraintAnnotation) {}

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return (null != customerRepo.findByCustomerNumber(value));
    }

}
