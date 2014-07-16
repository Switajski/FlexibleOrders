package de.switajski.priebes.flexibleorders.web.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * TODO this object is disposable by good serialization
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class CustomerDto {

	private Long id, customerNumber;
	
    private String email, password, phone, firstName, lastName;

    //*********************
    // invoice address attributes
    //*********************
    private String name1, name2, street, city, country;
    
    private int postalCode;
    
    //*********************
    // delivery address attributes
    //*********************
    private String dname1, dname2, dstreet, dcity, dcountry;

    private int dpostalCode;

    //*********************
    // Customer details attributes
    //*********************
    private String vendorNumber, vatIdNo, paymentConditions;
    
    private String saleRepresentative, mark, contact1, contact2, contact3;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVatIdNo() {
		return vatIdNo;
	}

	public void setVatIdNo(String vatIdNo) {
		this.vatIdNo = vatIdNo;
	}

	public String getPaymentConditions() {
		return this.paymentConditions;
	}
	
	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}

	public String getDname1() {
		return dname1;
	}

	public void setDname1(String dname1) {
		this.dname1 = dname1;
	}

	public String getDname2() {
		return dname2;
	}

	public void setDname2(String dname2) {
		this.dname2 = dname2;
	}

	public String getDstreet() {
		return dstreet;
	}

	public void setDstreet(String dstreet) {
		this.dstreet = dstreet;
	}

	public String getDcity() {
		return dcity;
	}

	public void setDcity(String dcity) {
		this.dcity = dcity;
	}

	public String getDcountry() {
		return dcountry;
	}

	public void setDcountry(String dcountry) {
		this.dcountry = dcountry;
	}

	public int getDpostalCode() {
		return dpostalCode;
	}

	public void setDpostalCode(int dpostalCode) {
		this.dpostalCode = dpostalCode;
	}

	public String getSaleRepresentative() {
		return saleRepresentative;
	}

	public void setSaleRepresentative(String saleRepresentative) {
		this.saleRepresentative = saleRepresentative;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getContact1() {
		return contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public String getContact3() {
		return contact3;
	}

	public void setContact3(String contact3) {
		this.contact3 = contact3;
	} 

}
