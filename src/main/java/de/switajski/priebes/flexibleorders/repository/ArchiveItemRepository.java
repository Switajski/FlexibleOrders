package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = ArchiveItem.class)
public interface ArchiveItemRepository {
}
