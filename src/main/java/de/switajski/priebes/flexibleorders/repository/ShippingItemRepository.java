package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = ShippingItem.class)
public interface ShippingItemRepository 
extends JpaSpecificationExecutor<ShippingItem>, 
JpaRepository<ShippingItem, Long>,
ItemRepository<ShippingItem> {

	List<ShippingItem> findByOrderConfirmationNumberAndProduct(
			long orderConfirmationNumber, Product product);
	
	
	@Query("select i "
			+ "from ShippingItem i "
			+ "where i.quantityLeft != 0 ")
	Page<ShippingItem> findOpen(Pageable pageable);
	
	@Query("select i "
			+ "from ShippingItem i "
			+ "where i.quantityLeft != 0 "
			+ "order by i.created")
	List<ShippingItem> findOpen();


	@Query("select i "
			+ "from ShippingItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 "
			+ "order by i.created")
	List<ShippingItem> findByCustomerAndOpen(Customer customer);

	@Query("select i "
			+ "from ShippingItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 ")
	Page<ShippingItem> findByCustomerAndOpen(Customer customer,
			Pageable pageable);


	List<InvoiceItem> findByInvoiceNumberAndProduct(Object invoiceNumber,
			Product product);
}
