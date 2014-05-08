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

	static String toBeShippedQuery = " from ConfirmationItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(confirmEvent.quantity) from ConfirmationItem confirmEvent "
				+ "where confirmEvent.orderItem = ri.orderItem)"
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ShippingItem shipEvent "
				+ "where shipEvent.orderItem = ri.orderItem) "
			+ ")";
	
	static String toBeInvoicedQuery = " from ShippingItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(shippingItem.quantity) from ShippingItem shippingItem "
				+ "where shippingItem.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(invoiceItem.quantity),0) from InvoiceItem invoiceItem "
				+ "where invoiceItem.orderItem = ri.orderItem)"
			+ ")";
	
	static String toBePaidQuery = " from InvoiceItem ri where "
			+ "ri IN "
			+ "(SELECT he from ReportItem he where he.orderItem = ri.orderItem and "
				+ "(SELECT sum(invoiceItem.quantity) from InvoiceItem invoiceItem "
				+ "where invoiceItem.orderItem = ri.orderItem) "
				+ " > "
				+ "(SELECT coalesce(sum(receiptItem.quantity),0) from ReceiptItem receiptItem "
				+ "where receiptItem.orderItem = ri.orderItem) "
			+ ")";
	
	static String toBeCompletedQuery = " from ReportItem ri where TYPE(ri) = ReceiptItem";
	
	static String selectDistictReport = "select distinct(ri.report) ";

//	static String selectCountDistictReport = "select count(distinct(ri.report)) "; 
	
	static String andCustomer = " and ri.report.customerNumber = ?1";
	
	@Query(selectDistictReport + toBeShippedQuery + andCustomer)
	Page<Report> findAllToBeShippedByCustomerNumber(Long customerNumber, Pageable pageable);
	
	@Query(selectDistictReport + toBeShippedQuery)
	Page<Report> findAllToBeShipped(Pageable pageable);

	@Query(selectDistictReport + toBeInvoicedQuery)
	Page<Report> findAllToBeInvoiced(Pageable pageable);
	
	@Query(selectDistictReport + toBeInvoicedQuery + andCustomer)
	Page<Report> findAllToBeInvoicedByCustomer(Long customerNumber, Pageable pageable);
	
	@Query(selectDistictReport + toBePaidQuery)
	Page<Report> findAllToBePaid(Pageable pageable);
	
	@Query(selectDistictReport + toBePaidQuery + andCustomer)
	Page<Report> findAllToBePaidByCustomer(Long customerNumber, Pageable pageable);
	
	@Query(selectDistictReport + toBeCompletedQuery)
	Page<Report> findAllCompleted(Pageable pageable);

	@Query(selectDistictReport + toBeCompletedQuery + andCustomer)
	Page<Report> findAllCompletedByCustomer(Long customerNumber, Pageable pageable);
	
//	@Query(selectCountDistictReport + toBeShippedQuery + andCustomer)
//	Long countAllToBeShippedByCustomerNumber(Long customerNumber, Pageable pageable);
//	
//	@Query(selectCountDistictReport + toBeShippedQuery)
//	Long countAllToBeShipped(Pageable pageable);
//
//	@Query(selectCountDistictReport + toBeInvoicedQuery)
//	Long countAllToBeInvoiced(Pageable pageable);
//	
//	@Query(selectCountDistictReport + toBeInvoicedQuery + andCustomer)
//	Long countAllToBeInvoicedByCustomer(Long customerNumber, Pageable pageable);
//	
//	@Query(selectCountDistictReport + toBePaidQuery)
//	Long countAllToBePaid(Pageable pageable);
//	
//	@Query(selectCountDistictReport + toBePaidQuery + andCustomer)
//	Long countAllToBePaidByCustomer(Long customerNumber, Pageable pageable);
//	
//	@Query(selectCountDistictReport + toBeCompletedQuery)
//	Long countAllCompleted(Pageable pageable);
//
//	@Query(selectCountDistictReport + toBeCompletedQuery + andCustomer)
//	Long countAllCompletedByCustomer(Long customerNumber, Pageable pageable);


	Report findByDocumentNumber(String documentNumber);
}
