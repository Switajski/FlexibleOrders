package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class OrderConfirmationPdfFile extends OrderConfirmationPdfView {

	public String filePathAndName;

	protected PdfWriter newWriter(Document document, OutputStream os)
			throws DocumentException {
		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(
					filePathAndName));
			return writer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFilePathAndName() {
		return filePathAndName;
	}

	public void setFilePathAndName(String filePathAndName) {
		this.filePathAndName = filePathAndName;
	}

}
