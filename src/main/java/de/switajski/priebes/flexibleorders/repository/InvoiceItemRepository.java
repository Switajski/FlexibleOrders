package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = InvoiceItem.class)
public interface InvoiceItemRepository {
}
