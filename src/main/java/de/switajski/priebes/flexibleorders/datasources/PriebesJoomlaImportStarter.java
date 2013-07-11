package de.switajski.priebes.flexibleorders.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;


public class PriebesJoomlaImportStarter {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/applicationContext.xml");

		CustomerRepository channel = (CustomerRepository) context.getBean("CustomerRepository");

		
		PriebesJoomlaImport pji;
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connect = (Connection) DriverManager
					.getConnection("jdbc:mysql://localhost/bestellsystemv2?"
							+ "user=root&password=&useUnicode=yes&characterEncoding=UTF-8");
			pji = new PriebesJoomlaImport();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
