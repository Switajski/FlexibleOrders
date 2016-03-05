package de.switajski.priebes.flexibleorders.validation;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OrderParameterValidationTest extends ValidationStaticTestConfiguration {

    Set<ConstraintViolation<OrderParameter>> constraintViolations;

    OrderParameter orderParameter = new OrderParameter();

    @Test
    public void shouldRejectOrderParameterWithItemDtoWithoutProduct() {
        ItemDto item = new ItemDto();
        item.setQuantity(2);
        ArrayList<ItemDto> ris = new ArrayList<ItemDto>();
        ris.add(item);
        orderParameter.setItems(ris);

        whenValidating();

        assertTrue(constraintViolations.stream().anyMatch(p -> StringUtils.containsIgnoreCase(p.getPropertyPath().toString(), "product")));
    }

    private void whenValidating() {
        constraintViolations = validator.validate(orderParameter);
    }

}
