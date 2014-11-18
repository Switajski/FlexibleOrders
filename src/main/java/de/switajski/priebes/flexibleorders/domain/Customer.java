package de.switajski.priebes.flexibleorders.domain;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;

@Entity
@JsonAutoDetect
public class Customer extends GenericEntity{

	@NotNull
	@Column(unique = true)
    private Long customerNumber;
	
    @JsonIgnore
    @Embedded
    private Address invoiceAddress;
    
    @JsonIgnore
    @AttributeOverrides({
        @AttributeOverride(name="name1",column=@Column(name="shippingName1")),
        @AttributeOverride(name="name2",column=@Column(name="shippingName2")),
        @AttributeOverride(name="street",column=@Column(name="shippingStreet")),
        @AttributeOverride(name="postalCode",column=@Column(name="shippingPostalCode")),
        @AttributeOverride(name="city",column=@Column(name="shippingCity")),
        @AttributeOverride(name="country",column=@Column(name="shippingCountry"))
      })
    @Embedded
    private Address shippingAddress;

    @NotNull
    @Column(unique=true)
    private String email;
    
    @Embedded
    private CustomerDetails details;
    
    private String companyName, firstName, lastName;
    
    /**
     * number of days to add to due date
     */
    @JsonIgnore
    private int paymentGracePeriod;

    private String phone, fax;

    @Column(columnDefinition="TEXT")
    private String notes;
    
    public Customer(){}
    
    public Customer(
    		Long customerNumber,
    		String email,
    		Address invoiceAddress){
    	setInvoiceAddress(invoiceAddress);
    	setEmail(email);
    	setCustomerNumber(customerNumber);
    }
    
    public void setInvoiceAddress(Address address) {
    	this.invoiceAddress = address;
	}

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
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

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address deliveryAddress) {
		this.shippingAddress = deliveryAddress;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public String getFax() {
        return fax;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
