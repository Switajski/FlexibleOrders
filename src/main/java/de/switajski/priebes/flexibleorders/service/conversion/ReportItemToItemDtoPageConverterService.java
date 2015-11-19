package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
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
                .convertReportItems(reportItems.getContent());

        PageImpl<ItemDto> reportItemPage = createPage(
                reportItems.getTotalElements(),
                pageable,
                convertedReportItems);
        return reportItemPage;
    }

    public PageImpl<ItemDto> createPage(Long totalElements,
            Pageable pageable, List<ItemDto> ris) {
        return new PageImpl<ItemDto>(ris, pageable, totalElements);
    }

}
