package de.switajski.priebes.flexibleorders.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.CreditNote;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.service.process.OrderService;
import de.switajski.priebes.flexibleorders.web.itextpdf.CreditNotePdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.DeliveryNotesPdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.InvoicePdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderAgreementPdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderConfirmationPdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderPdfView;

/**
 * Controller for handling http requests on reports. E.g. PDFs
 * 
 * @author Marek Switajski
 * 
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    private static Logger log = Logger.getLogger(ReportController.class);
    @Autowired
    OrderItemRepository invoiceItemService;
    @Autowired
    ReportRepository reportRepo;
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    ReportItemServiceImpl itemService;
    @Autowired
    CustomerRepository customerService;
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showReportPdf(@PathVariable("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");
        Report report = reportRepo.findByDocumentNumber(id);
        if (report == null) throw new IllegalArgumentException("Report with given id not found");
        return createReportSpecificModelAndView(report);
    }

    private ModelAndView createReportSpecificModelAndView(Report report) {
        ModelToView modelToView = new ModelToView();
        if (report instanceof OrderConfirmation) {
            String model = OrderConfirmation.class.getSimpleName();
            return new ModelAndView(modelToView.getView(model),
                    model, (OrderConfirmation) report);
        }
        if (report instanceof OrderAgreement) {
            String model = OrderAgreement.class.getSimpleName();
            return new ModelAndView(modelToView.getView(model),
                    model, (OrderAgreement) report);
        }
        if (report instanceof DeliveryNotes) {
            String model = DeliveryNotes.class.getSimpleName();
            return new ModelAndView(modelToView.getView(model),
                    model, (DeliveryNotes) report);
        }
        if (report instanceof Invoice) {
            String model = Invoice.class.getSimpleName();
            return new ModelAndView(modelToView.getView(model),
                    model, (Invoice) report);
        }
        if (report instanceof CreditNote) {
            String model = CreditNote.class.getSimpleName();
            return new ModelAndView(modelToView.getView(model),
                    model, (CreditNote) report);
        }
        throw new IllegalStateException(
                "Could not find view handler for given Document");
    }

    private class ModelToView {
        Map<String, String> modelToView = new HashMap<String, String>();

        ModelToView() {
            modelToView.put(
                    OrderConfirmation.class.getSimpleName(),
                    OrderConfirmationPdfView.class.getSimpleName());
            modelToView.put(
                    OrderAgreement.class.getSimpleName(),
                    OrderAgreementPdfView.class.getSimpleName());
            modelToView.put(
                    DeliveryNotes.class.getSimpleName(),
                    DeliveryNotesPdfView.class.getSimpleName());
            modelToView.put(
                    Invoice.class.getSimpleName(),
                    InvoicePdfView.class.getSimpleName());
            modelToView.put(
                    CreditNote.class.getSimpleName(),
                    CreditNotePdfView.class.getSimpleName());
        }

        String getView(String model) {
            String view = modelToView.get(model);
            if (view == null) throw new IllegalStateException(
                    "There is no view for given model defined");
            return view;
        }

    }

    @RequestMapping(value = "/orders/{orderNumber}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showOrderPdf(
            @PathVariable("orderNumber") String orderNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");
        Order record = orderService.retrieveOrder(orderNumber);
        if (record == null) throw new IllegalArgumentException(
                "Bestellung mit gegebener Bestellnr. nicht gefunden");
        return new ModelAndView(OrderPdfView.class.getSimpleName(),
                Order.class.getSimpleName(), record);
    }

    @RequestMapping(value = "/listDocumentNumbersLike", method = RequestMethod.GET)
    public @ResponseBody
    JsonObjectResponse listInvoiceNumbers(
            @RequestParam(value = "query", required = false) Long orderNumberObject)
            throws Exception {

        log.debug("listOrderNumbers request:" + orderNumberObject);
        // TODO unify docNumbers
        throw new NotImplementedException();
    }

    public class invoiceNumber {

        public long invoiceNumber;

        public invoiceNumber(long invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public @ResponseBody
    JsonObjectResponse listAll(
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
    public @ResponseBody
    JsonObjectResponse listOrderNumbers(
            @RequestParam(value = "orderNumber", required = false) String orderNumber,
            @RequestParam(value = "customerId", required = false) Long customerId)
            throws Exception {
        // FIXME: find by customer and orderNumber
        log.debug("listOrderNumbers request:" + orderNumber);
        JsonObjectResponse response = new JsonObjectResponse();

        if (orderNumber != null) {
            List<String> orderNumbers = itemService
                    .retrieveOrderNumbersLike(orderNumber);
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
                list.addAll(itemService.retrieveOrderNumbersByCustomer(
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
