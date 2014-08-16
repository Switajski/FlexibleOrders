package de.switajski.priebes.flexibleorders.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Carrier;

@Repository
public interface CarrierRepository extends JpaSpecificationExecutor<Carrier>, JpaRepository<Carrier, Long> {
	
	Carrier findByCarrierNumber(Long carrierNumber);
	
}
