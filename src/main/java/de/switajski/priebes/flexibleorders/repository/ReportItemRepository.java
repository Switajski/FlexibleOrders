package de.switajski.priebes.flexibleorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, Long>, JpaSpecificationExecutor<ReportItem>{

}
