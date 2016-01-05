package de.switajski.priebes.flexibleorders.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.repository.ReportRepository;

@Service
public class DeliveryNotesNumberGeneratorService {

    @Autowired
    private ReportRepository reportRepository;

    public static final String PENDING_ITEMS_SUFFIX = "-AA";

    public String byOrderConfrimationNumber(String orderConfirmationNumber) {
        String firstDeliveryNotesNo = orderConfirmationNumber.replace("AB", "L");
        if (reportRepository.findByDocumentNumber(firstDeliveryNotesNo) == null) {
            return firstDeliveryNotesNo;
        }
        else {
            for (int i = 1; i < 10; i++) {
                String pendingDNNo = generatePendingDeliveryNotesNo(firstDeliveryNotesNo, i);
                if (reportRepository.findByDocumentNumber(pendingDNNo) == null) {
                    return pendingDNNo;
                }
            }
            return firstDeliveryNotesNo;
        }
    }

    public String generatePendingDeliveryNotesNo(String firstDeliveryNotesNo, int i) {
        return firstDeliveryNotesNo + PENDING_ITEMS_SUFFIX + i;
    };

    public void setReportRepository(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

}
