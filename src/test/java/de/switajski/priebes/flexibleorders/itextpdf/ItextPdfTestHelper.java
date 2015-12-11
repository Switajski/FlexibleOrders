package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Image;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDtoToPdfFileWriter;

public class ItextPdfTestHelper {

    public static String pdfPath = "src/test/resources/";

    public void createPdfFile(String fileName, ReportDto reportDto) throws Exception {
        Image logo = Image.getInstance("src/main/resources/images/LogoGross.jpg");
        ReportDtoToPdfFileWriter fileWriter = new ReportDtoToPdfFileWriter();

        fileWriter.writeFile(ItextPdfTestHelper.pdfPath + fileName, logo, reportDto);
    }

}
