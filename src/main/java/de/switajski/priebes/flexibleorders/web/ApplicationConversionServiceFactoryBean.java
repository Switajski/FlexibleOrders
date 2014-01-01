package de.switajski.priebes.flexibleorders.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.ItemServiceImpl;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	private static Logger log = Logger.getLogger(ApplicationConversionServiceFactoryBean.class);
	
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
//		registry.addConverter(getJSONCollectionToListConverter());
		//TODO: centralized JSON-Converter
	}
	
    @Autowired
    CustomerService customerService;
    
    @Autowired
    CustomerRepository customerRepo;
    
    @Autowired
    ItemServiceImpl itemService;
    
    @Autowired
    ItemRepository itemRepo;
    
    @Autowired
    CatalogProductRepository productService;
    
    public Converter<Customer, String> getCustomerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.Customer, java.lang.String>() {
            public String convert(Customer customer) {
                return new StringBuilder().append(customer.getShortName()).append(' ').append(customer.getFirstName()).append(' ').append(customer.getLastName()).append(' ').append(customer.getAddress().getStreet()).toString();
            }
        };
    }
    
    public Converter<Long, Customer> getIdToCustomerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.Customer>() {
            public de.switajski.priebes.flexibleorders.domain.Customer convert(java.lang.Long id) {
                return customerRepo.findOne(id);
            }
        };
    }
    
    public Converter<String, Customer> getStringToCustomerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.Customer>() {
            public de.switajski.priebes.flexibleorders.domain.Customer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Customer.class);
            }
        };
    }
    
    public Converter<Item, String> getItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.Item, java.lang.String>() {
            public String convert(Item orderItem) {
                return new StringBuilder().append(orderItem.getCreated()).append(' ').append(orderItem.getQuantity()).append(' ').append(' ').append(orderItem.getProductName()).toString();
            }
        };
    }
    
    public Converter<Long, Item> getIdToItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.Item>() {
            public de.switajski.priebes.flexibleorders.domain.Item convert(java.lang.Long id) {
                return itemRepo.findOne(id);
            }
        };
    }
    
    public Converter<String, Item> getStringToOrderItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.Item>() {
            public de.switajski.priebes.flexibleorders.domain.Item convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Item.class);
            }
        };
    }
    
    public Converter<Product, String> getProductToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.Product, java.lang.String>() {
            public String convert(Product product) {
                return new StringBuilder().append(product.getProductNumber()).append(' ').append(product.getName()).toString();
            }
        };
    }
    
//    public Converter<Long, Product> getIdToProductConverter() {
//        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.Product>() {
//            public de.switajski.priebes.flexibleorders.domain.Product convert(java.lang.Long id) {
//                return productService.findProduct(id);
//            }
//        };
//    }
    
    public Converter<String, Product> getStringToProductConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.Product>() {
            public de.switajski.priebes.flexibleorders.domain.Product convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Product.class);
            }
        };
    }
    
    public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getCustomerToStringConverter());
        registry.addConverter(getIdToCustomerConverter());
        registry.addConverter(getStringToCustomerConverter());
        registry.addConverter(getStringToOrderItemConverter());
        registry.addConverter(getProductToStringConverter());
        registry.addConverter(getStringToProductConverter());
    }
    
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
	
}
