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
			+ "order by min(ii.created) desc")
	Page<Long> getAllInvoiceNumbers(Customer customer, Pageable pageable);
	
	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "group by ii.invoiceNumber "
			+ "order by min(ii.created) desc")
	Page<Long> getAllInvoiceNumbers(Pageable pageable);
	
	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "group by ii.invoiceNumber "
			+ "order by min(ii.created) desc")
	List<Long> getAllInvoiceNumbers();
	
	@Query("select count(DISTINCT ii.invoiceNumber) "
			+ "from InvoiceItem ii")
	Long countAllInvoices();

	@Query("select ii.invoiceNumber "
			+ "from InvoiceItem ii "
			+ "where ii.customer = ?1 "
			+ "group by ii.invoiceNumber "
			+ "order by min(ii.created) desc")
	List<Long> getAllInvoiceNumbers(Customer customer);

}
