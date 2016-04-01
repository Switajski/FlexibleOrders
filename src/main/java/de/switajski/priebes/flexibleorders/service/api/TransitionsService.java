package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.CancelReport;
import de.switajski.priebes.flexibleorders.domain.report.CancellationItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.CatalogProductServiceByMagento;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * @author Marek Switajski
 *
 */
@Service
public class TransitionsService {

    private static Logger log = Logger.getLogger(TransitionsService.class);

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CatalogProductServiceByMagento cProductService;

    /**
     * Creates initially an order with its order items
     *
     * @param orderParameter
     *
     * @return created order, when successfully persisted
     */
    @Transactional
    public Order order(@Valid OrderParameter orderParameter) {

        Long customerNumber = orderParameter.getCustomerNumber();
        Order order = new Order(
                customerRepo.findByCustomerNumber(customerNumber),
                OriginSystem.FLEXIBLE_ORDERS,
                orderParameter.getOrderNumber());
        order.setCreated((orderParameter.getCreated() == null) ? new Date() : orderParameter.getCreated());
        PurchaseAgreement purchaseAgreement = new PurchaseAgreement();
        purchaseAgreement.setCustomerNumber(customerNumber);
        purchaseAgreement.setExpectedDelivery(orderParameter.getExpectedDelivery());

        for (ItemDto ri : orderParameter.getItems()) {
            validate(ri);
            Product product = (ri.getProduct().equals("0")) ? createCustomProduct(ri) : createProductFromCatalog(ri);
            OrderItem oi = new OrderItem(
                    order,
                    product,
                    ri.getQuantity());
            oi.setNegotiatedPriceNet(new Amount(
                    ri.getPriceNet(),
                    Currency.EUR));
            oi.setAdditionalInfo(ri.getAdditionalInfo());
            order.addOrderItem(oi);
        }

        return orderRepo.save(order);
    }

    private void validate(ItemDto ri) {
        if (ri.getProductName() == null) throw new IllegalArgumentException("Produktnamen nicht angegeben");
    }

    private Product createProductFromCatalog(ItemDto ri) {
        Product product;

        try {
            CatalogProduct cProduct = cProductService.findByProductNumber(ri.getProduct());
            if (cProduct == null) throw new IllegalArgumentException("Artikelnr nicht gefunden");
            product = cProduct.toProduct();
            product.setName(ri.getProductName());
            return product;
        }
        catch (Exception e) {
            log.warn("Could not find ProductNumber " + ri.getProduct() + " in Catalog");

            Product p = new Product();
            p.setName((ri.getProductName()));
            p.setProductType(ProductType.PRODUCT);
            p.setProductNumber(ri.getProduct());
            return p;
        }
    }

    private Product createCustomProduct(ItemDto ri) {
        Product p = new Product();
        p.setName(ri.getProductName());
        p.setProductType(ProductType.CUSTOM);
        return p;
    }

    @Transactional
    public boolean deleteOrder(String orderNumber) {
        Order order = orderRepo.findByOrderNumber(orderNumber);
        if (order == null) throw new IllegalArgumentException(
                "Bestellnr. zum l" + Unicode.O_UML + "schen nicht gefunden");
        for (OrderItem oi : order.getItems()) {
            if (!oi.getReportItems().isEmpty()) {
                throw new RuntimeException("Kann nicht l" + Unicode.O_UML + "schen: Bestellung hat Dokumente wie ABs, Lieferscheine, Rechnungen etc.");
            }
        }
        orderRepo.delete(order);
        return true;
    }

    @Transactional
    public CancelReport cancelReport(String reportNo) {
        Report cr = reportRepo.findByDocumentNumber(reportNo);
        if (cr == null) throw new IllegalArgumentException("Angegebene Dokumentennummer nicht gefunden");
        CancelReport cancelReport = createCancelReport(cr);
        return reportRepo.save(cancelReport);
    }

    private CancelReport createCancelReport(Report cr) {
        CancelReport cancelReport = new CancelReport("ABGEBROCHEN-"
                + cr.getDocumentNumber());
        for (ReportItem he : cr.getItems()) {
            cancelReport.addItem(new CancellationItem(
                    cancelReport,
                    he.getOrderItem(),
                    he.getQuantity(),
                    new Date()));
        }
        return cancelReport;
    }

    @Transactional
    public boolean deleteReport(String reportNumber) {
        Report r = reportRepo.findByDocumentNumber(reportNumber);
        if (r == null) throw new IllegalArgumentException(
                "Bericht zum l" + Unicode.O_UML + "schen nicht gefunden");
        if (r.hasConsecutiveDocuments()) throw new IllegalStateException("L" + Unicode.O_UML + "schen nicht m" + Unicode.O_UML
                + "glich, da noch abgeleitete Dokumente existieren");
        reportRepo.delete(r);
        return true;
    }

    @Transactional(readOnly = true)
    public Order retrieveOrder(String orderNumber) {
        Order order = orderRepo.findByOrderNumber(orderNumber);
        order.getCustomer();
        order.getItems();
        return orderRepo.findByOrderNumber(orderNumber);
    }

}
