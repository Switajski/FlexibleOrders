package de.switajski.priebes.flexibleorders.itextpdf.dto;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ReportItemInPdf {

    public String sku, productName, orderNumber, customerNumber, companyName, additionalInfo;

    public int quantity;

    public Address shippingAddress;

    public ReportItemInPdf(ReportItem ri) {
        OrderItem orderItem = ri.getOrderItem();
        orderNumber = orderItem.getOrder().getOrderNumber();
        customerNumber = orderItem.getOrder().getCustomer().getCustomerNumber().toString();
        companyName = orderItem.getOrder().getCustomer().getCompanyName();
        quantity = ri.getQuantity();
        productName = orderItem.getProduct().getName();
        additionalInfo = orderItem.getAdditionalInfo();
        if (ri.getReport() instanceof OrderConfirmation) {
            shippingAddress = ((OrderConfirmation) ri.getReport()).getPurchaseAgreement().getShippingAddress();
        }
        sku = orderItem.getProduct().getProductNumber();
    }

}
