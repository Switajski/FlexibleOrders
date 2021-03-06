package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportItemInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedToOneCustomerInPdf;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.specification.HasCustomerSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.IsConfirmationItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.OverdueItemSpecification;
import de.switajski.priebes.flexibleorders.service.conversion.OrderItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportingService {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ReportItemToItemDtoConversionService reportItemToItemDtoConversionService;
    @Autowired
    private ReportItemRepository reportItemRepo;
    @Autowired
    private OrderItemToItemDtoConversionService oi2ItemDtoConversionService;
    @Autowired
    private PurchaseAgreementReadService purchaseAgreementService;

    @Transactional(readOnly = true)
    public Page<ItemDto> toBeConfirmedByCustomer(
            Customer customer,
            Pageable pageable) {
        Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmedByCustomer(
                customer,
                pageable);
        return createPage(
                toBeConfirmed.getTotalElements(),
                pageable,
                oi2ItemDtoConversionService.convertOrders(toBeConfirmed
                        .getContent()));
    }

    /**
     * 
     * @param pageable
     * @param byOrder
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ItemDto> toBeConfirmed(PageRequest pageable) {
        Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmed(pageable);
        return createPage(
                toBeConfirmed.getTotalElements(),
                pageable,
                oi2ItemDtoConversionService.convertOrders(toBeConfirmed.getContent()));
    }

    @Transactional(readOnly = true)
    public List<String> orderNumbersLike(String orderNumber) {
        List<Order> orders = orderRepo.findByOrderNumberLike(orderNumber);
        return extractOrderNumbers(orders);
    }

    private List<String> extractOrderNumbers(List<Order> orders) {
        List<String> orderNumbers = new ArrayList<String>();
        for (Order order : orders) {
            orderNumbers.add(order.getOrderNumber());
        }
        return orderNumbers;
    }

    @Transactional(readOnly = true)
    public Page<String> orderNumbersByCustomer(
            Customer customer,
            PageRequest pageRequest) {

        Page<Order> orders = orderRepo.findByCustomer(customer, pageRequest);
        Page<String> result = extractOrderNumber(orders);
        return result;
    }

    private Page<String> extractOrderNumber(Page<Order> orders) {
        // if no orders are found return empty list
        if (orders.getSize() < 1) return new PageImpl<String>(new ArrayList<String>());

        List<String> ordersList = new ArrayList<String>();
        for (Order order : orders)
            ordersList.add(order.getOrderNumber());

        Page<String> result = new PageImpl<String>(
                ordersList,
                new PageRequest(
                        orders.getSize(),
                        orders.getNumber() + 1),
                orders.getTotalElements());
        return result;
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> createMissing(PageRequest pageRequest, Specification<ReportItem> spec) {
        Page<ReportItem> openReportItems = reportItemRepo.findAll(spec, pageRequest);
        return createMissingItems(pageRequest, openReportItems);
    }

    @Transactional(readOnly = true)
    public PageImpl<ItemDto> createMissingItems(
            PageRequest pageable,
            Page<ReportItem> reportItems) {
        List<ItemDto> overdueItemDtos = createMissingItems(reportItems.getContent());

        PageImpl<ItemDto> reportItemPage = createPage(
                reportItems.getTotalElements(),
                pageable,
                overdueItemDtos);
        return reportItemPage;
    }

    public List<ItemDto> createMissingItems(List<ReportItem> content) {
        return content.stream()
                .filter(ri -> ri.overdue() > 0)
                .map(ri -> reportItemToItemDtoConversionService.convert(ri, ri.overdue()))
                .collect(Collectors.toList());
    }

    public PageImpl<ItemDto> createPage(
            Long totalElements,
            Pageable pageable,
            List<ItemDto> ris) {
        return new PageImpl<ItemDto>(ris, pageable, totalElements);
    }

    @Transactional(readOnly = true)
    public ReportInPdf toBeShippedToCustomer(Customer customer) {
        return createToBeShippedDto(customer, new ToBeShippedToOneCustomerInPdf());
    }

    @Transactional(readOnly = true)
    public ReportInPdf toBeShipped() {
        return createToBeShippedDto(null, new ToBeShippedInPdf());
    }

    private ReportInPdf createToBeShippedDto(Customer customer, ReportInPdf report) {
        report.items = new HashSet<ReportItem>();
        Specifications<ReportItem> specs = Specifications
                .where(new IsConfirmationItemSpecification())
                .and(new OverdueItemSpecification());
        if (customer != null) specs = specs.and(new HasCustomerSpecification(customer));

        List<ReportItem> reportItems = reportItemRepo.findAll(specs);
        for (ReportItem ri : reportItems) {
            if (ri.overdue() > 0) {
                ReportItemInPdf reportItemInPdf = new ReportItemInPdf(ri);
                reportItemInPdf.quantity = ri.overdue();
                report.itemDtos.add(reportItemInPdf);
            }
            if (customer != null) {
                report.customerNumber = customer.getCustomerNumber();
                report.customerFirstName = customer.getFirstName();
                report.customerLastName = customer.getLastName();
                report.subject = "Ausstehende Artikel";
            }
            else report.customerFirstName = "Alle Kunden";

        }
        if (customer != null) {
            Collection<Address> sAddresses = purchaseAgreementService.shippingAddressesWithoutDeviations(reportItems);
            if (sAddresses.size() > 0) {
                report.shippingSpecific_shippingAddress = sAddresses.iterator().next();
            }
        }
        return report;
    }

}
