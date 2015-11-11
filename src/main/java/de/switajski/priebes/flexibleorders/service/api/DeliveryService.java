package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalDate.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.QuantityLeftCalculatorService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;

@Service
public class DeliveryService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoConverterService convService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private QuantityLeftCalculatorService qtyLeftCalcService;
    @Autowired
    private ExpectedDeliveryService expectedDeliveryService;

    @Transactional
    public DeliveryNotes deliver(DeliverParameter deliverParameter) {
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) throw new IllegalArgumentException(
                "Lieferscheinnummer existiert bereits");

        Map<ReportItem, Integer> risWithQty = convService.mapItemDtosToReportItemsWithQty(deliverParameter.agreementItemDtos);
        Set<ReportItem> ris = risWithQty.keySet();

        Address shippingAddress = retrieveShippingAddress(ris);
        DeliveryNotes deliveryNotes = createDeliveryNotes(deliverParameter);
        if (!deliverParameter.ignoreContradictoryExpectedDeliveryDates){
        	validateExptectedDeliveryDates(ris, deliveryNotes);
        }

        for (Entry<ReportItem, Integer> riWithQty : risWithQty.entrySet()) {
            ReportItem agreementItem = riWithQty.getKey();
            int qty = riWithQty.getValue();
            OrderItem orderItemToBeDelivered = agreementItem.getOrderItem();

            qtyLeftCalcService.validateQuantity(qty, agreementItem);

            deliveryNotes.addItem(new ShippingItem(
                    deliveryNotes,
                    orderItemToBeDelivered,
                    qty,
                    new Date()));

            deliveryNotes.setShippedAddress(shippingAddress);
        }

        return reportRepo.save(deliveryNotes);
    }

    //TODO: move to validator
	private void validateExptectedDeliveryDates(Set<ReportItem> ris,
			DeliveryNotes deliveryNotes) {
		Set<Integer> expectedDeliveryDates = expectedDeliveryService.retrieveWeekOfYear(ris);
		if (expectedDeliveryDates.size() > 1){
			StringBuilder messageBuilder = new StringBuilder("Angegebene Positionen haben ABs mit widerspr" + Unicode.U_UML + "chlichen Lieferdaten: ");
			Iterator<Integer> lItr = expectedDeliveryDates.iterator(); 
			while (lItr.hasNext()){
				Integer kw = lItr.next();
				messageBuilder.append("KW ").append(kw);
				if (lItr.hasNext()){
					messageBuilder.append(", ");
				}
			}
			throw new ContradictoryPurchaseAgreementException(messageBuilder.toString());
		} else if (expectedDeliveryDates.size() == 1) {
			int expectedWeek = expectedDeliveryDates.iterator().next();
			int isWeek = weekOf(deliveryNotes.getCreated());
			if (expectedWeek != isWeek){
				throw new ContradictoryPurchaseAgreementException("Widerrüchliche Liefertermine: KW aus AB ist " + expectedWeek + ", Datum des Lieferscheins liegt aber in KW " + isWeek);
			}
		}
	}

    private int weekOf(Date created) {
    	return weekOf(new LocalDate(created));
	}

	private int weekOf(LocalDate next) {
		return next.weekOfWeekyear().get();
	}

	private DeliveryNotes createDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.deliveryNotesNumber);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);
        deliveryNotes.setShippingCosts(deliverParameter.shipment);
        deliveryNotes.setDeliveryMethod(deliverParameter.deliveryMethod);
        deliveryNotes.setShowPrices(deliverParameter.showPricesInDeliveryNotes);
        return deliveryNotes;
    }

    /**
     * @return one invoicing Address
     * @throws ContradictoryPurchaseAgreementException if no single invoicing address could be determined
     */
    private Address retrieveShippingAddress(Set<ReportItem> reportItems) {
        Set<Address> ias = shippingAddressService.retrieve(reportItems);
        if (ias.size() > 1) 
        	throw new ContradictoryPurchaseAgreementException(
                "Verschiedene Lieferadressen in Auftr" + Unicode.A_UML + "gen gefunden: "
                        + BeanUtil.createStringOfDifferingAttributes(ias));
        else if (ias.size() == 0) throw new NotFoundException("Keine Lieferaddresse aus Kaufvertr" + Unicode.A_UML + "gen gefunden");
        Address invoicingAddress = ias.iterator().next();
        return invoicingAddress;
    }

}
