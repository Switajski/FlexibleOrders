package de.switajski.priebes.flexibleorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.report.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, String>, JpaSpecificationExecutor<Report>{

	Report findByDocumentNumber(String documentNumber);

}
