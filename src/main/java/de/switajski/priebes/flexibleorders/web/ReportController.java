package de.switajski.priebes.flexibleorders.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.itextpdf.OrderToDtoConversionService;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderConfirmationRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ReportingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportToDtoConversionService;

/**
 * Controller for handling http requests on reports. E.g. PDFs
 *
 * @author Marek Switajski
 *
 */
@CrossOrigin
@Controller
public class ReportController {

    private static Logger log = Logger.getLogger(ReportController.class);
    @Autowired
    ReportRepository reportRepo;
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    ReportingService reportingService;
    @Autowired
    CustomerRepository customerService;
    @Autowired
    TransitionsService orderService;
    @Autowired
    ReportToDtoConversionService report2DtoConversionService;
    @Autowired
    OrderToDtoConversionService orderDtoConversionService;
    @Autowired
    OrderConfirmationRepository orderConfirmationRepo;
    @Autowired
    CustomerRepository customerRepo;

    @RequestMapping(value = "/reports/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showReportPdf(@PathVariable("id") String id) throws ContradictoryAddressException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");
        Report report = reportRepo.findByDocumentNumber(id);
        if (report != null) {
            return createReportSpecificModelAndView(report);
        }
        else if (orderConfirmationRepo.findByOrderAgreementNumber(id) != null) {
            return createReportSpecificModelAndView(orderConfirmationRepo.findByOrderAgreementNumber(id));
        }
        else {
            Order order = orderRepo.findByOrderNumber(id);
            if (order != null) return new ModelAndView(
                    PdfView.class.getSimpleName(),
                    ReportInPdf.class.getSimpleName(),
                    orderDtoConversionService.toDto(order));
        }
        throw new IllegalArgumentException("Report or order with given id not found");
    }

    @RequestMapping(value = "/reports/{id}.html", produces = "text/html")
    public @ResponseBody String showReportWrappedInHtml(
            @PathVariable("id") String id,
            HttpServletRequest request) {
        String link = request.getRequestURL().toString().replace("html", "pdf");
        return new StringBuilder()
                .append("<html><body>")
                .append("<object type=\"application/pdf\" ")
                .append("data=\"")
                .append(link)
                .append("\" ")
                .append("width=\"100%\" height=\"100%\" >")
                .append("<embed type=\"application/pdf\" src=\"" + link + "\" />")
                .append("</object></body></html>")
                .toString();
    }

    @RequestMapping(value = "/kunden/{customerNumber}/ausstehendeArtikel.pdf", headers = "Accept=application/pdf")
    public ModelAndView showToBeShippedByCustomerPdf(@PathVariable("customerNumber") Long customerNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");
        Customer customer = customerRepo.findByCustomerNumber(customerNumber);
        if (customer == null) {
            throw new IllegalArgumentException("Kunden mit Kundennr.: " + customerNumber + " nicht gefunden");
        }
        ReportInPdf report = reportingService.toBeShippedToCustomer(customer);
        return new ModelAndView(
                PdfView.class.getSimpleName(),
                ReportInPdf.class.getSimpleName(),
                report);
    }

    @RequestMapping(value = "/ausstehendeArtikel.pdf", headers = "Accept=application/pdf")
    public ModelAndView showAllToBeShippedPdf() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");

        ReportInPdf report = reportingService.toBeShipped();
        return new ModelAndView(
                PdfView.class.getSimpleName(),
                ReportInPdf.class.getSimpleName(),
                report);
    }

    /**
     * TODO: dynamic method overloading is not supported by Java. Find
     * alternative. (http://skeletoncoder
     * .blogspot.de/2006/09/java-tutorials-overloading-is-compile.html)
     * 
     * @param report
     * @return
     * @throws ContradictoryAddressException
     */
    private ModelAndView createReportSpecificModelAndView(Report report) throws ContradictoryAddressException {
        String model = ReportInPdf.class.getSimpleName();
        ReportInPdf reportDto = report2DtoConversionService.convert(report);

        if (reportDto == null) throw new IllegalStateException(
                "Could not find view handler for given Document");

        return new ModelAndView(
                PdfView.class.getSimpleName(),
                model,
                reportDto);

    }

    public class invoiceNumber {

        public long invoiceNumber;

        public invoiceNumber(long invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listAll(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts) {
        JsonObjectResponse response = new JsonObjectResponse();
        Page<Order> customer = orderRepo.findAll(new PageRequest(
                page - 1,
                limit));
        response.setData(customer.getContent());
        response.setTotal(customer.getTotalElements());
        response.setMessage("All entities retrieved.");
        response.setSuccess(true);

        return response;
    }

    @RequestMapping(value = "/orders/listOrderNumbers", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listOrderNumbers(
            @RequestParam(value = "orderNumber", required = false) String orderNumber,
            @RequestParam(value = "customerId", required = false) Long customerId)
                    throws Exception {
        // FIXME: find by customer and orderNumber
        log.debug("listOrderNumbers request:" + orderNumber);
        JsonObjectResponse response = new JsonObjectResponse();

        if (orderNumber != null) {
            List<String> orderNumbers = reportingService
                    .orderNumbersLike(orderNumber);
            if (!orderNumbers.isEmpty()) {
                response.setTotal(orderNumbers.size());
                response.setData(formatOrderNumbers(orderNumbers));
            }
            else {
                response.setTotal(0l);
            }
        }
        else { // orderNumber == null
            List<Customer> customers = customerService.findAll();
            ArrayList<String> list = new ArrayList<String>();
            for (Customer customer : customers)
                list.addAll(reportingService.orderNumbersByCustomer(
                        customer,
                        new PageRequest(0, 20)).getContent());
            response.setTotal(list.size());
            response.setData(formatOrderNumbers(list));
        }

        response.setMessage("All entities retrieved.");
        response.setSuccess(true);

        return response;
    }

    private orderNumber[] formatOrderNumbers(List<String> orderNumbers) {
        List<orderNumber> entities = new ArrayList<orderNumber>();
        for (String on : orderNumbers)
            entities.add(new orderNumber(on));
        orderNumber[] orderNumberArray = entities.toArray(new orderNumber[0]);
        return orderNumberArray;
    }

    public class orderNumber {

        public String orderNumber;

        public orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

    }
}
