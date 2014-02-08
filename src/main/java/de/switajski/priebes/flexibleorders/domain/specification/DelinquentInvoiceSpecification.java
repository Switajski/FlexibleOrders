package de.switajski.priebes.flexibleorders.domain.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class DelinquentInvoiceSpecification implements Specification<OrderItem>{
	
	private Date currentDate;
	
	public DelinquentInvoiceSpecification(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public boolean isSatisfiedBy(OrderItem candidate, Customer customer){
		if (new ShippedSpecification(false, false).isSatisfiedBy(candidate)) return false;
		Integer gracePeriod = customer.getPaymentGracePeriod();
		if (gracePeriod == null) gracePeriod = 0;

		for (HandlingEvent he: candidate.getAllHesOfType(HandlingEventType.SHIP)){
				Date firmDeadline = 
						DateUtils.addDays(he.getInvoice().getEvaluationDate(), gracePeriod);
				return currentDate.after(firmDeadline);
		}
		return false;
	}

	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
