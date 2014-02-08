package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Task extends GenericEntity{

	private boolean done;
	
	@OneToOne
	private Report report;

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
}
