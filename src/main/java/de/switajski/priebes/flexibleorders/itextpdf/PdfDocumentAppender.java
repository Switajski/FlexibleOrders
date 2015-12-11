package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

public interface PdfDocumentAppender {

    public void append(Document document) throws DocumentException;

}
