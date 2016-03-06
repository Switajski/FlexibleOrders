package de.switajski.priebes.flexibleorders.validation;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.internal.util.ReflectionHelper;
import org.junit.Before;

/**
 * Static, because the validator is initialized statically. FlexibleOrders also
 * uses validators with '@Component' annotation, that are injected by spring
 * (e.g. {@link ReportNumberValidator}). In order to mock those dependencies use
 * this test configuration and inject mocks by
 * {@link #inject(Class, ConstraintValidator)}
 * 
 * @author switajski
 *
 */
public abstract class ValidationStaticTestConfiguration {

    Validator validator;

    Map<Class<?>, ConstraintValidator<?, ?>> constraintValidators = new HashMap<>();

    @Before
    public void setup() {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        configuration.constraintValidatorFactory(new ConstraintValidatorDelegate(constraintValidators));

        validator = configuration.buildValidatorFactory().getValidator();
    }

    protected void inject(Class<?> clazz, ConstraintValidator<?, ?> cValidator) {
        constraintValidators.put(clazz, cValidator);
        setup();
    }

    private class ConstraintValidatorDelegate implements ConstraintValidatorFactory {

        private Map<Class<?>, ConstraintValidator<?, ?>> validators;

        public ConstraintValidatorDelegate(Map<Class<?>, ConstraintValidator<?, ?>> constraintValidators) {
            validators = constraintValidators;
        }

        public final <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            if (validators.containsKey(key)) return (T) validators.get(key);
            return ReflectionHelper.newInstance(key, "ConstraintValidator");
        }

    }

}
