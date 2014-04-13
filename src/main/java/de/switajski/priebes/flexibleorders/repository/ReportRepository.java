package de.switajski.priebes.flexibleorders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, String>, JpaSpecificationExecutor<Report>{

	Report findByDocumentNumber(String documentNumber);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CONFIRM) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP)"
			+ ")"
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllToBeShippedByCustomerNumber(Long customerNumber, Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CONFIRM) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP)"
			+ ")")
	Page<Report> findAllToBeShipped(Pageable pageable);
	
	
	
	
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.INVOICE) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID)"
			+ ")"
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllToBePaidByCustomer(Long customerNumber, Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.INVOICE) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID)"
			+ ")")
	Page<Report> findAllToBePaid(Pageable pageable);
	
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where ri.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID "
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllCompletedByCustomer(Long customerNumber, Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where ri.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID")
	Page<Report> findAllCompleted(Pageable pageable);

	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.INVOICE)"
			+ ")")
	Page<Report> findAllToBeInvoiced(Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.INVOICE)"
			+ ")"
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllToBeInvoicedByCustomer(Long customerNumber, Pageable pageable);
	
}
