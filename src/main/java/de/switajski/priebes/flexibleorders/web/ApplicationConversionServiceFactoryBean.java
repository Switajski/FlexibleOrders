package de.switajski.priebes.flexibleorders.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}
	
	public Converter<OrderItem, String> getOrderItemToJsonConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.ArchiveItem, java.lang.String>() {
            public String convert(ArchiveItem archiveItem) {
                return new StringBuilder().append(archiveItem.getCreated()).append(' ').append(archiveItem.getQuantity()).append(' ').append(archiveItem.getPriceNet()).append(' ').append(archiveItem.getProductName()).toString();
            }
        };
    }
}
