package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;

public class ConfirmationItemBuilder extends ReportItemBuilder<ConfirmationItem, Builder<ConfirmationItem>> {

    @Override
    public ConfirmationItem build() {
        ConfirmationItem ii = new ConfirmationItem();
        super.build(ii);
        return ii;
    }

    public static ConfirmationItem createConfirmationItemWithAddress(int i) {
        return createConfirmationItemWithAddress(i, new Date());
    }

    // TODO:refactor to usage of entity builder
    public static ConfirmationItem createConfirmationItemWithAddress(int i, Date date) {
        OrderConfirmation orderConfirmation = new OrderConfirmation();
        PurchaseAgreement purchaseAgreement = new PurchaseAgreement();
        purchaseAgreement.setShippingAddress(AddressBuilder.buildWithGeneratedAttributes(i));
        orderConfirmation.setPurchaseAgreement(purchaseAgreement);
        ConfirmationItem confirmationItem = new ConfirmationItem();
        orderConfirmation.addItem(confirmationItem);
        return confirmationItem;
    }

    public static ConfirmationItem create(Date date) {
        return createConfirmationItemWithAddress(1, date);
    }

}
