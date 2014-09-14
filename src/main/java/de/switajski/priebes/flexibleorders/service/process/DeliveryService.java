package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
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
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

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
            shippedAddress = validateShippingAddress(shippedAddress, new AgreementHistory(DeliveryHistory.createFrom(orderItemToBeDelivered)));

            deliveryNotes.setShippedAddress(shippedAddress);

            if (firstOrder == null) firstOrder = orderItemToBeDelivered.getOrder();
        }

        // TODO:Refactor: DRY!
        deliveryNotes.setCustomerNumber(firstOrder
                .getCustomer()
                .getCustomerNumber());
        return reportRepo.save(deliveryNotes);
    }

    private Address validateShippingAddress(Address shippedAddress, AgreementHistory history) {
        Address temp = history.getAgreementDetails().getShippingAddress();
        if (shippedAddress == null) shippedAddress = temp;
        else if (!shippedAddress.equals(temp)) throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");
        return shippedAddress;
    }

}
