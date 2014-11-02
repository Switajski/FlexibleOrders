package de.switajski.priebes.flexibleorders.web.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoProductDto {

    public String entity_id, type_id, sku, regular_price_without_tax, name;
    
}
