package de.switajski.priebes.flexibleorders.web.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;

/**
 * 
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {

    public Long customerNumber;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String email, password, firstName, lastName, phone, fax, notes;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    @NotNull
    public String companyName;

    // *********************
    // invoice address attributes
    // *********************
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String name1, name2, street, city, country;

    public int postalCode;

    // *********************
    // delivery address attributes
    // *********************
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String dname1, dname2, dstreet, dcity, dcountry;

    public int dpostalCode;

    // *********************
    // Customer details attributes
    // *********************
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String vendorNumber, vatIdNo, paymentConditions;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String saleRepresentative, mark, contact1, contact2, contact3, contact4;

}
