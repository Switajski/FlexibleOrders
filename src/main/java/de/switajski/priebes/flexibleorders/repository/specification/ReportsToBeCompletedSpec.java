package de.switajski.priebes.flexibleorders.repository.specification;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.Report;

public class ReportsToBeCompletedSpec implements Specification<Report>{

	@Override
	public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		Path<Set<ReceiptItem>> reportItems = root.get("items");
		
		return null;
	}
	
}
