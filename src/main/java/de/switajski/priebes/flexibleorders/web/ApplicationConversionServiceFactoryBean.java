package de.switajski.priebes.flexibleorders.web;

import org.apache.log4j.Logger;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

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
	
}
