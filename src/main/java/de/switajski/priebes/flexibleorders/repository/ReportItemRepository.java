package de.switajski.priebes.flexibleorders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.ReportItem;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, Long>, JpaSpecificationExecutor<ReportItem>{

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
	
	static String selectReportItem = "select ri ";

	static String andCustomer = " and ri.report.customerNumber = ?1";
	
	@Query(selectReportItem + toBeShippedQuery + andCustomer)
	Page<ReportItem> findAllToBeShippedByCustomerNumber(Long customerNumber, Pageable pageable);
	
	@Query(selectReportItem + toBeShippedQuery)
	Page<ReportItem> findAllToBeShipped(Pageable pageable);

	@Query(selectReportItem + toBeInvoicedQuery)
	Page<ReportItem> findAllToBeInvoiced(Pageable pageable);
	
	@Query(selectReportItem + toBeInvoicedQuery + andCustomer)
	Page<ReportItem> findAllToBeInvoicedByCustomer(Long customerNumber, Pageable pageable);
	
	@Query(selectReportItem + toBePaidQuery)
	Page<ReportItem> findAllToBePaid(Pageable pageable);
	
	@Query(selectReportItem + toBePaidQuery + andCustomer)
	Page<ReportItem> findAllToBePaidByCustomer(Long customerNumber, Pageable pageable);
	
	@Query(selectReportItem  + " from ReportItem ri where TYPE(ri) = ReceiptItem")
	Page<ReportItem> findAllCompleted(Pageable pageable);

	@Query(selectReportItem  + " from ReportItem ri where TYPE(ri) = ReceiptItem " + andCustomer)
	Page<ReportItem> findAllCompletedByCustomer(Long customerNumber, Pageable pageable);
	
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

	
	
}
