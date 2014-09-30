package de.switajski.priebes.flexibleorders.json;

import java.text.SimpleDateFormat;

import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

public class JsonFormatterConstants {

    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            DD_MM_YYYY);
    
    public static final DateTimeFormatter JODA_DATE_FORMAT = new DateTimeFormatterFactory(DD_MM_YYYY).createDateTimeFormatter();
    
}
