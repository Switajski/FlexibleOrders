package de.switajski.priebes.flexibleorders.itextpdf.builder;

import java.math.BigDecimal;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

public class InvoiceCalculation {
    private Amount netGoods, discountAmount, shipping, net, vat, gross;
    private String paymentConditions, discountText;

    public InvoiceCalculation(ReportDto report){
        netGoods = report.netGoods;
        
        if (report.shippingSpecific_shippingCosts != null)
            shipping = report.shippingSpecific_shippingCosts;
        
        discountText = null;
        discountAmount = null;
        if (report.invoiceSpecific_discountRate != null){
            discountAmount = calculateDiscount(report.netGoods, report.invoiceSpecific_discountRate);
        if (report.invoiceSpecific_discountText != null)
            discountText = report.invoiceSpecific_discountText;
        }
        
        net = report.netGoods;
        if (discountAmount != null)
            net = net.add(discountAmount.negate());
        if (shipping != null){
            net = net.add(shipping);
        }
        vat = net.multiply(report.vatRate);
        gross = net.add(vat);
    }
    
    private Amount calculateDiscount(Amount netGoods, BigDecimal discountPercentage) {
        BigDecimal discountRate = discountPercentage.divide(new BigDecimal(100d));
        return netGoods.multiply(discountRate);
    }

    public Amount getNet() {
        return net;
    }

    public Amount getVat() {
        return vat;
    }

    public Amount getShipping() {
        return shipping;
    }

    public Amount getGross() {
        return gross;
    }

    public Amount getDiscountAmount() {
        return discountAmount;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public String getDiscountText() {
        return discountText;
    }

    public Amount getNetGoods() {
        return netGoods;
    }
}