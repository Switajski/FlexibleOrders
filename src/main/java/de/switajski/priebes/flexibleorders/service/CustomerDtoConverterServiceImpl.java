package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;

public class CustomerDtoConverterServiceImpl {

	public static Customer toCustomer(CustomerDto dto, Customer customer) {
		if (dto.getName1() == null) dto.setName1(dto.getFirstName());
		if (dto.getName2() == null) dto.setName2(dto.getLastName());
		Address invoiceAddress = new Address(dto.getName1(), dto.getName2(), dto.getStreet(), dto.getPostalCode(), 
				dto.getCity(), Country.DEUTSCHLAND);
		Address deliveryAddress = new Address(dto.getDname1(), dto.getDname2(), dto.getDstreet(), dto.getDpostalCode(), 
				dto.getDcity(), Country.DEUTSCHLAND);
		ContactInformation info = new ContactInformation();
		info.setContact1(dto.getContact1());
		info.setContact2(dto.getContact2());
		info.setContact3(dto.getContact3());
		info.setContact4(dto.getContact4());
		
		CustomerDetails cd = new CustomerDetails();
		cd.setVatIdNo(dto.getVatIdNo());
		cd.setVendorNumber(dto.getVendorNumber());
		cd.setPaymentConditions(dto.getPaymentConditions());
		cd.setContactInformation(info);
		cd.setMark(dto.getMark());
		cd.setSaleRepresentative(dto.getSaleRepresentative());
		
		customer.setInvoiceAddress(invoiceAddress);
		customer.setShippingAddress(deliveryAddress);
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
