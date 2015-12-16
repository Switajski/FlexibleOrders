package de.switajski.priebes.flexibleorders.service.api;

import java.time.LocalDate;

public interface OrderNumberGeneratorService {

    public String generate(LocalDate date);
}
