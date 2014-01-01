package de.switajski.priebes.flexibleorders.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class Amount {

	private BigDecimal value;
	
	private Currency currency = Currency.EUR;
	
	public Amount(BigDecimal value, Currency currency) {
		this.value = value;
		this.currency = currency;
	}
	
	public Amount(BigDecimal value){
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
