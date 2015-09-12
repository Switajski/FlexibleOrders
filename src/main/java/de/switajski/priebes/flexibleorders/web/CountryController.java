package de.switajski.priebes.flexibleorders.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.CountryDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@Controller
public class CountryController {

    @RequestMapping("/country")
    public @ResponseBody JsonObjectResponse listAll(){
        Set<CountryDto> countries = new HashSet<CountryDto>();
        for (Country c : Country.values()){
            CountryDto dto = new CountryDto();
            dto.id = c.name();
            dto.country = c.getName();
            countries.add(dto);
        }
        JsonObjectResponse response = ExtJsResponseCreator.createResponse(countries);
        response.setTotal(Country.values().length);
        return response;
    }
    
}
