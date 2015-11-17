package de.switajski.priebes.flexibleorders.testdata;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ItemDtoShorthand {

    public static ItemDto item(CatalogProduct product, int qty) {
        ItemDto item = new ItemDto();
        item.priceNet = product.getRecommendedPriceNet().getValue();
        item.product = product.toProduct().getProductNumber();
        item.productName = product.getName();
        item.quantity = qty;
        return item;
    }

    public static ItemDto item(CatalogProduct product, int qty, String reportNo) {
        ItemDto item = item(product, qty);
        // TODO: qtyLeft is really nonsense
        item.quantityLeft = qty;
        if (reportNo.startsWith("B")) item.orderNumber = reportNo;
        else if (reportNo.startsWith("AB")) item.orderConfirmationNumber = reportNo;
        else if (reportNo.startsWith("L")) item.deliveryNotesNumber = reportNo;
        else if (reportNo.startsWith("R")) item.invoiceNumber = reportNo;
        return item;
    }

}
