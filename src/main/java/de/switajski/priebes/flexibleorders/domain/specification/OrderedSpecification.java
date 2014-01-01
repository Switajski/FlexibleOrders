package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Item;


/**
 * 
 * @author Marek Switajski
 *
 */
@Embeddable
public class OrderedSpecification implements Specification<Item>{

	private Integer orderedQuantity;
	
	private Long orderNumber;
	
	private Long productNumber;
	
	private String productName;
	
	private String customerEmail;
	
	public OrderedSpecification() {}
	
	/**
	 * Constructor with minimum attributes needed for a valid order
	 * @param orderedQuantity
	 * @param orderNumber
	 * @param productNumber
	 * @param productName
	 * @param customerEmail
	 */
	public OrderedSpecification(Integer orderedQuantity, 
			Long orderNumber,
			Long productNumber,
			String productName,
			String customerEmail) {
		this.orderedQuantity = orderedQuantity;
		this.orderNumber = orderNumber;
		this.productNumber = productNumber;
		this.productName = productName;
		this.customerEmail = customerEmail;
	}
	
	public boolean isSatisfiedBy(Item item){
		if (item.getOrderedSpecification().getQuantity() == null || 
				item.getOrderedSpecification().getQuantity() < 0) return false;
		if (item.getOrderedSpecification().getOrderNumber() == null) return false;
		if (item.getOrderedSpecification().getProductName() == null) return false;
		if (item.getOrderedSpecification().getProductNumber() == null) return false;
		if (item.getOrderedSpecification().getCustomerEmail() == null
				&& item.getCustomer() == null) return false;
		
		int summedQuan = 0;
		for (HandlingEvent orderEvent: item.getDeliveryHistory().getHandlingEvents()){
			if (orderEvent.getType() == HandlingEventType.ORDER)
				summedQuan += orderEvent.getQuantity();
		}
		if (orderedQuantity == summedQuan)
			return true;
		return false;
	}
	
	@Override
	public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		Predicate orderedPred = cb.and(
				cb.greaterThan(root.get("quantity").as(Integer.class), 0),
				cb.isNotNull(root.get("orderNumber")),
				cb.isNotNull(root.get("productName")),
				cb.isNotNull(root.get("productNumber")),
				cb.isNotNull(root.get("customerEmail"))
				);
		return orderedPred;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(Long productNumber) {
		this.productNumber = productNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Integer getQuantity() {
		return orderedQuantity;
	}

	public void setQuantity(Integer quantity) {
		this.orderedQuantity = quantity;
	}

	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

}
