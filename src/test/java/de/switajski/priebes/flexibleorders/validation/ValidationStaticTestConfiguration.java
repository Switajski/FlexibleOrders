package de.switajski.priebes.flexibleorders.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;

import net.bytebuddy.pool.TypePool.Resolution.Illegal;
import org.apache.commons.beanutils.ConstructorUtils;
import org.hibernate.validator.internal.util.ReflectionHelper;

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

    Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();

    Map<Class<?>, ConstraintValidator<?, ?>> constraintValidators = new HashMap<>();

    private void setup() {
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
            try {
                return (T) ConstructorUtils.invokeConstructor(key, "ConstraintValidator");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            throw new IllegalStateException("Constraint invocations are broken in test");
        }
    
        @Override
        public void releaseInstance(ConstraintValidator<?, ?> instance) {
        
        }
    
    }

}
