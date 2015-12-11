package de.switajski.priebes.flexibleorders.itextpdf;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportItemInPdf;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ContactInformationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingItemBuilder;

public class ReportDtoTestFixture {

    private static final String O_NR = "3465897";
    private static final Customer customer = new CustomerBuilder().yvonne().build();

    public static ReportDto amendTestData(ReportDto r) {
        DeliveryMethod deliveryMethod = new DeliveryMethod();
        deliveryMethod.setAddress(AddressBuilder.createDefault());
        deliveryMethod.setName("DEUTSCHE POST");
        deliveryMethod.setPhone("+4940786876");

        r.invoiceSpecific_headerAddress = customer.getInvoiceAddress();
        r.shippingSpecific_shippingAddress = customer.getShippingAddress();
        r.shippingSpecific_deliveryMethod = deliveryMethod;
        r.shippingSpecific_shippingCosts = new Amount(BigDecimal.valueOf(15.5d), Currency.EUR);

        r.documentNumber = O_NR;
        r.created = new Date();
        r.customerEmail = customer.getEmail();
        r.customerFirstName = customer.getFirstName();
        r.customerLastName = customer.getLastName();
        r.customerNumber = customer.getCustomerNumber();
        r.customerPhone = customer.getPhone();
        r.customerSpecific_contactInformation = new ContactInformationBuilder()
                .setContact1("Hr. Priebe")
                .setContact2("Mobil: 0175 / 124312541")
                .setContact3("Fax: 0175 / 12431241")
                .setContact4("Email: info@priebe.eu")
                .build();
        r.customerSpecific_mark = "Filiale";
        r.orderConfirmationSpecific_paymentConditions = "So schnell wie m" + Unicode.O_UML + "glich, ohne Prozente sonst Inkasso Moskau";
        r.customerSpecific_vatIdNo = "ATU-No.111234515";
        r.customerSpecific_vendorNumber = "PRIEBES-1";
        r.customerSpecific_saleRepresentative = "Herr Vertreter1";
        r.netGoods = new Amount(BigDecimal.valueOf(786d), Currency.EUR);
        r.vatRate = 0.4d;

        r.related_orderNumbers = new HashSet<String>(Arrays.asList("B12", "B13"));
        r.related_orderConfirmationNumbers = new HashSet<String>(Arrays.asList("AB11", "AB13"));
        r.related_deliveryNotesNumbers = new HashSet<String>(Arrays.asList("L11", "L13"));
        r.related_invoiceNumbers = new HashSet<String>(Arrays.asList("R11", "R13"));

        r.items = new HashSet<ReportItem>();
        for (int i = 0; i < 57; i++) {
            new OrderItemBuilder();
            Amount price = new Amount(BigDecimal.valueOf(14.5d), Currency.EUR);
            Amount priceNegotiated = new Amount(BigDecimal.valueOf(12.5d), Currency.EUR);
            OrderItem orderItem = givenOrderItem(i, price, priceNegotiated);
            ShippingItem shippingItem = new ShippingItemBuilder()
                    .setItem(orderItem)
                    .setQuantity(i + 1)
                    .setReport(new DeliveryNotesBuilder().build())
                    .setPredecessor(new ConfirmationItemBuilder()
                            .setReport(new OrderConfirmationBuilder().setDocumentNumber("Vorgaenger Doknr").build())
                            .setItem(orderItem)
                            .setQuantity(2)
                            .build())
                    .build();
            r.items.add(shippingItem);
            r.itemDtos.add(new ReportItemInPdf(shippingItem));
        }

        return r;
    }

    private static OrderItem givenOrderItem(int i, Amount price, Amount priceNegotiated) {
        return new OrderItemBuilder()
                .setNegotiatedPriceNet(price)
                .setOrderedQuantity(i)
                .setOrder(new OrderBuilder()
                        .setCustomer(customer)
                        .setOrderNumber("AB151231001-2")
                        .build())
                .setProduct(
                        new CatalogProductBuilder("some productname ", "65187", ProductType.PRODUCT)
                                .setRecommendedPriceNet(priceNegotiated)
                                .build()
                                .toProduct())
                .build();
    }
}
