package de.switajski.priebes.flexibleorders.domain.embeddable;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.DeliveryType;

/**
 * TODO: Split into Carrier and Method. Concept to be analyzed: Delivery abroad, zones, etc...
 * @author Marek Switajski
 *
 */
@Embeddable
public class DeliveryMethod{
	
    private Long externalId;
    
    @NotNull
	@Enumerated
	private DeliveryType deliveryType;
	
	private String name, phone;
	
	private Address address;

	public DeliveryMethod() {
		this.deliveryType = DeliveryType.SPEDITION;
	}
	
	public Long getId() {
        return externalId;
    }

    public void setId(Long id) {
        this.externalId = id;
    }
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

}
