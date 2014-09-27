package de.switajski.priebes.flexibleorders.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.BusinessInputException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ReportingService;

@Service
public class AgreementService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportingService reportingService;

    @Transactional
    public OrderAgreement agree(String orderConfirmationNo, String orderAgreementNo){
        OrderConfirmation oc = reportingService.retrieveOrderConfirmation(orderConfirmationNo);
        if (oc == null)
            throw new BusinessInputException("Auftragsbest"+Unicode.aUml+"tigung mit angegebener Nummer nicht gefunden");

        OrderAgreement oa = new OrderAgreement();
        oa.setDocumentNumber(orderAgreementNo);
        oa.setAgreementDetails(oc.getPurchaseAgreement());
        oa.setCustomerDetails(oc.getCustomerDetails());
        oa.setOrderConfirmationNumber(orderConfirmationNo);
        oa = takeOverConfirmationItems(oc, oa);
        
        return reportRepo.save(oa);
    }

    private OrderAgreement takeOverConfirmationItems(OrderConfirmation oc,
            OrderAgreement oa) {
        for (ReportItem ri:oc.getItems()){
            AgreementItem ai = new AgreementItem();
            int calculateLeft = QuantityCalculator.calculateLeft(ri);
            if (calculateLeft < 1)
                throw new BusinessInputException("Eine angegebene position hat keine offenen Positionen mehr");
            ai.setQuantity(calculateLeft);
            ai.setOrderItem(ri.getOrderItem());
            //TODO: bidirectional management of relationship
            ai.setReport(oa);
            oa.addItem(ai);
        }
        return oa;
    }
}
