package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.report.Invoice;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;

@Service
@Transactional(readOnly=true)
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	InvoiceItemRepository invoiceItemRepository;
	
	@Override
	public long countAll() {
		return invoiceItemRepository.countAllInvoices();
	}
	
	@Override
	public Page<Long> getInvoiceNumbersByCustomer(Customer customer, Pageable pageable) {
		return invoiceItemRepository.getAllInvoiceNumbers(customer, pageable);
	}

	public Page<Invoice> findAll(Pageable pageable){
		Page<Long> invoiceNumbers = invoiceItemRepository.getAllInvoiceNumbers(pageable);
		
		List<Invoice> orders = new ArrayList<Invoice>();
		for (Long invoiceNumber:invoiceNumbers){
			orders.add(new Invoice(invoiceItemRepository.findByInvoiceNumber(invoiceNumber)));
		}
		Page<Invoice> invoicesPage = new PageImpl<Invoice>(orders, pageable, invoiceNumbers.getTotalElements());
		return invoicesPage;
		
	}

	@Override
	public Invoice find(Long invoiceNumber) {
		return getInvoice(invoiceNumber);
	}
	
	private Invoice getInvoice(Long invoiceNumber){
		List<InvoiceItem> ois = invoiceItemRepository.findByOrderNumber(invoiceNumber);
		Invoice invoice = new Invoice(ois);
		return invoice;
	}

	@Override
	public List<Invoice> findAll() {
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		for (Long orderNumber: invoiceItemRepository.getAllInvoiceNumbers())
			invoices.add(getInvoice(orderNumber));
		return invoices;
	}

	@Override
	public void save(Invoice t) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void delete(Invoice t) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void update(Invoice t) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public List<Invoice> findByCustomer(Customer customer) {
		ArrayList<Invoice> orders = new ArrayList<Invoice>();
		for (Long invoiceNumber: invoiceItemRepository.getAllInvoiceNumbers(customer))
			orders.add(getInvoice(invoiceNumber));
		return orders;
	}

	@Override
	public Page<Invoice> findByCustomer(Customer customer, Pageable pageable) {
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		Page<Long> invoiceNumbers = invoiceItemRepository.getAllInvoiceNumbers(customer, pageable);
		for (Long invoiceNumber: invoiceNumbers)
			invoices.add(getInvoice(invoiceNumber));
		Page<Invoice> pages = new PageImpl<Invoice>(invoices, pageable, invoiceNumbers.getSize());
		return pages;
	}

	@Override
	public List<Long> findInvoiceNumbersLike(Long invoiceNumber) {
		ArrayList<Long> orderNumbersToReturn = new ArrayList<Long>();
		List<Long> orderNumbers = invoiceItemRepository.getAllInvoiceNumbers();
		for (Long on:orderNumbers){
			if (on.toString().startsWith(invoiceNumber.toString()))
				orderNumbersToReturn.add(on);
		}
		return orderNumbersToReturn;
	}

}
