package de.switajski.priebes.flexibleorders.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Category;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

@Service
@Transactional
public class PriebesJoomlaImporterService implements ImporterService {

	private static Logger log = Logger.getLogger(PriebesJoomlaImporterService.class);
	
	public static final String CAT_IMAGE_PATH="D:/PriebesJoomlaXampp/htdocs/media/k2/categories";
	public static final String DATABASE_URL = "jdbc:mysql://localhost/bestellsystemv2?"
			+ "user=root&password=&useUnicode=yes&characterEncoding=UTF-8";
	private static final String PRIEBES_DB = "priebesJoomlaDb";
	private Connection connection;
	private Category rootCategory;
	
	@Autowired
	CustomerRepository customerRepository;


	private Connection getConnection(){
		if (this.connection==null)
			connection = this.init();
		return this.connection;
			
	}
	
	public Connection init() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = (Connection) DriverManager
					.getConnection(DATABASE_URL);
			if (checkDuplicates())
				throw new IllegalStateException("Cannot import as long as there are duplicates!");
			return connection;
			
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
		return connection;
	}

	public boolean checkDuplicates() {
		// TODO Auto-generated method stub
		return false;
	}

	@Transactional
	public void importCustomers() {
		Statement stmt;
		connection = this.getConnection();
		try {
			stmt = (Statement) connection.createStatement(
					 ResultSet.TYPE_SCROLL_INSENSITIVE,
					 ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT * from " + PRIEBES_DB
					+".jos_k2store_address left join priebesJoomlaDb.jos_users on jos_k2store_address.user_id=jos_users.id");
			
			while (rs.next()) {
				// retrieve and print the values for the current row
				int id = rs.getInt("id");
				int user_id = rs.getInt("user_id");
				String email = rs.getString("email");
				if (email==null || existsCustomer(email)) continue;
				String first_name = rs.getString("first_name");
				String last_name = rs.getString("last_name");
				String password = rs.getString("password");
				
				String address_1 = rs.getString("address_1");
				address_1 += " " +rs.getString("address_2");
				String city = rs.getString("city");
				String zip = rs.getString("zip".trim());
				String phone_1 = rs.getString("phone_1");
				
				log.debug("ROW = " + id + " " + user_id + " " + first_name + " " + last_name);
				
				Customer c = new Customer();
				c.setEmail(email);		
				c.setPhone(phone_1);
				c.setName2(first_name + " " + last_name);
				c.setPassword(password);
				c.setCreated(new Date());
				c.setCity(city);
				c.setStreet(address_1);
				c.setPostalCode(Integer.parseInt(zip.trim()));
				c.setCountry(Country.GERMANY);
				customerRepository.save(c);
				
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private boolean existsCustomer(String email) {
		return !customerRepository.findByEmail(email).isEmpty();
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
