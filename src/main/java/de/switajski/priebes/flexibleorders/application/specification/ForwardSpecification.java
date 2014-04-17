package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.Embeddable;

@Embeddable
public class ForwardSpecification {

	private String forwardedToEmail;

	public String getForwardedToEmail() {
		return forwardedToEmail;
	}

	public void setForwardedToEmail(String forwardedToEmail) {
		this.forwardedToEmail = forwardedToEmail;
	} 
}
