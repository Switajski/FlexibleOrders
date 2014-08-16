package de.switajski.priebes.flexibleorders.service.helper;

import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.report.Report;

public abstract class ReportFilterHelper {

	public static List<Report> filter(List<Report> reports, Class<? extends Report> clazz){
		List<Report> returnedReports = new ArrayList<Report>();
		for (Report r: reports){
			if (clazz.isInstance(r))
				returnedReports.add(r);
		}
		return returnedReports;
	}
	
}
