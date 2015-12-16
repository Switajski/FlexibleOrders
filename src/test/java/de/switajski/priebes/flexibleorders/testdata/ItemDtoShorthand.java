package de.switajski.priebes.flexibleorders.testdata;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ItemDtoShorthand {

    public static ItemDto item(CatalogProduct product, int qty) {
        ItemDto item = new ItemDto();
        item.setPriceNet(product.getRecommendedPriceNet().getValue());
        item.setProduct(product.toProduct().getProductNumber());
        item.setProductName(product.getName());
        item.setQuantity(qty);
        return item;
    }

    public static ItemDto item(CatalogProduct product, int qty, String reportNo) {
        ItemDto item = item(product, qty);
        // TODO: qtyLeft is really nonsense
        item.setQuantityLeft(qty);
        if (reportNo.startsWith("B")) item.setOrderNumber(reportNo);
        else if (reportNo.startsWith("AB")) item.setOrderConfirmationNumber(reportNo);
        else if (reportNo.startsWith("L")) item.setDeliveryNotesNumber(reportNo);
        else if (reportNo.startsWith("R")) item.setInvoiceNumber(reportNo);
        return item;
    }

}
