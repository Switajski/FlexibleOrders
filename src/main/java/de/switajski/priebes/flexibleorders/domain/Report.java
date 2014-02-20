package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Report extends GenericEntity{
	
	/**
	 * Natural id of a Report.
	 */
	@NotNull
	@Column(unique = true)
	private String documentNumber;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private Set<HandlingEvent> events = new HashSet<HandlingEvent>();
	
	protected Report() {}
	
	public Report(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public String getDocumentNumber() {
		return documentNumber;
	}

	public Set<HandlingEvent> getEvents() {
		return events;
	}
	
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public void addEvent(HandlingEvent handlingEvent){
		if (events.contains(handlingEvent)) return ;
		events.add(handlingEvent);
		handlingEvent.setReport(this);
	}
	
}
