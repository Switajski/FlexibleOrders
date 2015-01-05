package de.switajski.priebes.flexibleorders.service.api;

import org.joda.time.LocalDate;

public interface OrderNumberGeneratorService {

    public String generate(LocalDate date);
}
