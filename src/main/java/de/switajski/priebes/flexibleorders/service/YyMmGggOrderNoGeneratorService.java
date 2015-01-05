package de.switajski.priebes.flexibleorders.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.specification.OrderCreatedBetweenSpecification;
import de.switajski.priebes.flexibleorders.service.api.OrderNumberGeneratorService;

@Service
public class YyMmGggOrderNoGeneratorService implements OrderNumberGeneratorService {

    @Autowired
    OrderRepository orderRepo;
    
    @Override
    public String generate(LocalDate date) {
        Integer year = date.getYear();
        Integer month = date.getMonthOfYear();
        Long generated = orderRepo.count(OrderCreatedBetweenSpecification.inMonth(date));
        generated = generated + 1;
        return new StringBuilder()
            .append("B")
            .append(year.toString().substring(2))
            .append(("00" + month).substring(month.toString().length()))
            .append(("000" + generated).substring(generated.toString().length())).toString();
    }

}
