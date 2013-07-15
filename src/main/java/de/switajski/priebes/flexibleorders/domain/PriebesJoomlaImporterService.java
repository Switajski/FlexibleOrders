package de.switajski.priebes.flexibleorders.domain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

@Service
@Transactional
/**
 * TODO: This class is quick and dirty. PriebesJoomla should be a repository with a second database instead.
 * like in http://stackoverflow.com/questions/4423125/spring-is-it-possible-to-use-multiple-transaction-managers-in-the-same-applica
 * 
 * @author Marek
 *
 */
public class PriebesJoomlaImporterService implements ImporterService {

	private static Logger log = Logger.getLogger(PriebesJoomlaImporterService.class);
	
	public static final String CAT_IMAGE_PATH="D:/PriebesJoomlaXampp/htdocs/media/k2/categories";
	public static final String DATABASE_URL = "jdbc:mysql://localhost/bestellsystemv2?"
			+ "user=root&password=&useUnicode=yes&characterEncoding=UTF-8";
	private static final String PRIEBES_DB = "priebesJoomlaDb";
	private Connection connection;
	private Category rootCategory;
	
	@Autowired	CustomerRepository customerRepository;
	@Autowired	CategoryRepository categoryRepo;
	@Autowired	ProductRepository productRepository;
	@Autowired	OrderItemRepository orderItemRepository;
	@Autowired	ShippingItemRepository shippingItemRepo;
	@Autowired	InvoiceItemRepository invoiceRepo;
	@Autowired 	ArchiveItemRepository archiveRepo;
	
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
				c.setShortName(last_name);
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
		return (customerRepository.findByEmail(email)!=null);
	}

	public void importProducts() {
		Statement stmt;
		try {
			stmt = (Statement) getConnection().createStatement(
					 ResultSet.TYPE_SCROLL_INSENSITIVE,
					 ResultSet.CONCUR_READ_ONLY);
			String catImagePath="D:/PriebesJoomlaXampp/htdocs/media/k2/categories";
			
			ResultSet rs = stmt.executeQuery("SELECT * from " + PRIEBES_DB
					+".jos_k2_categories ORDER BY id");
			
			while (rs.next()) {
				Long id = rs.getLong("id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				int parent = rs.getInt("parent");
				int ordering = rs.getInt("ordering");
				boolean trash = rs.getBoolean("trash");
				if (trash) continue;
				boolean published = false;
				if (rs.getInt("published")==0) published = false;
				String image = rs.getString("image");
				
				log.debug("ROW = " + id + " " + name + " " + description + " " + parent);
				
				Category kat = new Category();
				kat.setName(name);
				kat.setActivated(published);
				kat.setImage(image);
				kat.setSortOrder(ordering);
				
				categoryRepo.save(kat);
				
				this.populateCategoryWithArtikel(kat,id);
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void populateCategoryWithArtikel(Category category, Long id) {
		Statement stmt2;
		try {
			stmt2 = (Statement) getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			ResultSet as = stmt2.executeQuery("SELECT * from "
					+ PRIEBES_DB
					+ ".jos_k2_items where catid="+id);
			while (as.next()) {
				Long a_id = as.getLong("id");
				String a_title = as.getString("title"); 

				String a_intro = as.getString("introtext");
				String a_fulltext = as.getString("fulltext");
				boolean a_trash = as.getBoolean("trash"); 
				boolean a_published = as.getBoolean("published");
				String a_video = as.getString("video");
				String a_gallery = as.getString("gallery");
				if (a_gallery != null) {
					a_gallery = a_gallery.replace("{gallery}", "");
					a_gallery = a_gallery.replace("{/gallery}", "");
				}
				Date a_created = as.getDate("created");
				long a_ordering = as.getLong("ordering");
				long a_artikelnummer = as.getLong("artikelnummer");
				if (a_artikelnummer==0 || (productRepository.findByProductNumber(a_artikelnummer)!=null)) {
					Random gen = new Random();
					a_artikelnummer = gen.nextInt();
					a_artikelnummer = Math.abs(a_artikelnummer);
				}
				else log.debug("originale Artikelnummer genommen:"+a_artikelnummer) ;
				int a_catid = as.getInt("catid");

				log.debug("ROW = " + a_id + " " + a_title + " " + a_gallery + " " + a_catid);

				Product artikel = new Product();
				artikel.setName(a_title);
				artikel.setActive(a_published);
				artikel.setImageGalery(a_gallery);
				artikel.setSortOrder(a_ordering);
				artikel.setProductNumber(a_artikelnummer);
				artikel.setCategory(category);
				artikel.setProductType(ProductType.PRODUCT);
				if (artikel.getId() == null) {
					productRepository.save(artikel);
				}

			}
			stmt2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void importPrices() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importOrderItems() {

		Statement stmt;
		try {
			stmt = (Statement) getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet orders = stmt.executeQuery("SELECT * from priebesJoomlaDb.jos_k2store_orders");

			//Loop for Order
			while(orders.next()){
				Statement stmt2 = (Statement) getConnection().createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet orderItems = stmt2.executeQuery("SELECT * from priebesJoomlaDb.jos_k2store_orderitems where order_id="+orders.getLong("order_id"));

				//Loop for OrderItems
				while(orderItems.next()){
					OrderItem oi = new OrderItem();
					
					Long orderNumber = orders.getLong("order_id");
					oi.setOrderNumber(orderNumber);
					List<OrderItem> orderItemList = orderItemRepository.findByOrderNumber(orderNumber);
					if (orderItemList.isEmpty()) oi.setOrderItemNumber(1);
					else oi.setOrderItemNumber(orderItemList.size()+1);
					
					String product_id = orderItems.getString("product_id");
					String title = this.getSingleResult("select title from priebesJoomlaDb.jos_k2_items where id="+product_id, "title");
					if (title==null) continue;
					Product product = productRepository.findByName(title);
					oi.setProduct(product);
					
					oi.setCreated(orders.getDate("created_date"));
					String email = this.getSingleResult("SELECT email from priebesJoomlaDb.jos_users where id="+orders.getInt("user_id"), "email");
					Customer customer = customerRepository.findByEmail(email);
					if (customer==null) continue;
					oi.setCustomer(customer);
					
					if (orderItems.getLong("rechnung_id") != 0l)
					{oi.setAccountNumber(orderItems.getLong("rechnung_id"));
					oi.setInvoiceNumber(orderItems.getLong("rechnung_id"));}
					if (orderItems.getLong("ab_id") != 0l)
					oi.setOrderConfirmationNumber(orderItems.getLong("ab_id"));
					
					oi.setQuantity(orderItems.getInt("orderitem_quantity"));
					oi.setPriceNet(orderItems.getBigDecimal("orderitem_price"));
					oi.setStatus(Status.COMPLETED);
					
					orderItemRepository.save(oi);
					
					if (oi.getOrderConfirmationNumber()!=null){
						ShippingItem si = oi.confirm(false);
						shippingItemRepo.save(si);
						orderItemRepository.save(oi);
						if (oi.getInvoiceNumber()!=null){
							InvoiceItem ii = si.deliver();
							invoiceRepo.save(ii);
							orderItemRepository.save(oi);
							if (oi.getAccountNumber()!=null){
								ArchiveItem ai = ii.complete(si);
								archiveRepo.save(ai);
								invoiceRepo.save(ii);
								orderItemRepository.save(oi);
							}
						}
					}
					
				}
				stmt2.close();			
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private String getSingleResult(String query, String column) throws SQLException{
		Statement stmt = (Statement) getConnection().createStatement(
				 ResultSet.TYPE_SCROLL_INSENSITIVE,
				 ResultSet.CONCUR_READ_ONLY);
		
		ResultSet as = stmt.executeQuery(query);
		String toReturn = null;
		while (as.next()){
			toReturn = as.getString(column);
		}
		as.close();
		stmt.close();
		return toReturn;
	}
	


}
