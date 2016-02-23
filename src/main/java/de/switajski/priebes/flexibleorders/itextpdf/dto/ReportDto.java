package de.switajski.priebes.flexibleorders.itextpdf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ReportDto {

    public String documentNumber;

    public Date created;

    /**
     * @deprecated due to uncecessary dependency to ReportItem. use
     *             {@link #itemDtos} instead.
     */
    public Set<ReportItem> items = new HashSet<ReportItem>();

    public Address address;

    public List<ReportItemInPdf> itemDtos = new ArrayList<ReportItemInPdf>();

    public Long customerNumber;

    public Amount netGoods;

    public Double vatRate;

    public String customerFirstName, customerLastName, customerEmail,
            customerPhone;

    public Set<String> related_orderNumbers, related_orderConfirmationNumbers,
            related_invoiceNumbers, related_orderAgreementNumbers,
            related_deliveryNotesNumbers,
            related_creditNoteNumbers = new HashSet<String>();

    public String customerSpecific_vendorNumber, customerSpecific_vatIdNo,
            customerSpecific_saleRepresentative, customerSpecific_mark;
    public ContactInformation customerSpecific_contactInformation;

    // order agreement specific
    public String orderConfirmationNumber;
    public String orderConfirmationSpecific_paymentConditions;
    public Date orderConfirmationSpecific_oldestOrderDate;

    public String shippingSpecific_trackNumber, shippingSpecific_packageNumber;
    public Amount shippingSpecific_shippingCosts;
    public LocalDate shippingSpecific_expectedDelivery;
    public DeliveryMethod shippingSpecific_deliveryMethod;
    public boolean shippingSpecific_expectedDeliveryDateDeviates;
    public Address shippingSpecific_shippingAddress;

    // invoice specific
    public boolean invoiceSpecific_hasItemsWithDifferentCreationDates;
    public String invoiceSpecific_billing;
    /**
     * @deprecated report dto needs only to have one address - use
     *             {@link #address} attribute instead
     */
    public BigDecimal invoiceSpecific_discountRate;
    public String invoiceSpecific_discountText;

    // order specific TODO: merge to ItemDto
    public Set<OrderItem> orderItems;

    public boolean showPricesInDeliveryNotes;

    /**
     * @deprecated because of uneccessary dependencies to ReportItem use
     *             {@link #getReportItemsInPdfByOrder()} instead
     * @return
     */
    public List<ReportItem> getItemsByOrder() {
        List<ReportItem> ris = new ArrayList<ReportItem>(items);
        Collections.sort(ris, new Comparator<ReportItem>() {

            @Override
            public int compare(ReportItem r1, ReportItem r2) {
                Product p1 = r1.getOrderItem().getProduct();
                Product p2 = r2.getOrderItem().getProduct();
                if (p1.hasProductNo() && p2.hasProductNo()) return p1.getProductNumber().compareTo(
                        p2.getProductNumber());
                else if (!p1.hasProductNo() && !p2.hasProductNo()) {
                    if (p1.getName() != null && p2.getName() != null) return p1.getName().compareTo(p2.getName());
                    else return 0;
                }
                else if (p1.hasProductNo()) {
                    return -1;
                }
                else if (p2.hasProductNo()) {
                    return 1;
                }

                else return 0;

            }

        });
        return Collections.unmodifiableList(ris);
    }

    public List<ReportItemInPdf> getReportItemsInPdfByOrder() {
        List<ReportItemInPdf> ris = new ArrayList<ReportItemInPdf>(itemDtos);
        Collections.sort(ris, new Comparator<ReportItemInPdf>() {

            @Override
            public int compare(ReportItemInPdf arg0, ReportItemInPdf arg1) {
                if (!arg0.customerNumber.equals(arg1.customerNumber)) {
                    return arg0.customerNumber.compareTo(arg1.customerNumber);
                }
                String productName1 = arg0.productName;
                String productName2 = arg1.productName;
                if (hasProductNo(arg0) && hasProductNo(arg1)) return productName1.compareTo(productName2);
                else if (!hasProductNo(arg0) && !hasProductNo(arg1)) {
                    if (productName1 != null && productName2 != null) return productName1.compareTo(productName2);
                    else return 0;
                }
                else if (hasProductNo(arg0)) {
                    return -1;
                }
                else if (hasProductNo(arg1)) {
                    return 1;
                }

                else return 0;
            }

            private boolean hasProductNo(ReportItemInPdf arg0) {
                return !StringUtils.isEmpty(arg0.productName);
            }

        });

        return Collections.unmodifiableList(ris);
    }

    /**
     * @deprecated because of uneccessary dependencies use
     *             {@link #getReportItemsInPdfByOrder()} instead
     * @return
     */
    public List<OrderItem> getOrderItemsByOrder() {
        List<OrderItem> ris = new ArrayList<OrderItem>(orderItems);
        Collections.sort(ris, new Comparator<OrderItem>() {

            @Override
            public int compare(OrderItem r1, OrderItem r2) {
                Product p1 = r1.getProduct();
                Product p2 = r2.getProduct();
                if (p1.hasProductNo() && p2.hasProductNo()) return p1.getProductNumber().compareTo(
                        p2.getProductNumber());
                else if (!p1.hasProductNo() && !p2.hasProductNo()) {
                    return p1.getName().compareTo(p2.getName());
                }
                else if (p1.hasProductNo()) {
                    return -1;
                }
                else if (p2.hasProductNo()) {
                    return 1;
                }

                else return 0;

            }

        });
        return ris;
    }

    public boolean isShowExtendedInformation() {
        return !(customerSpecific_vendorNumber == null
                && customerSpecific_vatIdNo == null
                && customerSpecific_saleRepresentative == null
                && customerSpecific_mark == null && customerSpecific_contactInformation == null);
    }
}
