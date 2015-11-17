package de.switajski.priebes.flexibleorders.itextpdf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DeliveryNotesPdfFileTest.class,
        InvoicePdfFileTest.class,
        OrderConfirmationPdfFileTest.class,
        OrderPdfFileTest.class
})
public class PdfFilesUnitTestSuite {

}
