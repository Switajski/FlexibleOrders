package de.switajski.priebes.flexibleorders.service.process;

import static de.switajski.priebes.flexibleorders.service.process.ProcessServiceHelper.validateQuantity;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.exceptions.BusinessErrorCode;
import de.switajski.priebes.flexibleorders.exceptions.SystemException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;

@Service
public class DeliveryService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoConverterService convService;
    @Autowired
    private ShippingAddressService shippingAddressService;

    @Transactional
    public DeliveryNotes deliver(DeliverParameter deliverParameter) {
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) throw new IllegalArgumentException(
                "Lieferscheinnummer existiert bereits");

        Map<ReportItem, Integer> risWithQty = convService.mapItemDtosToReportItemsWithQty(deliverParameter.agreementItemDtos);
        Set<ReportItem> ris = risWithQty.keySet();

        Address shippingAddress = retrieveInvoicingAddress(ris, deliverParameter.ignoreContradictoryExpectedDeliveryDates);
        DeliveryNotes deliveryNotes = createDeliveryNotes(deliverParameter);

        for (Entry<ReportItem, Integer> riWithQty : risWithQty.entrySet()) {
            ReportItem agreementItem = riWithQty.getKey();
            int qty = riWithQty.getValue();
            OrderItem orderItemToBeDelivered = agreementItem.getOrderItem();

            validateQuantity(qty, agreementItem);

            deliveryNotes.addItem(new ShippingItem(
                    deliveryNotes,
                    orderItemToBeDelivered,
                    qty,
                    new Date()));

            deliveryNotes.setShippedAddress(shippingAddress);
        }

        return reportRepo.save(deliveryNotes);
    }

    private DeliveryNotes createDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.deliveryNotesNumber);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);
        // TODO: could this be retrieved from DB?
        deliveryNotes.setCustomerNumber(deliverParameter.customerNumber);
        return deliveryNotes;
    }

    private Address retrieveInvoicingAddress(Set<ReportItem> reportItems, boolean ignoreContradictoryExpectedDeliveryDates) {
        Set<Address> ias = shippingAddressService.retrieve(reportItems);
        if (ias.size() > 1 && !ignoreContradictoryExpectedDeliveryDates) 
            throw new SystemException("Verschiedene Lieferadressen in Auftr"+ Unicode.aUml + "gen gefunden: "
                + BeanUtil.createStringOfDifferingAttributes(ias), BusinessErrorCode.CONTRADICTORY_PAYMENT_AGREEMENTS);
        else if (ias.size() == 0) 
            throw new SystemException("Keine Lieferaddresse aus Kaufvertr" + Unicode.aUml + "gen gefunden", BusinessErrorCode.NOT_FOUND);
        Address invoicingAddress = ias.iterator().next();
        return invoicingAddress;
    }

}
