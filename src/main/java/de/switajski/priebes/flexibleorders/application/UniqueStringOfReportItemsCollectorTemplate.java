package de.switajski.priebes.flexibleorders.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public abstract class UniqueStringOfReportItemsCollectorTemplate {

    public StringBuilder run(Collection<ReportItem> reportItems, StringBuilder s) {
        Set<String> uniqueStrings = new HashSet<String>();
        for (ReportItem ri : reportItems) {
            uniqueStrings.add(createString(ri));
        }

        Iterator<String> itr = uniqueStrings.iterator();
        while (itr.hasNext()) {
            s.append(itr.next());
            if (itr.hasNext()) s.append(", ");
        }

        return s;
    }

    public abstract String createString(ReportItem ri);

}
