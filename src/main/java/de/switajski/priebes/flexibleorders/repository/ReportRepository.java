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
				+ "(SELECT sum(confirmEvent.quantity) from ConfirmationItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem)"
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ShippingItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem) "
			+ ")"
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllToBeShippedByCustomerNumber(Long customerNumber, Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ConfirmationItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ShippingItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem) "
			+ ")")
	Page<Report> findAllToBeShipped(Pageable pageable);
	

	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(invoiceItem.quantity) from InvoiceItem invoiceItem "
				+ "where invoiceItem.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(receiptItem.quantity),0) from ReceiptItem receiptItem "
				+ "where receiptItem.orderItem = ri.orderItem) "
			+ ")"
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllToBePaidByCustomer(Long customerNumber, Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(invoiceItem.quantity) from InvoiceItem invoiceItem "
				+ "where invoiceItem.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(receiptItem.quantity),0) from ReceiptItem receiptItem "
				+ "where receiptItem.orderItem = ri.orderItem) "
			+ ")")
	Page<Report> findAllToBePaid(Pageable pageable);
	
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where TYPE(ri) = ReceiptItem "
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllCompletedByCustomer(Long customerNumber, Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where TYPE(ri) = ReceiptItem")
	Page<Report> findAllCompleted(Pageable pageable);

	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(shippingItem.quantity) from ShippingItem shippingItem "
				+ "where shippingItem.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(invoiceItem.quantity),0) from InvoiceItem invoiceItem "
				+ "where invoiceItem.orderItem = ri.orderItem)"
			+ ")")
	Page<Report> findAllToBeInvoiced(Pageable pageable);
	
	@Query("SELECT distinct(ri.report) from ReportItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(shippingItem.quantity) from ShippingItem shippingItem "
				+ "where shippingItem.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(invoiceItem.quantity),0) from InvoiceItem invoiceItem "
				+ "where invoiceItem.orderItem = ri.orderItem)"
			+ ")"
			+ "and ri.report.customerNumber = ?1")
	Page<Report> findAllToBeInvoicedByCustomer(Long customerNumber, Pageable pageable);
	
}
