package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Entity;

@Entity
public class CancelReport extends Report {

    @Override
    public boolean hasConsecutiveDocuments() {
        return false;
    }

    protected CancelReport() {

    }

    public CancelReport(String documentNumber) {
        super(documentNumber);
    }

}
