package de.switajski.priebes.flexibleorders.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.Category;
import de.switajski.priebes.flexibleorders.domain.CustomerService;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

public class PriebesJoomlaImport {

	private static Logger log = Logger.getLogger(PriebesJoomlaImport.class);
	
	public static final String CAT_IMAGE_PATH="D:/PriebesJoomlaXampp/htdocs/media/k2/categories";
	public static final String DATABASE_URL = "jdbc:mysql://localhost/bestellsystemv2?"
			+ "user=root&password=&useUnicode=yes&characterEncoding=UTF-8";

	private static final String PRIEBES_DB = "priebesJoomlaDb";

	Connection connection;
	private Category rootCategory;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CustomerRepository customerRepository;
	
	
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

	public void importCustomers() throws SQLException {
		Statement stmt = (Statement) connection.createStatement(
				 ResultSet.TYPE_SCROLL_INSENSITIVE,
				 ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = stmt.executeQuery("SELECT * from " + PRIEBES_DB
				+".jos_k2store_address left join priebesJoomlaDb.jos_users on jos_k2store_address.user_id=jos_users.id");
		
		while (rs.next()) {
			// retrieve and print the values for the current row
			int id = rs.getInt("id");
			int user_id = rs.getInt("user_id");
			String email = rs.getString("email");
			if (email==null || !existsCustomer(email)) continue;
			String first_name = rs.getString("first_name");
			String last_name = rs.getString("last_name");
			String password = rs.getString("password");

			String address_1 = rs.getString("address_1");
			address_1 += " " +rs.getString("address_2");
			String city = rs.getString("city");
			String zip = rs.getString("zip".trim());
			String phone_1 = rs.getString("phone_1");
			
			log.debug("ROW = " + id + " " + user_id + " " + first_name + " " + last_name);
			
			/*Customer kunde = new Kunde();
			kunde.setAnrede(Anrede.HERR);
			kunde.setBenutzerkonto(true);
			kunde.setEmail(email);
			kunde.setKundenart(Kundenart.HAENDLER);
			kunde.setNachname(last_name);
			kunde.setPassword(password);
			kunde.setVorname(first_name);
			kunde.setErstelltDatum(new Date());
			kunde.setKundennummer(user_id);
			kunde.persist();
			
			Adresse adresse = new Adresse();
			adresse.setErstelltDatum(new Date());
			adresse.setKunde(kunde);
			adresse.setLand(Land.DEUTSCHLAND);
			adresse.setPerson(first_name + " "+last_name);
			adresse.setPlz(Integer.parseInt(zip.trim()));
			adresse.setStadt(city);
			adresse.setStrasse(address_1);
			adresse.persist();
			
			kunde.setLieferadresse(adresse);
			kunde.setRechnungsadresse(adresse);
			kunde.merge();*/
			
		}
//		stmt.close();


		
	}

	private boolean existsCustomer(String email) {
//		customerRepository.findCustomerByEmail(email);
//		return.findKundesByEmailLike(email).getResultList().isEmpty();
		return false;
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
