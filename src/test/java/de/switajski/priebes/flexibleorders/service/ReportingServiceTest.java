package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderAgreementBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;

public class ReportingServiceTest {

	private static final String OC_NO = "AB-1";
	private static final String OA_NO = "AU-1";

	@Mock
	ReportRepository reportRepo;
	
	@InjectMocks
	ReportingService reportingService = new ReportingService();
	
	@Test
	public void retrieveOrderConfirmation_shouldReturnOrderConfirmation(){
		//GIVEN
		givenReports();
		
		//WHEN
		OrderConfirmation oc = reportingService.retrieveOrderConfirmation(OC_NO);
		
		//THEN
		assertThat(oc, not(nullValue()));
	}
	
	private void givenReports(){
		MockitoAnnotations.initMocks(this);
		when(reportRepo.findByDocumentNumber(OC_NO)).thenReturn(
				new OrderConfirmationBuilder().setDocumentNumber(OC_NO).build());
		when(reportRepo.findByDocumentNumber(OA_NO)).thenReturn(
				new OrderAgreementBuilder().setDocumentNumber(OA_NO).build());
	}
	
	@Test
	public void retrieveOrderAgreemenet_shouldReturnOrderAgreement(){
		//GIVEN
		givenReports();
		
		//WHEN
		OrderAgreement oa = reportingService.retrieveOrderAgreement(OA_NO);
		
		//THEN
		assertThat(oa, not(nullValue()));
	}
	
	@Test
	public void retrieveOrderAgreemenet_shouldNotReturnOrderConfirmation(){
		//GIVEN
		givenReports();
		
		//WHEN
		OrderAgreement oa = reportingService.retrieveOrderAgreement(OC_NO);
		
		//THEN
		assertThat(oa, nullValue());
	}
	
}
