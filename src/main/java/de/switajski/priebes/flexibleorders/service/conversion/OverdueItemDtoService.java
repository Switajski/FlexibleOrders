package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class OverdueItemDtoService {

    @Transactional(readOnly = true)
    public ItemDto createOverdue(ReportItem ri) {
        ItemDto item = new ItemDto();
        item.documentNumber = ri.getReport().getDocumentNumber();
        // TODO: instanceof: this is not subject of this class
        if (ri.getReport() instanceof OrderConfirmation) {
            item.orderConfirmationNumber = ri.getReport().getDocumentNumber();
            // TODO: should be done by a service
            OrderConfirmation orderConfirmation = (OrderConfirmation) ri.getReport();
            item.agreed = orderConfirmation.isAgreed();
            PurchaseAgreement pa = orderConfirmation.getPurchaseAgreement();
            if (pa != null) {
                item.expectedDelivery = pa.getExpectedDelivery();
            }
        }
        if (ri.getReport() instanceof Invoice) {
            item.invoiceNumber = ri.getReport().getDocumentNumber();
            item.shareHistory = (DeliveryHistory.of(ri).getInvoiceNumbers().size() > 1) ? true : false;
        }
        if (ri.getReport() instanceof DeliveryNotes) {
            item.deliveryNotesNumber = ri.getReport().getDocumentNumber();
            // TODO refactor to separate class
            item.shareHistory = (DeliveryHistory.of(ri).getDeliveryNotesNumbers().size() > 1) ? true : false;
        }
        if (ri.getReport() instanceof Receipt) {
            item.receiptNumber = ri.getReport().getDocumentNumber();
        }
        item.created = ri.getCreated();
        Order order = ri.getOrderItem().getOrder();
        item.customer = order.getCustomer().getId();
        item.customerNumber = order.getCustomer().getCustomerNumber();
        item.customerName = order.getCustomer().getCompanyName();
        item.documentNumber = ri.getReport().getDocumentNumber();
        item.id = ri.getId();
        item.orderNumber = order.getOrderNumber();
        if (ri.getOrderItem().getNegotiatedPriceNet() != null) {
            item.priceNet = ri.getOrderItem().getNegotiatedPriceNet().getValue();
        }
        item.product = ri.getOrderItem().getProduct().getProductNumber();
        item.productType = ri.getOrderItem().getProduct().getProductType();
        item.productName = ri.getOrderItem().getProduct().getName();
        item.quantity = ri.toBeProcessed();
        item.status = ri.provideStatus();
        // item.quantityLeft = ri.toBe;
        return item;
    }

}
