package de.switajski.priebes.flexibleorders.datasources;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

public class PriebesJoomlaImportTest {
	
	PriebesJoomlaImport pji;
	
	@Autowired 
	CustomerRepository customerRepo;
	
	@Before
	public void shouldInitialize(){
		pji = new PriebesJoomlaImport();
	}
	
	@Test
	public void shouldImportCustomer(){
		assertFalse(customerRepo.count()==0);
	}

}
