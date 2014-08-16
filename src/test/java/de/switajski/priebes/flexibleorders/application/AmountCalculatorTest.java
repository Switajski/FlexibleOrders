package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.reference.Currency;

public class AmountCalculatorTest {

	@Test(expected = IllegalArgumentException.class)
	public void sum_shouldThrowExceptionWhenAddingDifferentCurrencies() {
		// GIVEN
		List<Amount> amounts = Arrays.asList(
				new Amount(BigDecimal.TEN, Currency.EUR),
				new Amount(BigDecimal.TEN, Currency.PLN));

		// WHEN
		AmountCalculator.sum(amounts);
	}

	@Test
	public void sum_shouldReturnSum() {
		// GIVEN
		List<Amount> amounts = Arrays.asList(
				new Amount(BigDecimal.TEN, Currency.EUR),
				new Amount(BigDecimal.TEN, Currency.EUR));

		// WHEN
		Amount sum = AmountCalculator.sum(amounts);

		// THEN
		assertThat(sum, equalTo(new Amount(new BigDecimal(20), Currency.EUR)));
	}
}
