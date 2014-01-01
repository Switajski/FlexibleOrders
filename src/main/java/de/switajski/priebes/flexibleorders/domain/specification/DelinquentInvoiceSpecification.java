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
import de.switajski.priebes.flexibleorders.domain.Item;

public class DelinquentInvoiceSpecification implements Specification<Item>{
	
	private Date currentDate;
	
	public DelinquentInvoiceSpecification(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public boolean isSatisfiedBy(Item candidate, Customer customer){
		if (new ShippedSpecification().isSatisfiedBy(candidate)) return false;
		int gracePeriod = customer.getPaymentGracePeriod();

		for (HandlingEvent he: candidate.getDeliveryHistory().getAllHesOfType(HandlingEventType.SHIP)){
			if (he.getShippedSpec() != null && he.getShippedSpec().getDueDate() != null){
				Date firmDeadline = 
						DateUtils.addDays(he.getShippedSpec().getDueDate(), gracePeriod);
				return currentDate.after(firmDeadline);
			}
		}
		return false;
	}

	@Override
	public Predicate toPredicate(Root<Item> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
