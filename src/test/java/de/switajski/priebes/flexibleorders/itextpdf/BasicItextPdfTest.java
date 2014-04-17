package de.switajski.priebes.flexibleorders.itextpdf;

/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

/**
 * First iText example: Hello World.
 */
public class BasicItextPdfTest {

	/** Path to the resulting PDF file. */
	public static final String RESULT = "src/test/resources/priebes.pdf";

	public static final String LOGO = "src/main/webapp/images/Logo.jpg";

	public static final String FONT = "Arial";

	/**
	 * Creates a PDF file: hello.pdf
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args)
			throws DocumentException, IOException {
		new BasicItextPdfTest().createPdf(RESULT);
	}

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createPdf(String filename)
			throws DocumentException, IOException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		// step 3
		document.open();
		// step 4
		insertLogo(document);
		addAddress(document, null);
		insertAdresse(document, null);
		insertTitle(document, "Rechnung/Lieferschein");
		insertBestellung(document);
		// step 5
		document.close();
	}

	private void addAddress(Document doc, String addressTo)
			throws DocumentException {
		Paragraph addresse = new Paragraph("priebes OHG");
		addresse.setFont(FontFactory.getFont("Arial", 10, Font.NORMAL));
		addresse.setAlignment(Element.ALIGN_RIGHT);
		addresse.add(Chunk.NEWLINE);
		addresse.add("Maxstrasse 1");
		addresse.add(Chunk.NEWLINE);
		addresse.add("71636 Ludwigsburg");
		addresse.add(Chunk.NEWLINE);
		addresse.add("http://www.priebes.eu");
		addresse.add(Chunk.NEWLINE);

		doc.add(addresse);
	}

	private void insertLogo(Document document) throws BadElementException,
			MalformedURLException, DocumentException, IOException {
		Image img = Image.getInstance(LOGO);
		img.setAlignment(Image.RIGHT);
		img.scaleToFit(180, 75);
		document.add(img);
	}

	private void insertAdresse(Document doc, String address)
			throws MalformedURLException, IOException, DocumentException {
		Image img = Image.getInstance(LOGO);
		// img.setAlignment(Image.LEFT);
		img.scaleToFit(70, 35);
		img.setAbsolutePosition(50, 700);
		Chunk tab1 = new Chunk(new VerticalPositionMark(), 90, true);
		Paragraph p = new Paragraph();
		p.add(tab1);
		p.setFont(FontFactory.getFont(FONT, 10, Font.NORMAL));
		p.add("Maxstrasse1, 71636 Ludwigsburg");

		if (address == null) {
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
		}

		doc.add(p);
		doc.add(img);
	}

	private void insertTitle(Document doc, String title)
			throws MalformedURLException, IOException, DocumentException {

		Paragraph p = new Paragraph();

		p.setFont(FontFactory.getFont(FONT, 16, Font.BOLD));
		p.add(Chunk.NEWLINE);
		p.add(Chunk.NEWLINE);
		p.add(Chunk.NEWLINE);

		p.add(title);
		doc.add(p);
		p.add(Chunk.NEWLINE);
		p.add(Chunk.NEWLINE);
	}

	private void insertBestellung(Document doc) throws DocumentException {

		PdfPTable table = new PdfPTable(6);

		HashSet<PdfPCell> header = new HashSet<PdfPCell>();
		header.add(new PdfPCell(new Phrase("Bestellpos.")));
		header.add(new PdfPCell(new Phrase("Artikelnr.")));
		header.add(new PdfPCell(new Phrase("Artikel")));
		header.add(new PdfPCell(new Phrase("Stueck")));
		header.add(new PdfPCell(new Phrase("Preis")));
		header.add(new PdfPCell(new Phrase("Betrag")));
		for (PdfPCell cell : header) {
			cell.setBorder(Rectangle.BOTTOM);
			table.addCell(cell);
		}

		HashSet<PdfPCell> cells = new HashSet<PdfPCell>();
		cells.add(new PdfPCell(new Phrase("1")));
		cells.add(new PdfPCell(new Phrase("10099")));
		cells.add(new PdfPCell(new Phrase("Lutz dots")));
		cells.add(new PdfPCell(new Phrase("2")));
		cells.add(new PdfPCell(new Phrase("6.06")));
		cells.add(new PdfPCell(new Phrase("12.12")));
		for (PdfPCell cell : cells) {
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
		}

		doc.add(table);
	}

}