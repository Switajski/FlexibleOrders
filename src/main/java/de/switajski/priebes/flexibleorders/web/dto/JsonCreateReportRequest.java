package de.switajski.priebes.flexibleorders.web.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;
import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.JsonJodaLocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonJodaLocalDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Country;

/**
 * TODO: try Jackson serializer with public attributes - anyhow this is a data structure
 * Can have several orders
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class JsonCreateReportRequest {
	
	public Long customerId;
	
	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String paymentConditions, 
	    mark;
	
	@JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	public Date created;

	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String name1, 
	    name2, 
	    street, 
	    city, 
	    country;
	
	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String 
	    contact1, 
	    contact2, 
	    contact3, 
	    contact4;
	
	public Integer postalCode;
	
	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String 
	    dname1, 
	    dname2, 
	    dstreet, 
	    dcity, 
	    dcountry;
	
	public Integer dpostalCode;
	
	public List<ItemDto> items;

	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String invoiceNumber, 
	    deliveryNotesNumber, 
	    orderNumber;
	
	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String trackNumber, 
	    packageNumber;

	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String billing;
	
	@JsonDeserialize(using = BigDecimalDeserializer.class)
	public BigDecimal shipment;

	@JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
	public String orderConfirmationNumber;

	@JsonSerialize(using = JsonJodaLocalDateSerializer.class)
	@JsonDeserialize(using = JsonJodaLocalDateDeserializer.class)
	public LocalDate expectedDelivery;

	public Long deliveryMethodNo;

    public String saleRepresentative, 
        valueAddedTaxIdNo, 
        vendorNumber;

		
	public Address createInvoiceAddress(){
		return new Address(name1, name2, street, postalCode, city,
				Country.DEUTSCHLAND);
	}
	
	public Address createDeliveryAddress(){
		return new Address(dname1, dname2, dstreet, dpostalCode, dcity,
				Country.DEUTSCHLAND);
	}

	public void validate(){
		if (items.isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben!");
		for (ItemDto item:items){
			if (item.quantity < 1)
				throw new IllegalArgumentException("Menge von "+item.productName+" ist kleiner als 1");
			if (item.priceNet == null)
				throw new IllegalArgumentException("Keinen Preis angegeben");
		}
	}

}
