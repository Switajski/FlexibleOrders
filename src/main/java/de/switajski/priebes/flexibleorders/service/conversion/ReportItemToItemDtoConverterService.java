package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportItemToItemDtoConverterService {

    /**
     * @deprecated this method was used before successor / predecessor in
     *             reporItem mapping. use {@link #convert(ReportItem, int)}
     * @param ri
     * @return
     */
    @Deprecated
    @Transactional(readOnly = true)
    public ItemDto createOverdue(ReportItem ri) {
        int toBeProcessed = ri.overdue();
        if (toBeProcessed == 0) {
            return null;
        }
        return convert(ri, toBeProcessed);
    }

    @Transactional(readOnly = true)
    public ItemDto convert(ReportItem ri, int toBeProcessed) {
        ItemDto item = new ItemDto();
        item.setQuantity(toBeProcessed);
        item.setQuantityLeft(toBeProcessed);
        item.setDocumentNumber(ri.getReport().getDocumentNumber());
        if (ri.getReport() instanceof OrderConfirmation) {
            item.setOrderConfirmationNumber(ri.getReport().getDocumentNumber());
            OrderConfirmation orderConfirmation = (OrderConfirmation) ri.getReport();
            item.setAgreed(orderConfirmation.isAgreed());
            PurchaseAgreement pa = orderConfirmation.getPurchaseAgreement();
            if (pa != null) {
                item.setExpectedDelivery(pa.getExpectedDelivery());
            }
        }
        if (ri.getReport() instanceof Invoice) {
            item.setInvoiceNumber(ri.getReport().getDocumentNumber());
        }
        if (ri.getReport() instanceof DeliveryNotes) {
            item.setDeliveryNotesNumber(ri.getReport().getDocumentNumber());
        }
        if (ri.getReport() instanceof Receipt) {
            item.setReceiptNumber(ri.getReport().getDocumentNumber());
        }
        item.setCreated(ri.getCreated());
        Order order = ri.getOrderItem().getOrder();
        item.setCustomer(order.getCustomer().getId());
        item.setCustomerNumber(order.getCustomer().getCustomerNumber());
        item.setCustomerName(order.getCustomer().getCompanyName());
        item.setDocumentNumber(ri.getReport().getDocumentNumber());
        item.setId(ri.getId());
        item.setOrderNumber(order.getOrderNumber());
        if (ri.getOrderItem().getNegotiatedPriceNet() != null) {
            item.setPriceNet(ri.getOrderItem().getNegotiatedPriceNet().getValue());
        }
        item.setProduct(ri.getOrderItem().getProduct().getProductNumber());
        item.setProductType(ri.getOrderItem().getProduct().getProductType());
        item.setProductName(ri.getOrderItem().getProduct().getName());
        item.setStatus(ri.provideStatus());
        return item;
    }

}
