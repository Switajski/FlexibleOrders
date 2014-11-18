package de.switajski.priebes.flexibleorders.service.conversion;

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
		if (dto.name1 == null) dto.name1 = dto.firstName;
		if (dto.name2 == null) dto.name2 = dto.lastName;
		Address invoiceAddress = new Address(dto.name1, dto.name2, dto.street, dto.postalCode, 
				dto.city, Country.DEUTSCHLAND);
		Address deliveryAddress = new Address(dto.dname1, dto.dname2, dto.dstreet, dto.dpostalCode, 
				dto.dcity, Country.DEUTSCHLAND);
		ContactInformation info = new ContactInformation();
		info.setContact1(dto.contact1);
		info.setContact2(dto.contact2);
		info.setContact3(dto.contact3);
		info.setContact4(dto.contact4);
		
		CustomerDetails cd = new CustomerDetails();
		cd.setVatIdNo(dto.vatIdNo);
		cd.setVendorNumber(dto.vendorNumber);
		cd.setPaymentConditions(dto.paymentConditions);
		cd.setContactInformation(info);
		cd.setMark(dto.mark);
		cd.setSaleRepresentative(dto.saleRepresentative);
		
		customer.setInvoiceAddress(invoiceAddress);
		customer.setShippingAddress(deliveryAddress);
		customer.setDetails(cd);
		customer.setCustomerNumber(dto.customerNumber);
		customer.setEmail(dto.email);
		customer.setFirstName(dto.firstName);
		customer.setCompanyName(dto.companyName);
		customer.setLastName(dto.lastName);
		customer.setEmail(dto.email);
		customer.setPhone(dto.phone);
		customer.setFax(dto.fax);
		customer.setNotes(dto.notes);
		return customer;
	}
	
	public static List<CustomerDto> convertToJsonCustomers(
            Collection<Customer> customers) {
        List<CustomerDto> jsonCustomers = new ArrayList<CustomerDto>();
        for (Customer c : customers){
            CustomerDto jc = new CustomerDto();
            jc.customerNumber = c.getCustomerNumber();
            jc.companyName = c.getCompanyName();
            jc.lastName = c.getLastName();
            jc.firstName = c.getFirstName();
            jc.email = c.getEmail();
            jc.phone = c.getPhone();
            jc.fax = c.getFax();
            jc.notes = c.getNotes();
            
            jc.name1 = c.getInvoiceAddress().getName1();
            jc.name2 = c.getInvoiceAddress().getName2();
            jc.street = c.getInvoiceAddress().getStreet();
            jc.postalCode = c.getInvoiceAddress().getPostalCode();
            jc.city = c.getInvoiceAddress().getCity();
            jc.country = c.getInvoiceAddress().getCountry().toString();
            
            if (c.getShippingAddress() != null) {
                jc.dname1 = c.getShippingAddress().getName1();
                jc.dname2 = c.getShippingAddress().getName2();
                jc.dstreet = c.getShippingAddress().getStreet();
                jc.dpostalCode = c.getShippingAddress().getPostalCode();
                jc.dcity = c.getShippingAddress().getCity();
                jc.dcountry = c.getShippingAddress().getCountry().toString();
            }
            
            CustomerDetails details = c.getDetails();
            if (details != null) {
                jc.vendorNumber = details.getVendorNumber();
                jc.vatIdNo = details.getVatIdNo();
                jc.paymentConditions = details.getPaymentConditions();
                if (details.getContactInformation() != null) {
                    jc.contact1 = details.getContactInformation().getContact1();
                    jc.contact2 = details.getContactInformation().getContact2();
                    jc.contact3 = details.getContactInformation().getContact3();
                    jc.contact4 = details.getContactInformation().getContact4();
                }
                jc.mark = details.getMark();
                jc.saleRepresentative = details.getSaleRepresentative();
            }
            jsonCustomers.add(jc);
        }
        return jsonCustomers;
    }
	
}
