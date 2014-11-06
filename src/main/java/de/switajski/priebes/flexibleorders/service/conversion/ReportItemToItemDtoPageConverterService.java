package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.helper.ItemDtoFilterHelper;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportItemToItemDtoPageConverterService {

	@Autowired
	private ItemDtoConverterService itemDtoConverterService;

	@Transactional(readOnly = true)
	public PageImpl<ItemDto> createWithWholeNonCompletedReports(
			PageRequest pageable,
			Page<ReportItem> reportItems) {
		List<ItemDto> convertedReportItems = itemDtoConverterService
				.convertReportItems(complementMissing(reportItems.getContent()));
		
		List<ItemDto> filtered = ItemDtoFilterHelper
				.filterQtyLeftZero(convertedReportItems);
		
		PageImpl<ItemDto> reportItemPage = createPage(
				reportItems.getTotalElements(),
				pageable,
				filtered);
		return reportItemPage;
	}

	@Transactional(readOnly = true)
	public PageImpl<ItemDto> createWithWholeReports(PageRequest pageable,
			Page<ReportItem> reportItems) {
		List<ItemDto> convertedReportItems = itemDtoConverterService
				.convertReportItems(complementMissing(reportItems.getContent()));
		
		PageImpl<ItemDto> reportItemPage = createPage(
				reportItems.getTotalElements(),
				pageable,
				convertedReportItems);
		return reportItemPage;
	}

	private List<ReportItem> complementMissing(
			List<ReportItem> reportItems) {

		List<ReportItem> allReportItems = new ArrayList<ReportItem>();
		for (Report report : getReportsByReportItems(reportItems))
			allReportItems.addAll(report.getItems());

		return allReportItems;
	}

	private Set<Report> getReportsByReportItems(List<ReportItem> reportItems) {
		if (reportItems.isEmpty())
			return Collections.<Report> emptySet();

		Class<?> type = reportItems.iterator().next().getClass();

		Set<Report> reportsToBeComplemented = new HashSet<Report>();
		for (ReportItem ri : reportItems) {
			if (!type.isInstance(ri))
				throw new IllegalStateException(
						"ReportItems to complement have different types");
			reportsToBeComplemented.add(ri.getReport());
		}
		return reportsToBeComplemented;
	}

	public PageImpl<ItemDto> createPage(Long totalElements,
			Pageable pageable, List<ItemDto> ris) {
		return new PageImpl<ItemDto>(ris, pageable, totalElements);
	}
	
}
