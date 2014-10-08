package de.switajski.priebes.flexibleorders.web;

import java.util.ArrayList;
import java.util.List;

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
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderConfirmationRepository;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;
import de.switajski.priebes.flexibleorders.service.api.OrderService;
import de.switajski.priebes.flexibleorders.service.conversion.DeliveryNotesToDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.InvoiceToDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.OrderConfirmationToDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.OrderToDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportToDtoConversionService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.CreditNotePdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.DeliveryNotesPdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.InvoicePdfView;
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
    ReportRepository reportRepo;
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    ReportItemServiceImpl itemService;
    @Autowired
    CustomerRepository customerService;
    @Autowired
    OrderService orderService;
    @Autowired 
    ReportToDtoConversionService reportDtoConversionService;
    @Autowired 
    ReportToDtoConversionService creditNoteDtoConversionService;
    @Autowired 
    InvoiceToDtoConversionService invoiceDtoConversionService;
    @Autowired 
    DeliveryNotesToDtoConversionService deliveryNotesDtoConversionService;
    @Autowired 
    OrderConfirmationToDtoConversionService orderConfirmationDtoConversionService;
	@Autowired
    OrderToDtoConversionService orderDtoConversionService;
	@Autowired
	OrderConfirmationRepository orderConfirmationRepo;
    

    @RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showReportPdf(@PathVariable("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");
        Report report = reportRepo.findByDocumentNumber(id);
        if (report != null){
        	return createReportSpecificModelAndView(report);
        } else if (orderConfirmationRepo.findByOrderAgreementNumber(id) != null){
            return createReportSpecificModelAndView(orderConfirmationRepo.findByOrderAgreementNumber(id));
        } else {
        	Order order = orderRepo.findByOrderNumber(id);
        	if (order != null)
        		return new ModelAndView(OrderPdfView.class.getSimpleName(),
                        ReportDto.class.getSimpleName(), orderDtoConversionService.toDto(order));
        }
        throw new IllegalArgumentException("Report or order with given id not found");
    }

    private ModelAndView createReportSpecificModelAndView(Report report) {
    	String model = ReportDto.class.getSimpleName();
        if (report instanceof OrderConfirmation) {
            return new ModelAndView(OrderConfirmationPdfView.class.getSimpleName(),
                    model, orderConfirmationDtoConversionService.toDto((OrderConfirmation) report));
        }
        if (report instanceof DeliveryNotes) {
            return new ModelAndView(DeliveryNotesPdfView.class.getSimpleName(),
                    model, deliveryNotesDtoConversionService.toDto((DeliveryNotes) report));
        }
        if (report instanceof Invoice) {
            return new ModelAndView(InvoicePdfView.class.getSimpleName(),
                    model, invoiceDtoConversionService.toDto((Invoice) report));
        }
        if (report instanceof CreditNote) {
            return new ModelAndView(CreditNotePdfView.class.getSimpleName(),
                    model, creditNoteDtoConversionService.toDto(report));
        }
        throw new IllegalStateException(
                "Could not find view handler for given Document");
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
