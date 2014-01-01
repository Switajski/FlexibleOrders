package de.switajski.priebes.flexibleorders.domain;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.specification.Address;

@Entity
@JsonAutoDetect
public class Customer extends GenericEntity{

    /**
     */
    @Column(unique = true)
    private String shortName;
    
    @JsonIgnore
    @Embedded
    private Address address;

    /**
     */
    @NotNull
    @Column(unique = true)
    private String email;

    /**
     */
    @JsonIgnore
    private String password;

    /**
     */
    private String phone;
    
    private String firstName;
    
    private String lastName;
    
    /**
     * number of days to add to due date
     */
    private int paymentGracePeriod;
    
    public Customer(){}
    
    public Customer(
    		String shortName,
    		String email,
    		Address address){
    	setAddress(address);
    	setEmail(email);
    	setShortName(shortName);
    }
    
    public void setAddress(Address address) {
    	this.address = address;
	}

	public Address getAddress(){
    	return address;
    }
    
	public String getShortName() {
        return this.shortName;
    }

	public void setShortName(String shortName) {
        this.shortName = shortName;
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public String getPhone() {
        return this.phone;
    }

	public void setPhone(String phone) {
        this.phone = phone;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public int getPaymentGracePeriod() {
		return paymentGracePeriod;
	}

	public void setPaymentGracePeriod(int paymentGracePeriod) {
		this.paymentGracePeriod = paymentGracePeriod;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
