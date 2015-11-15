package de.switajski.priebes.flexibleorders.web.itextpdf.shorthand;

import com.itextpdf.text.pdf.PdfPCell;

public class PdfPCellUtility {

	public static PdfPCell noBorder(PdfPCell cell){
		cell.setBorder(0);
		return cell;
	}
	
	public static PdfPCell noBorder(){
		PdfPCell cell = new PdfPCell();
		noBorder(cell);
		return cell;
	}
}
