package de.switajski.priebes.flexibleorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;

@Repository
public interface HandlingEventRepository extends JpaRepository<HandlingEvent, Long>, JpaSpecificationExecutor<HandlingEvent>{

}
