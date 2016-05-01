package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Image;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdfToPdfFileWriter;

public class ItextPdfTestHelper {

    public static String pdfPath = "src/test/resources/";

    public void createPdfFile(String fileName, ReportInPdf reportDto) throws Exception {
        Image logo = Image.getInstance("src/main/resources/images/LogoGross.jpg");
        ReportInPdfToPdfFileWriter fileWriter = new ReportInPdfToPdfFileWriter();

        fileWriter.writeFile(ItextPdfTestHelper.pdfPath + fileName, logo, reportDto);
    }

}
