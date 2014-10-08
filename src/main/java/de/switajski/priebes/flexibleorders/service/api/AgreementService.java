package de.switajski.priebes.flexibleorders.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.OrderConfirmationRepository;

@Service
public class AgreementService {

    @Autowired
    private OrderConfirmationRepository reportRepo;

    @Transactional
    public OrderConfirmation agree(String orderConfirmationNo, String orderAgreementNo) {
        OrderConfirmation oc = reportRepo.findByDocumentNumber(orderConfirmationNo);
        if (oc == null) {
            throw new IllegalArgumentException("Auftragsbest" + Unicode.aUml + "tigung mit angegebener Nummer nicht gefunden");
        }
        oc.setOrderAgreementNumber(orderAgreementNo);
        return reportRepo.save(oc);
    }

}
