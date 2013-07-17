package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class OrderPdfFile extends OrderPdfView {

	public static final String RESULT
    = "src/test/java/de/switajski/priebes/flexibleorders/report/itextpdf/OrderPdfFileTest.pdf";
	
	
	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
		PdfWriter writer;
			try {
				writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
				return writer;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
	}

}
