package de.switajski.priebes.flexibleorders.domain;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import flexjson.JSONSerializer;


public class OrderItemServiceImpl implements OrderItemService {
	
	public List<OrderItem> findByOrderNumber(long orderNumber){
		return orderItemRepository.findByOrderNumber(orderNumber);
	}
}
