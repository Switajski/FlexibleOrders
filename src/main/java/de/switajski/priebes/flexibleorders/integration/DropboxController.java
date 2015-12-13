package de.switajski.priebes.flexibleorders.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.itextpdf.OrderToDtoConversionService;
import de.switajski.priebes.flexibleorders.itextpdf.PdfConfiguration;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDtoToPdfFileWriter;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.conversion.ReportToDtoConversionService;
import de.switajski.priebes.flexibleorders.web.ExceptionController;

@CrossOrigin
@Controller
@RequestMapping("/dropbox")
public class DropboxController extends ExceptionController {

    private static Logger log = Logger.getLogger(DropboxController.class);

    @Autowired
    ReportRepository reportRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    ReportToDtoConversionService reportToDtoConversionService;

    @Autowired
    OrderToDtoConversionService orderToDtoConversionService;

    @Autowired
    ReportDtoToPdfFileWriter reportDtoToPdfFileWriter;

    @Autowired
    PdfConfiguration config;

    @Autowired
    AccessTokenHolder accessTokenHolder;

    @RequestMapping(value = "/sendReport/{documentNumber}", method = RequestMethod.POST)
    public @ResponseBody JsonObjectResponse sendReport(@PathVariable("documentNumber") String documentNumber) throws Exception {

        ReportDto reportDto = retrieveReportDtoOrFail(documentNumber);

        String fileAndPathName = documentNumber + ".pdf";
        reportDtoToPdfFileWriter.writeFile(fileAndPathName, config.logo(), reportDto);

        String accessToken = accessTokenHolder.getAccessToken();
        accessToken = "47vdhzrlJREAAAAAAAAA7qJ5Yf-Yunbut3FIiJe_NIYjPLDTqgHbQ0CYEquN02Lt";
        uploadToDropbox(new File(fileAndPathName), fileAndPathName, accessToken);

        JsonObjectResponse jsonObjectResponse = new JsonObjectResponse();
        jsonObjectResponse.setSuccess(true);

        return jsonObjectResponse;
    }

    private ReportDto retrieveReportDtoOrFail(String documentNumber) {
        ReportDto reportDto = null;
        Report report = reportRepo.findByDocumentNumber(documentNumber);
        if (report != null) {
            reportDto = reportToDtoConversionService.convert(report);
        }
        else {
            Order order = orderRepo.findByOrderNumber(documentNumber);
            if (order == null) {
                throw new IllegalArgumentException("Konnte Dokument mit angegebener Dokumentennummer " + documentNumber + " nicht finden");
            }
            else {
                reportDto = orderToDtoConversionService.toDto(order);
            }
        }
        return reportDto;
    }

    private void uploadToDropbox(File file, String fileName, String accessToken) {
        String dropboxPath = "/FlexibleOrders";

        // Create a DbxClientV2, which is what you use to make API calls.
        String userLocale = Locale.getDefault().toString();
        DbxRequestConfig requestConfig = new DbxRequestConfig("FlexibleOrders", userLocale);

        DbxClient dbxClient = new DbxClient(requestConfig, accessToken, DbxHost.Default);

        // Make the API call to upload the file.
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                dbxClient.uploadFile(dropboxPath + "/" + fileName, DbxWriteMode.add(), file.length(), in);

            }
            finally {
                in.close();
            }
        }
        catch (DbxException ex) {
            throw new IllegalStateException("Error uploading to Dropbox: " + ex.getMessage());
        }
        catch (IOException ex) {
            throw new IllegalStateException("Error reading from file \"" + file.getName() + "\": " + ex.getMessage());
        }
    }

}
