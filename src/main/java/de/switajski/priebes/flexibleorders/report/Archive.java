package de.switajski.priebes.flexibleorders.report;

import java.util.List;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;

public class Archive extends Report<ArchiveItem> {

	public Archive(List<ArchiveItem> items) {
		super(items);
	}

}
