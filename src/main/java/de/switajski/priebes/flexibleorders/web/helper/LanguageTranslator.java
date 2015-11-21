package de.switajski.priebes.flexibleorders.web.helper;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class LanguageTranslator {

    public static String translate(ReportItem obj) {
        if (obj instanceof ConfirmationItem) return "AB-Position";
        else if (obj instanceof InvoiceItem) return "Rechnungsposition";
        else if (obj instanceof ShippingItem) return "Lieferscheinposition";
        else if (obj instanceof ReceiptItem) return "Quittungsposition";
        else return (obj.getClass().getSimpleName());
    }

}
