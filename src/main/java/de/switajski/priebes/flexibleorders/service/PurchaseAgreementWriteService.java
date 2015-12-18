package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

@Service
public class PurchaseAgreementWriteService {

    @Autowired
    ReportRepository reportRepository;

    @Transactional
    public void changeShippingAddress(String docNo, Address shippingAddress) {

        Report report = reportRepository.findByDocumentNumber(docNo);
        if (report == null) throw new NotFoundException("Konnte Dokument mit gegebener Nummer nicht finden");
        OrderConfirmation oc = (OrderConfirmation) report;
        oc.changeShippingAddress(shippingAddress);
        reportRepository.save(oc);
    }

}
