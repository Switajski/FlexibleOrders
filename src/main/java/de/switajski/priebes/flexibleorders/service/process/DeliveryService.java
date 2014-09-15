package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class DeliveryService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ReportItemRepository reportItemRepo;

    @Transactional
    public DeliveryNotes deliver(DeliverParameter deliverParameter) {
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) 
            throw new IllegalArgumentException("Lieferscheinnummer existiert bereits");

        DeliveryNotes deliveryNotes = new DeliveryNotes(
                deliverParameter.deliveryNotesNumber,
                null,
                deliverParameter.shipment);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);

        Order firstOrder = null;

        Address shippedAddress = null;
        for (ItemDto agreementItemDto : deliverParameter.agreementItemDtos) {
            ReportItem agreementItem = reportItemRepo
                    .findOne(agreementItemDto.id);
            OrderItem orderItemToBeDelivered = agreementItem
                    .getOrderItem();

            ServiceHelper.validateQuantity(agreementItemDto.quantityLeft, agreementItem);

            deliveryNotes.addItem(new ShippingItem(
                    deliveryNotes,
                    orderItemToBeDelivered,
                    agreementItemDto.quantityLeft, // TODO: GUI sets
                    // quanitityToDeliver at this
                    // nonsense parameter
                    new Date()));

            // validate addresses DRY!
            AgreementHistory agreementHistory = new AgreementHistory(DeliveryHistory.createFrom(orderItemToBeDelivered));
            shippedAddress = validateShippingAddress(
                    shippedAddress, 
                    agreementHistory);
            validateAgreementDetails(agreementHistory.getAgreementDetails());

            deliveryNotes.setShippedAddress(shippedAddress);

            if (firstOrder == null) firstOrder = orderItemToBeDelivered.getOrder();
        }

        // TODO:Refactor: DRY!
        deliveryNotes.setCustomerNumber(firstOrder
                .getCustomer()
                .getCustomerNumber());
        return reportRepo.save(deliveryNotes);
    }

    private void validateAgreementDetails(Set<AgreementDetails> agreementDetails) {
        if (agreementDetails.size() > 1)
            throw new IllegalArgumentException("Zu liefernde Auftr"+Unicode.aUml+"ge haben unterschiedlich Vereinbarungen");
    }

    private Address validateShippingAddress(Address shippedAddress, AgreementHistory history) {
        if (hasShippingAddressFromAgreementDetails(history)){
            Address temp = history.getOneAgreementDetail().getShippingAddress();
            if (shippedAddress == null) shippedAddress = temp;
            else if (!shippedAddress.equals(temp)) 
                throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");
        }
        return shippedAddress;
    }

    private boolean hasShippingAddressFromAgreementDetails(AgreementHistory history) {
        if (history == null)
            return false;
        if (history.getOneAgreementDetail() == null)
            return false;
        if (history.getOneAgreementDetail().getShippingAddress() == null)
            return false;
        return true;
    }

}
