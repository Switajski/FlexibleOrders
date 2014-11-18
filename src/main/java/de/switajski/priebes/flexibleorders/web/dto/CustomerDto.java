package de.switajski.priebes.flexibleorders.web.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;

/**
 * TODO this object is disposable by good serialization
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {

	public Long customerNumber;

	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String email, password, companyName, firstName, lastName, phone, fax, notes;

    //*********************
    // invoice address attributes
    //*********************
	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String name1, name2, street, city, country;
    
    public int postalCode;
    
    //*********************
    // delivery address attributes
    //*********************
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String dname1, dname2, dstreet, dcity, dcountry;

    public int dpostalCode;

    //*********************
    // Customer details attributes
    //*********************
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String vendorNumber, vatIdNo, paymentConditions;
    
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String saleRepresentative, mark, contact1, contact2, contact3, contact4;

}
