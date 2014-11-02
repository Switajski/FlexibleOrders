package de.switajski.priebes.flexibleorders.domain.embeddable;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.reference.Currency;

@Embeddable
public class Amount {

	@Transient
	private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat(",##0.00");
	
	@Transient
	public final static Amount ZERO_EURO = new Amount(BigDecimal.ZERO, Currency.EUR);
	
	@NotNull
	private BigDecimal value = BigDecimal.ZERO;
	
	@NotNull
	private Currency currency = Currency.EUR;
	
	public Amount() {}
	
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

	/**
	 * 
	 * @param negotiatedPriceNet not null and has currency
	 * @return
	 */
	public Amount add(Amount negotiatedPriceNet) {
		if (negotiatedPriceNet == null)
			return this;
		if (this.currency == null)
			currency = negotiatedPriceNet.getCurrency();
		
		if (sameCurrency(negotiatedPriceNet))
			return new Amount(negotiatedPriceNet.getValue().add(this.getValue()), this.currency);
		else throw new IllegalArgumentException("tried to add amounts with different currencies");
	}

	public boolean sameCurrency(Amount negotiatedPriceNet) {
		return negotiatedPriceNet.getCurrency().equals(this.currency);
	}
	
	public String toString(){
		String currencyChar = "";
		switch (getCurrency()) {
			case EUR: currencyChar+= " \u20ac"; break;
			case PLN: currencyChar+= " zl";
		}
		
		String s = "";
		if (value != null)			
			s += DECIMAL_FORMAT.format(value);
		else 
			s += " - ";
		if (currency != null)
			s += currencyChar;
		else 
			s += " undefined";
		
		return s;
	}

	public Amount multiply(Integer multiplicand) {
		return new Amount(value.multiply(new BigDecimal(multiplicand)), currency);
	}
	
	public Amount multiply(double multiplicand){
		return new Amount(value.multiply(new BigDecimal(multiplicand)), currency);
	}
	
	public Amount multiply(Amount multiplicand) {
		if (this.currency != multiplicand.currency)
			throw new IllegalArgumentException("Different currencies");
		return new Amount(value.multiply(multiplicand.getValue()), currency);
	}

	public Amount devide(double divisor) {
		if (divisor == 0d) throw new IllegalArgumentException("Cannot devide by zero");
		return new Amount(this.value.divide(new BigDecimal(divisor)), this.currency);
	}
	
	@JsonIgnore
	public boolean isGreaterZero(){
		if (this.getValue() == null)
			return false;
		if (this.getValue().compareTo(BigDecimal.ZERO) > 0)
			return true;
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Amount))
			return false;
		else {
			Amount a = (Amount) obj;
			if (a.currency != this.currency)
				return false;
			if (a.getValue().compareTo(this.getValue()) == 0)
				return true;
			else 
				return false;
		}
	}
	
}
