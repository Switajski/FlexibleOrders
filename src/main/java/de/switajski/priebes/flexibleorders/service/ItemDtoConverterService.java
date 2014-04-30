package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ItemDtoConverterService {

	private ReportItemRepository reportItemRepo;

	@Autowired
	public ItemDtoConverterService(ReportItemRepository reportItemRepo) {
		this.reportItemRepo = reportItemRepo;
	}
	
	public ItemDto convert(OrderItem orderItem) {
		ItemDto item = new ItemDto();
		item.setCreated(orderItem.getCreated());
		item.setCustomer(orderItem.getOrder().getCustomer().getId());
		item.setCustomerNumber(orderItem
				.getOrder()
				.getCustomer()
				.getCustomerNumber());
		item.setCustomerName(orderItem.getOrder().getCustomer().getLastName());
		item.setId(orderItem.getId());
		if (orderItem.getNegotiatedPriceNet() != null)
			item.setPriceNet(orderItem.getNegotiatedPriceNet().getValue());
		item.setOrderNumber(orderItem.getOrder().getOrderNumber());
		item.setProduct(orderItem.getProduct().getProductNumber());
		item.setProductName(orderItem.getProduct().getName());
		item.setStatus(orderItem.provideStatus());
		item.setQuantity(orderItem.getOrderedQuantity());
		item.setQuantityLeft(new QuantityLeftCalculator().calculate(
				orderItem,
				ReportItemType.CONFIRM));
		return item;
	}

	public Set<ItemDto> convertToReportItems(OrderItem orderItem,
			ReportItemType type) {
		Set<ItemDto> ris = new HashSet<ItemDto>();
		for (ReportItem ri : orderItem.getAllHesOfType(type)) {
			ItemDto item = ri.toItemDto();
			item.setQuantityLeft(new QuantityLeftCalculator().calculate(
					ri,
					type));
			ris.add(item);
		}
		return ris;
	}

	@Transactional(readOnly=true)
	public Set<ReportItem> convert(List<ItemDto> itemDtos) {
		Set<ReportItem> ris = new HashSet<ReportItem>();
		for (ItemDto entry : itemDtos) {
			ReportItem ri = reportItemRepo.findOne(entry.getId());
			if (ri != null)
				ris.add(ri);
		}
		return ris;
	}

	public Set<ShippingItem> convertToShippingItems(
			List<ItemDto> shippingItemDtos) {
		Set<ShippingItem> sis = new HashSet<ShippingItem>();
		Set<ReportItem> ris = convert(shippingItemDtos);
		for (ReportItem ri:ris)
			if (ri instanceof ShippingItem)
				sis.add((ShippingItem) ri);
			else
				throw new IllegalArgumentException("Given ItemDto is not a shipping item");
		return sis;
	}

}
