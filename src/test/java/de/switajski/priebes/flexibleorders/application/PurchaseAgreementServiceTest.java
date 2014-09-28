package de.switajski.priebes.flexibleorders.application;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AgreementItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;

public class PurchaseAgreementServiceTest {

    @Mock
    private ReportItemRepository reportItemRepo;
    @InjectMocks
    private PurchaseAgreementService purchaseAgreementService = new PurchaseAgreementService();
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void retrieveOneOrFail_shouldThrowExceptionOnContradictoryPurchaseAgreements() {
        // GIVEN
        HashSet<Long> riIds = new HashSet<Long>(Arrays.asList(1L, 2L));
        when(reportItemRepo.findAll(riIds)).thenReturn(Arrays.asList(
                givenAgreementItem(),
                givenAgreementItemWithOtherPurchaseAgreement()));

        // WHEN
        purchaseAgreementService.retrieveOneOrFail(riIds);

        // THEN expect Exception
    }
    
    @Test
    public void retrieveOneOrFail_shouldReturnOneIfPurchaseAgreementsAreEqual() {
        // GIVEN
        HashSet<Long> riIds = new HashSet<Long>(Arrays.asList(1L, 2L));
        when(reportItemRepo.findAll(riIds)).thenReturn(Arrays.asList(
                givenAgreementItem(),
                givenAgreementItem()));

        // WHEN
        PurchaseAgreement purchaseAgreement = purchaseAgreementService.retrieveOneOrFail(riIds);

        // THEN 
        assertThat(purchaseAgreement, is(not(nullValue())));
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
                                .setAgreementDetails(givenPurchaseAgreement())
                                .build())
                .build();
    }

    private PurchaseAgreement givenOtherPurchaseAgreement() {
        PurchaseAgreement ad = new PurchaseAgreement();
        ad.setExpectedDelivery(new LocalDate().plusDays(10));
        ad.setCustomerNumber(123L);
        return ad;
    }

    private ReportItem givenAgreementItemWithOtherPurchaseAgreement() {
        return new AgreementItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderAgreementBuilder()
                                .setAgreementDetails(givenOtherPurchaseAgreement())
                                .build())
                .build();
    }

    private PurchaseAgreement givenPurchaseAgreement() {
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setExpectedDelivery(new LocalDate());
        pa.setCustomerNumber(123L);
        return pa;
    }

}
