package de.switajski.priebes.flexibleorders.service.api;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.repository.specification.OrderCreatedBetweenSpecification;

@Service
public class OrderNumberGeneratorService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    ReportRepository reportRepo;

    public String yymmggg(LocalDate date) {
        return generateNextYYMMGGGFor(date, true, true);
    }

    public String generateNextYYMMGGGFor(LocalDate date, boolean inOrders, boolean inReports) {
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Specification<Order> spec = new OrderNumberStartsWith("B" + yymm(date));

        int lastDocumentNumber = 0;
        if (inOrders) {
            // Using Page, because it's the less effort for sort direction and
            // result size
            Page<Order> orders = orderRepo.findAll(spec, new PageRequest(0, 1, Sort.Direction.DESC, "orderNumber"));
            if (0 < orders.getContent().size()) {
                lastDocumentNumber = parseLastThreeNumbers(orders.iterator().next().getOrderNumber());
            }
        }
        if (inReports) {
            Page<Report> reports = reportRepo.findAll(new DocumentNumberContains(yymm(date)), new PageRequest(0, 1, Sort.Direction.DESC, "documentNumber"));
            if (0 < reports.getContent().size()) {
                int lastThreeNumbersParsed = parseLastThreeNumbers(reports.iterator().next().getDocumentNumber());
                if (lastThreeNumbersParsed > lastDocumentNumber) {
                    lastDocumentNumber = lastThreeNumbersParsed;
                }
            }
        }

        int generated = lastDocumentNumber + 1;

        return new StringBuilder().append(year.toString().substring(2))
                .append(("00" + month).substring(month.toString().length()))
                .append(("000" + generated).substring(new Integer(generated).toString().length()))
                .toString();

    }

    private int parseLastThreeNumbers(String orderNumber) {
        String numbers = orderNumber.replaceAll("[\\D]", "");
        String lastThreeNumbers = numbers.substring(Math.max(0, numbers.length() - 3));
        int parsedLastThreeNumbers = Integer.parseInt(lastThreeNumbers);
        return parsedLastThreeNumbers;
    }

    public String byymmggg(LocalDate date) {
        return "B" + generateNextYYMMGGGFor(date, true, false);
    }

    @SuppressWarnings("unused")
    private Specification<Order> inMonthAndStartsWithLetterB(LocalDate date) {
        return where(OrderCreatedBetweenSpecification.inMonth(date))
                .and(new OrderNumberStartsWith("B"));
    }

    private String yymm(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("yyMM").format(localDate);
    }

    public class OrderNumberStartsWith implements Specification<Order> {

        private String start;

        public OrderNumberStartsWith(String start) {
            this.start = start;
        }

        @Override
        public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("orderNumber"), start + "%");
        }

    }

    public class DocumentNumberContains implements Specification<Report> {

        private String pattern;

        public DocumentNumberContains(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("documentNumber"), "%" + pattern + "%");
        }

    }

    public class OrderNumberContains implements Specification<Order> {

        private String pattern;

        public OrderNumberContains(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("orderNumber"), "%" + pattern + "%");
        }

    }

}
