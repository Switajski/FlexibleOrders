package de.switajski.priebes.flexibleorders.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.InvoiceItemBuilder;


public class InvoiceItemIntegrationTest extends AbstractIntegrationTest<InvoiceItem>{

	@Autowired
	private InvoiceItemRepository iiRepo;

	@Override
	protected InvoiceItem createEntity() {
		return new InvoiceItemBuilder().create();
	}

	@Override
	protected JpaRepository<InvoiceItem, Long> getRepository() {
		return iiRepo;
	}
	
	
}
