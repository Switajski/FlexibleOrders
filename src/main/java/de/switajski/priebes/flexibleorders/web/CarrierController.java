package de.switajski.priebes.flexibleorders.web;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Carrier;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CarrierRepository;
import de.switajski.priebes.flexibleorders.service.CarrierDtoConverterServiceImpl;
import de.switajski.priebes.flexibleorders.web.dto.CarrierDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@RequestMapping("/carriers")
@Controller
public class CarrierController extends ExceptionController {

	@Autowired
	private CarrierRepository carrierRepo;

	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts) {
		Page<Carrier> carriers = carrierRepo.findAll(new PageRequest(
				page - 1,
				limit));
		JsonObjectResponse response = ExtJsResponseCreator.createResponse(
				JsonSerializationHelper.convertToJsonCarriers(carriers
						.getContent()));
		response.setTotal(carriers.getTotalElements());
		return response;
	}

	// TODO: illegal use of customerRepository
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody JsonObjectResponse create(@RequestBody CarrierDto cDto)
			throws JsonParseException, JsonMappingException, IOException {
		Carrier c = CarrierDtoConverterServiceImpl.toCarrier(
				cDto,
				new Carrier());
		carrierRepo.save(c);
		return ExtJsResponseCreator.createResponse(c);
	}

	// TODO: illegal use of customerRepository 
	@RequestMapping(value = "/udpate", method = RequestMethod.POST)
	public @ResponseBody JsonObjectResponse udpate(@RequestBody CarrierDto cDto)
			throws JsonParseException, JsonMappingException, IOException {
		Carrier c = CarrierDtoConverterServiceImpl.toCarrier(
				cDto,
				carrierRepo.findOne(cDto.getId()));
		carrierRepo.save(c);
		return ExtJsResponseCreator.createResponse(c);
	}

}
