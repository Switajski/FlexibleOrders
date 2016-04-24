package de.switajski.priebes.flexibleorders.service;

import java.time.LocalDateTime;
import java.util.Comparator;

public class DocumentNumberInFormatComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        boolean o1IsDigit = isDigit(o1.charAt(0));
        boolean o2IsDigit = isDigit(o2.charAt(0));

        if (o1IsDigit && o2IsDigit) return 0;
        if (o1IsDigit) return -1;
        if (o2IsDigit) return 1;

        Range i1 = firstNumbersInSuccession(o1);
        Range i2 = firstNumbersInSuccession(o2);

        if (i1.length() == 7 && i2.length() == 7) {

            LocalDateTime t1 = parseDate(o1, i1);
            LocalDateTime t2 = parseDate(o2, i2);

            int compareTime = t1.compareTo(t2);
            if (compareTime == 0) {
                return parseSeq(o1, i1).compareTo(parseSeq(o2, i2));
            }
            else return compareTime;
        }

        if (i1.length() != 7) return -1;
        if (i2.length() != 7) return 1;

        return 0;
    }

    private Integer parseSeq(String o1, Range i1) {
        return Integer.valueOf(o1.substring(i1.from + 4, i1.to + 1));
    }

    private LocalDateTime parseDate(String o1, Range r) {
        return LocalDateTime.of(
                Integer.valueOf("20" + o1.substring(r.from, r.from + 2)),
                Integer.valueOf(o1.substring(r.from + 2, r.from + 4)),
                1, 0, 0);
    }

    private Range firstNumbersInSuccession(String str) {
        Range r = new Range();
        for (int i = 0; i < str.length(); i++) {
            if (isDigit(str.charAt(i))) {
                if (r.from == null) r.from = i;
                r.to = i;
            }
            else if (r.from != null) {
                r.to = i;
                return r;
            }
        }
        return r;
    }

    private class Range {
        Integer from, to;

        int length() {
            if (to == null || from == null) return 0;
            return to - from + 1;
        }
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

}
