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

import de.switajski.priebes.flexibleorders.domain.DeliveryMethod;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.DeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.service.DeliveryMethodDtoConverterService;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryMethodDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@RequestMapping("/deliverymethods")
@Controller
public class DeliveryMethodController extends ExceptionController {

	@Autowired
	private DeliveryMethodRepository deliveryMethodRepo;
	
	@Autowired
	private DeliveryMethodDtoConverterService deliveryMethodDtoConverterService;

	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts) {
		Page<DeliveryMethod> dMethods = deliveryMethodRepo.findAll(new PageRequest(
				page - 1,
				limit));
		JsonObjectResponse response = ExtJsResponseCreator.createResponse(
				JsonSerializationHelper.convertToJsonDeliveryMethodDtos(dMethods
						.getContent()));
		response.setTotal(dMethods.getTotalElements());
		return response;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody JsonObjectResponse create(@RequestBody DeliveryMethodDto cDto)
			throws JsonParseException, JsonMappingException, IOException {
		DeliveryMethod c = deliveryMethodDtoConverterService.toDeliveryMethod(
				cDto,
				new DeliveryMethod());
		deliveryMethodRepo.save(c);
		return ExtJsResponseCreator.createResponse(c);
	}

	@RequestMapping(value = "/udpate", method = RequestMethod.POST)
	public @ResponseBody JsonObjectResponse udpate(@RequestBody DeliveryMethodDto cDto)
			throws JsonParseException, JsonMappingException, IOException {
		DeliveryMethod c = deliveryMethodDtoConverterService.toDeliveryMethod(
				cDto,
				deliveryMethodRepo.findOne(cDto.getId()));
		deliveryMethodRepo.save(c);
		return ExtJsResponseCreator.createResponse(c);
	}

}
