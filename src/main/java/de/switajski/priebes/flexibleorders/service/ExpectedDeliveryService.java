package de.switajski.priebes.flexibleorders.service;

import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class ExpectedDeliveryService {

	public Set<LocalDate> retrieve(Set<ReportItem> items) {
		throw new NotImplementedException();
	}

}
