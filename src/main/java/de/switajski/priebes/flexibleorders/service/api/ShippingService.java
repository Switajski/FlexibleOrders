package de.switajski.priebes.flexibleorders.service.api;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.exceptions.DeviatingExpectedDeliveryDatesException;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.repository.specification.HasCustomerSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.IsConfirmationItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.OverdueItemSpecification;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ShippingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemRepository reportItemRepo;
    @Autowired
    private PurchaseAgreementReadService purchaseAgreementService;
    @Autowired
    private ExpectedDeliveryService expectedDeliveryService;
    @Autowired
    private ReportItemToItemDtoConversionService reportItemToItemDtoConversionService;

    /**
     * 
     * @param valid
     *            deliverParameter
     * @return created delivery notes if successful
     * @throws ContradictoryAddressException
     * @throws DeviatingExpectedDeliveryDatesException
     * @throws NoItemsToShipFoundException
     */
    @Transactional
    public Set<ItemDto> ship(@Valid DeliverParameter deliverParameter)
            throws ContradictoryAddressException,
            DeviatingExpectedDeliveryDatesException,
            NoItemsToShipFoundException {

        DeliveryNotes deliveryNotes = new DeliveryNotes(deliverParameter);
        Long customerNumber = deliverParameter.getCustomerId();
        for (ItemDto itemDto : deliverParameter.getItems()) {
            ReportItem newItem = null;
            if (itemDto.isOffTheRecord()) {
                Order order = createOffTheRecordOrder(itemDto);

            }
            else {
                ReportItem itemToBeShipped = resolveItemToBeShipped(customerNumber, itemDto);
                if (itemToBeShipped == null) throw new NoItemsToShipFoundException();

                newItem = createReportItemByItemToBeShipped(itemDto, itemToBeShipped);
            }
            if (newItem == null) throw new NoItemsToShipFoundException();
            deliveryNotes.addItem(newItem);
        }

        if (!deliverParameter.isIgnoreContradictoryExpectedDeliveryDates()) {
            expectedDeliveryService.validateExpectedDeliveryDates(deliveryNotes.getItems(), deliveryNotes.getCreated());
        }
        Address shippingAddress = purchaseAgreementService.retrieveShippingAddressOrFail(deliveryNotes.getItems());
        deliveryNotes.setShippedAddress(shippingAddress);

        DeliveryNotes dn = reportRepo.save(deliveryNotes);

        return dn.getItems().stream()
                .map(item -> reportItemToItemDtoConversionService.convert(item, item.getQuantity()))
                .collect(Collectors.toSet());
    }

    private Order createOffTheRecordOrder(ItemDto itemDto) {
        throw new NotImplementedException();
        // OrderItem orderItem = new OrderItem(new Order());
        //
        // ShippingItem item = new ShippingItem();
        // item.setCreated(new Date());
        // item.setQuantity(itemDto.getQuantityLeft());
        // return item;
    }

    /**
     * 
     * @param customerNumber
     * @param itemDto
     * @return null if item couldn't be resolved
     */
    private ReportItem resolveItemToBeShipped(Long customerNumber, ItemDto itemDto) {
        ReportItem itemToBeShipped = null;
        if (itemDto.getId() != null) {
            itemToBeShipped = resolveAccordingToItemId(itemDto);
        }
        else if (customerNumber != null) {
            itemToBeShipped = resolveAccordingToCustomerAndOverdueItems(customerNumber.toString(), itemDto);
        }
        return itemToBeShipped;
    }

    @Transactional
    public ReportItem createReportItemByItemToBeShipped(ItemDto itemDto, ReportItem itemToBeShipped) {
        if (itemDto.getProductType() != ProductType.SHIPPING) {
            int qty = itemDto.getQuantityLeft();
            OrderItem orderItemToBeDelivered = itemToBeShipped.getOrderItem();
            QuantityUtility.validateQuantity(qty, itemToBeShipped);
            if (itemDto.isPending() == false) {
                ShippingItem shippingItem = new ShippingItem(
                        null,
                        orderItemToBeDelivered,
                        qty,
                        new Date());
                shippingItem.setPredecessor(itemToBeShipped);
                shippingItem.setPackageNumber(itemDto.getPackageNumber());
                shippingItem.setTrackNumber(itemDto.getTrackNumber());
                return shippingItem;
            }
            else {
                return new PendingItem(
                        null,
                        orderItemToBeDelivered,
                        qty,
                        new Date());
            }
        }
        return null;
    }

    private ReportItem resolveAccordingToCustomerAndOverdueItems(String customerNumber, ItemDto itemDto) {
        List<ReportItem> overdueConfirmationItemsToBeShipped = reportItemRepo
                .findAll(where(new HasCustomerSpecification(customerNumber)).and(new IsConfirmationItemSpecification()).and(new OverdueItemSpecification()));

        List<ReportItem> matchingConfirmationItems = overdueConfirmationItemsToBeShipped.stream()
                .filter(ri -> ri.getOrderItem().getProduct().getProductNumber().equals(itemDto.getProduct()))
                .filter(ri -> itemDto.getQuantityLeft() <= ri.overdue())
                .collect(Collectors.toList());

        int noMatching = matchingConfirmationItems.size();
        if (noMatching < 1) return null;
        if (noMatching == 1) {
            ReportItem matchingConfirmationItem = matchingConfirmationItems.iterator().next();
            return matchingConfirmationItem;
        }
        if (1 < noMatching) {
            Collections.sort(matchingConfirmationItems, (ReportItem r1, ReportItem r2) -> r1.getCreated().compareTo(r2.getCreated()));
            return matchingConfirmationItems.iterator().next();
        }

        return null;
    }

    private ReportItem resolveAccordingToItemId(ItemDto itemDto) {
        ReportItem itemToBeShipped;
        itemToBeShipped = reportItemRepo.findOne(itemDto.getId());
        if (itemToBeShipped == null) {
            throw new IllegalArgumentException("Angegebene Id zur Position nicht gefunden");
        }
        return itemToBeShipped;
    }

    @Transactional
    public Set<ItemDto> shipMany(DeliverParameter deliverParameter)
            throws ContradictoryAddressException,
            DeviatingExpectedDeliveryDatesException,
            NoItemsToShipFoundException {
        Set<ItemDto> savedDeliveryNotes = new HashSet<>();
        String originalDeliveryNotesNumber = deliverParameter.getDeliveryNotesNumber();
        deliverParameter.setPackageNumber(null);

        Map<String, List<ItemDto>> packages = new HashMap<String, List<ItemDto>>();
        for (ItemDto itemToBeShipped : deliverParameter.getItems()) {
            String packageNumber = itemToBeShipped.getPackageNumber();
            if (packageNumber == null) packageNumber = "";
            if (packages.get(packageNumber) == null) {
                packages.put(packageNumber, new ArrayList<ItemDto>());
            }
            packages.get(packageNumber).add(itemToBeShipped);
        }

        for (String packageNumber : packages.keySet()) {
            deliverParameter.setItems(packages.get(packageNumber));
            String suffix = packageNumber.equals("") ? "" : "-" + packageNumber;
            deliverParameter.setDeliveryNotesNumber(originalDeliveryNotesNumber.concat(suffix));
            deliverParameter.setPackageNumber(packageNumber);
            savedDeliveryNotes.addAll(ship(deliverParameter));
        }
        return savedDeliveryNotes;
    }

}
