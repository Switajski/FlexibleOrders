package de.switajski.priebes.flexibleorders.json;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class JsonFormatterConstants {

    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            DD_MM_YYYY);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

}
