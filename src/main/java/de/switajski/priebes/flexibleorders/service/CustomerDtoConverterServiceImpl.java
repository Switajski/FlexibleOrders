package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;

//TODO: Move this translation to spring Framework 
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
	
	public static List<CustomerDto> convertToJsonCustomers(
            Collection<Customer> customers) {
        List<CustomerDto> jsonCustomers = new ArrayList<CustomerDto>();
        for (Customer c : customers){
            CustomerDto jc = new CustomerDto();
            jc.setId(c.getCustomerNumber());
            jc.setCustomerNumber(c.getCustomerNumber());
            jc.setLastName(c.getLastName());
            jc.setFirstName(c.getFirstName());
            jc.setEmail(c.getEmail());
            jc.setPhone(c.getPhone());
            
            jc.setName1(c.getInvoiceAddress().getName1());
            jc.setName2(c.getInvoiceAddress().getName2());
            jc.setStreet(c.getInvoiceAddress().getStreet());
            jc.setPostalCode(c.getInvoiceAddress().getPostalCode());
            jc.setCity(c.getInvoiceAddress().getCity());
            jc.setCountry(c.getInvoiceAddress().getCountry().toString());
            
            if (c.getShippingAddress() != null) {
                jc.setDname1(c.getShippingAddress().getName1());
                jc.setDname2(c.getShippingAddress().getName2());
                jc.setDstreet(c.getShippingAddress().getStreet());
                jc.setDpostalCode(c.getShippingAddress().getPostalCode());
                jc.setDcity(c.getShippingAddress().getCity());
                jc.setDcountry(c.getShippingAddress().getCountry().toString());
            }
            
            CustomerDetails details = c.getDetails();
            if (details != null) {
                jc.setVendorNumber(details.getVendorNumber());
                jc.setVatIdNo(details.getVatIdNo());
                jc.setPaymentConditions(details.getPaymentConditions());
                jc.setContact1(details.getContactInformation().getContact1());
                jc.setContact2(details.getContactInformation().getContact2());
                jc.setContact3(details.getContactInformation().getContact3());
                jc.setContact4(details.getContactInformation().getContact4());
                jc.setMark(details.getMark());
                jc.setSaleRepresentative(details.getSaleRepresentative());
            }
            jsonCustomers.add(jc);
        }
        return jsonCustomers;
    }
	
}
