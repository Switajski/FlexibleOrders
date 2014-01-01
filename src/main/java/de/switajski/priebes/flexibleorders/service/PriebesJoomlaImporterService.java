package de.switajski.priebes.flexibleorders.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Category;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.factory.WholesaleOrderFactory;
import de.switajski.priebes.flexibleorders.domain.specification.Address;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.PayedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;

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
	private static final String PRIEBES_DB = "db358736992";
	public static final String DATABASE_URL = "jdbc:mysql://localhost/"+PRIEBES_DB+"?"
			+ "user=root&password=&useUnicode=yes&characterEncoding=UTF-8";
	private Connection connection;

	private	CustomerRepository customerRepository;
	private	CategoryRepository categoryRepo;
	private	CatalogProductRepository productRepository;
	private	ItemRepository itemRepository;
	private HandlingEventService heService;

	private Connection getConnection(){
		if (this.connection==null)
			connection = this.init();
		return this.connection;

	}

	@Autowired
	public PriebesJoomlaImporterService(
			CustomerRepository customerRepository,
			CategoryRepository categoryRepo,
			CatalogProductRepository productRepository,
			ItemRepository itemRepository,
			HandlingEventService handlingEventService) {
		this.customerRepository = customerRepository;
		this.categoryRepo = categoryRepo;
		this.productRepository = productRepository;
		this.itemRepository = itemRepository;
		this.heService = handlingEventService;
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
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
					+".jos_k2store_address left join "+PRIEBES_DB+".jos_users on jos_k2store_address.user_id=jos_users.id");

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
				c.setShortName(last_name);
				c.setPassword(password);
				c.setCreated(new Date());
				c.setAddress(new Address(first_name, last_name, address_1, Integer.parseInt(zip.trim()), city, Country.GERMANY));
				customerRepository.saveAndFlush(c);

			}
			stmt.close();
		} catch (SQLException e) {
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
				if (a_artikelnummer==0 || productRepository.findByProductNumber(a_artikelnummer)==null) {
					Random gen = new Random();
					a_artikelnummer = gen.nextInt();
					a_artikelnummer = Math.abs(a_artikelnummer);
				}
				else log.debug("originale Artikelnummer genommen:"+a_artikelnummer) ;
				int a_catid = as.getInt("catid");

				log.debug("ROW = " + a_id + " " + a_title + " " + a_gallery + " " + a_catid);

				CatalogProduct artikel = new CatalogProduct();
				if (a_title.equals("wendelin"))
					a_title.charAt(0);
				artikel.setName(a_title);
				artikel.setActive(a_published);
				artikel.setImageGalery(a_gallery);
				artikel.setSortOrder(a_ordering);
				artikel.setProductNumber(a_artikelnummer);
				artikel.setCategory(category);
				artikel.setProductType(ProductType.PRODUCT);
				if (artikel.getId() == null) {
					productRepository.saveAndFlush(artikel);
				}

			}
			stmt2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void importPrices() {
		Statement stmt;
		try {
			stmt = (Statement) getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT * from "+PRIEBES_DB+".jos_k2_items");

			while (rs.next()){
				Date created = rs.getDate("created");
				String title = rs.getString("title");
				if (title == null || title.isEmpty()){
					System.out.println("Importiere Preise: Name des Artikels ist leer!");
					continue;
				}
				if (productRepository.findByName(title) == null) continue;
				CatalogProduct artikel = productRepository.findByName(title);
				String plugins = rs.getString("plugins");
				if (!plugins.contains("k2storeitem_price=")) continue;
				String[] PluginsArray = plugins.split("\nk2storeitem_tax");
				if (PluginsArray.length!=2) System.out.println("Fehler beim Einlesen der k2_items.plugins!");
				String item_price=PluginsArray[0]; //k2storeitem_price=28.10
				String item_bestand = PluginsArray[1]; //k2storeitem_shipping=1
				//k2storeitem_bestand=
				item_price=item_price.substring(18);
				if (item_price.isEmpty()) continue; 
				item_price = item_price.replace(",", ".");
				System.out.println(item_price);
				double k2storeitem_price = Double.parseDouble(item_price);
				BigDecimal price = BigDecimal.valueOf(k2storeitem_price);
				BigDecimal item_priceD= BigDecimal.valueOf(k2storeitem_price);

				artikel.setRecommendedPriceNet(item_priceD);
				productRepository.saveAndFlush(artikel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void importOrderItems() {

		Statement stmt;
		try {
			stmt = (Statement) getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet orders = stmt.executeQuery("SELECT * from "
					+PRIEBES_DB+".jos_k2store_orders");

			//Loop for Order
			while(orders.next()){
				Statement stmt2 = (Statement) getConnection().createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet orderItems = stmt2.executeQuery("SELECT * from "+PRIEBES_DB+
						".jos_k2store_orderitems where order_id="+orders.getLong("order_id"));

				//Loop for OrderItems
				while(orderItems.next()){
					CatalogProduct product = productRepository.findByName(getProductTitle(orderItems.getString("product_id")));
					Customer customer = customerRepository.findByEmail(
							getEmailOfOiCustomer(orders));				
					int quantity = orderItems.getInt("orderitem_quantity");
					
					OrderedSpecification orderedSpec = new OrderedSpecification(
							quantity, 
							orders.getLong("order_id"),
							product.getProductNumber(),
							product.getName(),
							customer.getEmail() 
					);
					
					Item item = WholesaleOrderFactory.createItem(customer,  
							orderedSpec);
					if (item == null) continue;
					itemRepository.save(item);

					ConfirmedSpecification confirmedSpec = new ConfirmedSpecification(
							orderItems.getLong("ab_id"), null, 
							new Amount(orderItems.getBigDecimal("orderitem_price"), Currency.EUR),
							customer.getAddress());
					HandlingEvent he = heService.confirm(item, quantity, confirmedSpec);
					if (he == null) continue;

					ShippedSpecification shippedSpec = new ShippedSpecification(orderItems.getLong("rechnung_id"));
					HandlingEvent he2 = heService.deliver(item, quantity, shippedSpec);
					if (he2 == null) continue;
					
					PayedSpecification payedSpec = new PayedSpecification(orderItems.getLong("bezahlt_id"));
					heService.complete(item, quantity, payedSpec);
					
				}
				stmt2.close();			
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.info("Import ended successfully");

	}

	private String getEmailOfOiCustomer(ResultSet orders) throws SQLException {
		String email = this.getSingleResult("SELECT email from "+PRIEBES_DB+".jos_users where id="+orders.getInt("user_id"), "email");
		return email;
	}

	private String getProductTitle(String product_id) throws SQLException {
		String title = this.getSingleResult("select title from "+PRIEBES_DB+".jos_k2_items where id="+product_id, "title");
		return title;
	}

	private boolean idOrderItemValid(ResultSet orderItems) {
		try {
			// Rules to Check:
			if (orderItems.getInt("orderitem_quantity")<1 ||
					getProductTitle(orderItems.getString("product_id")) == null
					){				
				return false;
			}
			else return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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
