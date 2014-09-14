package de.switajski.priebes.flexibleorders.service.helper;

import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ItemDtoFilterHelper {
	
	public static List<ItemDto> filterQtyLeftZero(List<ItemDto> itemDtos){
		List<ItemDto> returnedItemDto = new ArrayList<ItemDto>();
		for (ItemDto itemDto:itemDtos)
			if (itemDto.quantityLeft != 0)
				returnedItemDto.add(itemDto);
		return returnedItemDto;
	}

}
