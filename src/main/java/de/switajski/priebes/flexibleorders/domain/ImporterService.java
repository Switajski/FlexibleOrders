package de.switajski.priebes.flexibleorders.domain;


public interface ImporterService {
	
	public void importCustomers();
	
	public void importOrderItems();
	
	public void importPrices();
	
	public void importProducts();

}