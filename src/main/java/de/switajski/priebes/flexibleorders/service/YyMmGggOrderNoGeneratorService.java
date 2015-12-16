package de.switajski.priebes.flexibleorders.service;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.specification.OrderCreatedBetweenSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.OrderNumberStartsWith;
import de.switajski.priebes.flexibleorders.service.api.OrderNumberGeneratorService;

@Service
public class YyMmGggOrderNoGeneratorService implements OrderNumberGeneratorService {

    @Autowired
    OrderRepository orderRepo;

    @Override
    public String generate(LocalDate date) {
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Specification<Order> spec =
                where(OrderCreatedBetweenSpecification.inMonth(date))
                        .and(new OrderNumberStartsWith("B"));

        int lastOrderNumber = 0;
        Page<Order> orders = orderRepo.findAll(spec, new PageRequest(0, 1, Sort.Direction.DESC, "orderNumber"));
        if (0 < orders.getContent().size()) {
            String numbers = orders.iterator().next().getOrderNumber().replaceAll("[\\D]", "");
            String lastThreeNumbers = numbers.substring(Math.max(0, numbers.length() - 3));
            lastOrderNumber = Integer.parseInt(lastThreeNumbers);
        }

        int generated = lastOrderNumber + 1;

        return new StringBuilder()
                .append("B")
                .append(year.toString().substring(2))
                .append(("00" + month).substring(month.toString().length()))
                .append(("000" + generated).substring(new Integer(generated).toString().length())).toString();
    }

}
