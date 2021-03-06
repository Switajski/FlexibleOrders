package de.switajski.priebes.flexibleorders.web;

import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.conversion.CustomerDtoConverterServiceImpl;
import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@RequestMapping("/customers")
@Controller
public class CustomerController extends ExceptionController {

    @Autowired
    private CustomerRepository customerRepo;

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listAll(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "query", required = false) String query) {
        PageRequest pageable = new PageRequest(
                page - 1,
                limit,
                Direction.ASC,
                "companyName",
                "lastName",
                "firstName");
        Page<Customer> customers;
        if (!StringUtils.isEmpty(StringUtils.stripToEmpty(query))) {
            customers = customerRepo.search(pageable, query);
        }
        else customers = customerRepo.findAll(pageable);
        JsonObjectResponse response = ExtJsResponseCreator.createSuccessResponse(
                CustomerDtoConverterServiceImpl.convertToJsonCustomers(customers
                        .getContent()));
        response.setTotal(customers.getTotalElements());
        return response;
    }

    @RequestMapping(value = "listitems", produces = "text/html")
    public String confirm(Model uiModel) {
        return "customers/listitems";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse create(@RequestBody @Valid CustomerDto cDto)
            throws JsonParseException,
            JsonMappingException,
            IOException {
        Customer c = CustomerDtoConverterServiceImpl.toCustomer(
                cDto,
                new Customer());
        if (c.getCustomerNumber() == null) throw new IllegalArgumentException("Keine Kundennummer angegeben");
        if (customerRepo.findByCustomerNumber(c.getCustomerNumber()) != null) {
            throw new IllegalArgumentException("Kundennummer besteht bereits");
        }
        customerRepo.save(c);
        return ExtJsResponseCreator.createSuccessResponse(c);
    }

    @RequestMapping(value = "/udpate", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse udpate(@RequestBody CustomerDto cDto)
            throws JsonParseException,
            JsonMappingException,
            IOException {
        Customer c = CustomerDtoConverterServiceImpl.toCustomer(
                cDto,
                customerRepo.findByCustomerNumber(cDto.customerNumber));
        customerRepo.save(c);
        return ExtJsResponseCreator.createSuccessResponse(c);
    }

}
