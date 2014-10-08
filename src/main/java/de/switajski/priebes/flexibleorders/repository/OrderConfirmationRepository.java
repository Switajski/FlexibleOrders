package de.switajski.priebes.flexibleorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;

@Repository
public interface OrderConfirmationRepository extends JpaRepository<OrderConfirmation, String>, JpaSpecificationExecutor<Report>{

	OrderConfirmation findByOrderAgreementNumber(String orderAgreementNumber);

    OrderConfirmation findByDocumentNumber(String orderConfirmationNo);

}
