package de.switajski.priebes.flexibleorders.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class DeliveryHistory extends GenericEntity {

	@OneToMany
	private Set<HandlingEvent> handlingEvents;
	
	public Set<HandlingEvent> getHandlingEvents() {
		return handlingEvents;
	}

	public void setHandlingEvents(HashSet<HandlingEvent> handlingEvents) {
		this.handlingEvents = handlingEvents;
	}
	
	public List<HandlingEvent> getAllHesOfType(HandlingEventType type){
		List<HandlingEvent> hesOfType = new ArrayList<HandlingEvent>();
		for (HandlingEvent he:handlingEvents){
			if (he.getType().equals(type))
				hesOfType.add(he);
		}
		return hesOfType;
	}
	
}
