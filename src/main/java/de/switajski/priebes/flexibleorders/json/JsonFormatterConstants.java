package de.switajski.priebes.flexibleorders.json;

import java.text.SimpleDateFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JsonFormatterConstants {

    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            DD_MM_YYYY);
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");
    
}
