package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.CustomerDetails;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;

public class CustomerDtoConverterServiceImpl {

	public static Customer toCustomer(CustomerDto dto, Customer customer) {
		if (dto.getName1() == null) dto.setName1(dto.getFirstName());
		if (dto.getName2() == null) dto.setName2(dto.getLastName());
		Address a = new Address(dto.getName1(), dto.getName2(), dto.getStreet(), dto.getPostalCode(), 
				dto.getCity(), Country.DEUTSCHLAND);
		CustomerDetails cd = new CustomerDetails();
		cd.setVatIdNo(dto.getVatIdNo());
		cd.setVendorNumber(dto.getVendorNumber());
		cd.setPaymentConditions(dto.getPaymentConditions());
		
		customer.setInvoiceAddress(a);
		customer.setDetails(cd);
		customer.setCustomerNumber(dto.getCustomerNumber());
		customer.setEmail(dto.getEmail());
		customer.setFirstName(dto.getFirstName());
		customer.setLastName(dto.getLastName());
		customer.setEmail(dto.getEmail());
		customer.setPhone(dto.getPhone());
		return customer;
	}
	
}
