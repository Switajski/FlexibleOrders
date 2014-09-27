package de.switajski.priebes.flexibleorders.service.process;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliveryServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportItemRepository reportItemRepository;

    @InjectMocks
    private DeliveryService deliveryService = new DeliveryService();

    @Test(expected = IllegalStateException.class)
    public void deliver_shouldRejectContradictingPurchaseAgreements() {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(reportItemRepository.findOne(1L)).thenReturn(givenAgreementItem());
        when(reportItemRepository.findOne(2L)).thenReturn(givenAgreementItemWithDifferentPurchaseAgreement());

        // WHEN
        deliveryService.deliver(createDeliverParameter(givenItemDto1(), givenItemDto2()));

        // THEN expect IllegalArgumentException
    }
    
    @Test
    public void deliver_shouldAcceptEqualAgreementDetails() {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(reportItemRepository.findOne(1L)).thenReturn(givenAgreementItem());
        when(reportItemRepository.findOne(2L)).thenReturn(givenAgreementItem());

        // WHEN
        deliveryService.deliver(createDeliverParameter(givenItemDto1(), givenItemDto2()));

        // THEN 
        verify(reportRepository).save(Matchers.any(DeliveryNotes.class));
    }


    private ReportItem givenAgreementItemWithDifferentPurchaseAgreement() {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(givenPurchaseAgreement())
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setExpectedDelivery(new Date());
        pa.setCustomerNumber(123L);
        return pa;
    }

    private ReportItem givenAgreementItem() {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(givenOtherPurchaseAgreement())
                                .build())
                .build();
    }

    private PurchaseAgreement givenOtherPurchaseAgreement() {
        PurchaseAgreement ad = new PurchaseAgreement();
        ad.setExpectedDelivery(new DateTime(new Date()).plusDays(10).toDate());
        ad.setCustomerNumber(123L);
        return ad;
    }

    private DeliverParameter createDeliverParameter(ItemDto... items) {
        DeliverParameter dp = new DeliverParameter();
        dp.agreementItemDtos = new ArrayList<ItemDto>();
        for (ItemDto item:items)
            dp.agreementItemDtos.add(item);
        return dp;
    }

    private ItemDto givenItemDto2() {
        ItemDto i = new ItemDto();
        i.id = 2L;
        i.quantityLeft = 2;
        return i;
    }

    private ItemDto givenItemDto1() {
        ItemDto i = new ItemDto();
        i.id = 1L;
        i.quantityLeft = 1;
        return i;
    }
}
