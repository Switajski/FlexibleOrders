package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ItemDtoConverterService {

	@Autowired
	private ReportItemRepository reportItemRepo;

	public List<ItemDto> convertOrderItems(Collection<OrderItem> orderItems){
		List<ItemDto> items = new ArrayList<ItemDto>();
		for (OrderItem oi:orderItems)
			items.add(convert(oi));
		return items;
	}
	
	public ItemDto convert(OrderItem orderItem) {
		ItemDto item = new ItemDto();
		Order order = orderItem.getOrder();
		if (order != null){
			item.setCustomer(order.getCustomer().getId());
			item.setCustomerNumber(order
					.getCustomer()
					.getCustomerNumber());
			item.setCustomerName(order.getCustomer().getLastName());
			item.setOrderNumber(order.getOrderNumber());
		}
		item.setCreated(orderItem.getCreated());
		item.setId(orderItem.getId());
		if (orderItem.getNegotiatedPriceNet() != null)
			item.setPriceNet(orderItem.getNegotiatedPriceNet().getValue());
		item.setProduct(orderItem.getProduct().getProductNumber());
		item.setProductName(orderItem.getProduct().getName());
		item.setStatus(DeliveryHistory.createFrom(orderItem).provideStatus());
		item.setQuantity(orderItem.getOrderedQuantity());
		item.setQuantityLeft(QuantityCalculator.calculateLeft(orderItem));
		return item;
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
	
	public List<ItemDto> convert(Collection<Report> reports){
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (Report report : reports) {
			ris.addAll(convertReport(report));
		}
		return ris;
	}
	
	public List<ItemDto> convertReport(Report report){
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (ReportItem ri : report.getItems()) {
			ris.add(convert(ri));
		}
		return ris;
	}
	
	public ItemDto convert(ReportItem ri){
			ItemDto item = new ItemDto();
			item.setDocumentNumber(ri.getReport().getDocumentNumber());
			
			if (ri.getReport() instanceof OrderConfirmation){
				item.setOrderConfirmationNumber(ri.getReport().getDocumentNumber());
			}
			if (ri.getReport() instanceof Invoice){
				Invoice invoice = (Invoice) ri.getReport();
				item.setInvoiceNumber(ri.getReport().getDocumentNumber());
				item.setDeliveryNotesNumber(ri.getReport().getDocumentNumber());
				item.setPaymentConditions(invoice.getPaymentConditions());
			}
			if (ri.getReport() instanceof DeliveryNotes){
				DeliveryNotes deliveryNotes = (DeliveryNotes) ri.getReport();
				item.setDeliveryNotesNumber(ri.getReport().getDocumentNumber());
				item.setTrackNumber(deliveryNotes.getTrackNumber());
				item.setPackageNumber(deliveryNotes.getPackageNumber());
			}
			if (ri.getReport() instanceof Receipt){
				item.setReceiptNumber(ri.getReport().getDocumentNumber());
			}
			item.setCreated(ri.getCreated());
			item.setCustomer(ri.getOrderItem().getOrder().getCustomer().getId());
			item.setCustomerNumber(ri.getOrderItem().getOrder().getCustomer().getCustomerNumber());
			item.setCustomerName(ri.getOrderItem().getOrder().getCustomer().getLastName());
			item.setDocumentNumber(ri.getReport().getDocumentNumber());
			item.setId(ri.getId());
			item.setOrderNumber(ri.getOrderItem().getOrder().getOrderNumber());
			if (ri.getOrderItem().getNegotiatedPriceNet() != null)
				item.setPriceNet(ri.getOrderItem().getNegotiatedPriceNet().getValue());
			item.setProduct(ri.getOrderItem().getProduct().getProductNumber());
			item.setProductName(ri.getOrderItem().getProduct().getName());
			item.setQuantity(ri.getQuantity());
			item.setStatus(ri.provideStatus());
			item.setQuantityLeft(QuantityCalculator.calculateLeft(ri));
		return item;
	}

	public List<ItemDto> convert(Order order){
		List<ItemDto> ois = new ArrayList<ItemDto>();
		for (OrderItem orderItem: order.getItems()){
			ois.add(convert(orderItem));
		}
		return ois;
	}

	public List<ItemDto> convertOrders(Collection<Order> orders) {
		List<ItemDto> items = new ArrayList<ItemDto>();
		for (Order order:orders){
			items.addAll(convert(order));
		}
		return items;
	}

	public List<ItemDto> convertReportItems(List<ReportItem> content) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
			for (ReportItem ri : content) {
				ris.add(convert(ri));
			}
		return ris;
	}

}
