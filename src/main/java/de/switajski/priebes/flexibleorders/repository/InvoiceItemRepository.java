package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Product;

@Repository
@RooJpaRepository(domainType = InvoiceItem.class)
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long>, 
	JpaSpecificationExecutor<InvoiceItem>,
	ItemRepository<InvoiceItem>{
	
	/**
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "where ii.customer = ?1 "
			+ "group by ii.invoiceNumber "
			+ "order by max(ii.created)")
	Page<Long> getAllInvoiceNumbers(Customer customer, Pageable pageable);
	
	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "group by ii.invoiceNumber "
			+ "order by max(ii.created)")
	Page<Long> getAllInvoiceNumbers(Pageable pageable);
	
	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "group by ii.invoiceNumber "
			+ "order by max(ii.created)")
	List<Long> getAllInvoiceNumbers();
	
	@Query("select count(DISTINCT ii.invoiceNumber) "
			+ "from InvoiceItem ii")
	Long countAllInvoices();

	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "where ii.customer = ?1 "
			+ "group by ii.invoiceNumber "
			+ "order by ii.created")
	List<Long> getAllInvoiceNumbers(Customer customer);
	
	@Query("select i "
			+ "from InvoiceItem i "
			+ "where i.quantityLeft != 0 ")
	Page<InvoiceItem> findOpen(Pageable pageable);

	@Query("select i "
			+ "from InvoiceItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = :customer "
			+ "order by i.created")
	List<InvoiceItem> findByCustomerAndOpen(Customer customer);
	
	@Query("select i "
			+ "from InvoiceItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 ")
	Page<InvoiceItem> findByCustomerAndOpen(Customer customer, Pageable pageable);

	List<InvoiceItem> findByInvoiceNumberAndProduct(long invoiceNr,
			Product product);

}
