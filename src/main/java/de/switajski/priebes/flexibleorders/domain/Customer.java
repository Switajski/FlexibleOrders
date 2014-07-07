package de.switajski.priebes.flexibleorders.domain;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@JsonAutoDetect
public class Customer extends GenericEntity{

	@NotNull
	@Column(unique = true)
    private Long customerNumber;
	
    @JsonIgnore
    @Embedded
    private Address address;

    @NotNull
    @Column(unique=true)
    private String email;
    
    @Embedded
    private CustomerDetails details;

    @JsonIgnore
    private String password;

    private String phone;
    
    private String firstName;
    
    private String lastName;
    
    /**
     * number of days to add to due date
     */
    @JsonIgnore
    private int paymentGracePeriod;
    
    public Customer(){}
    
    public Customer(
    		Long customerNumber,
    		String email,
    		Address address){
    	setAddress(address);
    	setEmail(email);
    	setCustomerNumber(customerNumber);
    }
    
    public void setAddress(Address address) {
    	this.address = address;
	}

	public Address getAddress(){
    	return address;
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

	public Long getCustomerNumber() {
		return this.customerNumber;
	}

	public void setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
	}

	public CustomerDetails getDetails() {
		return details;
	}

	public void setDetails(CustomerDetails details) {
		this.details = details;
	}
}
