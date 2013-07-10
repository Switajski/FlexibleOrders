package de.switajski.priebes.flexibleorders.datasources;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

public class PriebesJoomlaImportTest {
	
	PriebesJoomlaImport pji;
	
	@Before
	public void shouldInitialize(){
		pji = new PriebesJoomlaImport();
	}
	
	@Transactional
	@Test
	public void shouldImportCustomer(){
		
	}

}
