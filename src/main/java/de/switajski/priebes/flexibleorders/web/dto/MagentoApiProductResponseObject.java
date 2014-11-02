package de.switajski.priebes.flexibleorders.web.dto;

import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoApiProductResponseObject extends HashMap<Long, MagentoProductDto>{

    private static final long serialVersionUID = 1L;

}
