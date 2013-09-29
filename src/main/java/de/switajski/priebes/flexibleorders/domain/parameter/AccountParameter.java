package de.switajski.priebes.flexibleorders.domain.parameter;

/**
 * encapsulates and validates parameters for the "complete" transition 
 * {@link ItemTransition#complete}
 * @author Marek Switajski
 *
 */
public class AccountParameter {

	public long accountNumber;
	
	/**
	 * 
	 * @param accountNumber if a link to the accounting
	 */
	public AccountParameter(
			long accountNumber) {
		if (accountNumber < 1l)
			throw new IllegalArgumentException("account number must be more than 1");
		this.accountNumber = accountNumber;
	}

	public long getAccountNumber() {
		return accountNumber;
	}
}
