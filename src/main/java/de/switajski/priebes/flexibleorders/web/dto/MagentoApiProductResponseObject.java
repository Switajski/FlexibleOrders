package de.switajski.priebes.flexibleorders.web.dto;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoApiProductResponseObject extends LinkedHashMap<Long, MagentoProductDto> {

    private static final long serialVersionUID = 1L;

}
