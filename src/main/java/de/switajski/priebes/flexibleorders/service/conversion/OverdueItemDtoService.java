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

    /**
     * @deprecated this method was used before successor / predecessor in
     *             reporItem mapping. use
     *             {@link #createOverdue(ReportItem, int)}
     * @param ri
     * @return
     */
    @Deprecated
    @Transactional(readOnly = true)
    public ItemDto createOverdue(ReportItem ri) {
        int toBeProcessed = new QuantityToBeProcessedDeterminator(ri).overdueQuantity();
        if (toBeProcessed == 0) {
            return null;
        }
        return createOverdue(ri, toBeProcessed);
    }

    @Transactional(readOnly = true)
    public ItemDto createOverdue(ReportItem ri, int toBeProcessed) {
        ItemDto item = new ItemDto();
        item.quantity = toBeProcessed;
        item.quantityLeft = toBeProcessed;
        item.documentNumber = ri.getReport().getDocumentNumber();
        if (ri.getReport() instanceof OrderConfirmation) {
            item.orderConfirmationNumber = ri.getReport().getDocumentNumber();
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
        item.status = ri.provideStatus();
        return item;
    }

}
