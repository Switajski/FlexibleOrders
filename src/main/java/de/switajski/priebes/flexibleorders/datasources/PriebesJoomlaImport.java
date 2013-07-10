package de.switajski.priebes.flexibleorders.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.Category;
import de.switajski.priebes.flexibleorders.domain.CustomerService;

public class PriebesJoomlaImport {

	private static Logger log = Logger.getLogger(PriebesJoomlaImport.class);
	
	public static final String CAT_IMAGE_PATH="D:/PriebesJoomlaXampp/htdocs/media/k2/categories";
	public static final String DATABASE_URL = "jdbc:mysql://localhost/bestellsystemv2?"
			+ "user=root&password=&useUnicode=yes&characterEncoding=UTF-8";

	Connection connection;
	private Category rootCategory;
	
	@Autowired
	CustomerService customerService;
	
	public PriebesJoomlaImport() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = (Connection) DriverManager
					.getConnection(DATABASE_URL);
			if (checkDuplicates())
				throw new IllegalStateException("Cannot import as long as there are duplicates!");
			else{
				importCustomers();
				importProducts();
				importOrders();
				importPrices();
				setCategories();
				closeConnection();		
				log.debug("Import ready");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean checkDuplicates() {
		// TODO Auto-generated method stub
		return false;
	}

	public void importCustomers() {
		// TODO Auto-generated method stub
		
	}

	public void importProducts() {
		// TODO Auto-generated method stub
		
	}

	public void setCategories() {
		// TODO Auto-generated method stub
		
	}

	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}

	public void importPrices() {
		// TODO Auto-generated method stub
		
	}

	public void importOrders() {
		// TODO Auto-generated method stub
		
	}

}
