package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Comparator;

import org.junit.Test;

public class DocumentNumberInFormatComparatorTest {

    @Test
    public void shouldSortInDefinedDirection() {
        Comparator<String> comp = new DocumentNumberInFormatComparator();

        assertThat(comp.compare("R1604001", "AB1604001"), is(0));
        assertThat(comp.compare("R1604001", "AB1604002"), is(-1));
        assertThat(comp.compare("R1604001", "L1604002"), is(-1));
        assertThat(comp.compare("R1604001", "ZZZ1604002"), is(-1));
        assertThat(comp.compare("R1604002", "R1604001"), is(1));

        assertThat(comp.compare("123456", "R1604001"), is(-1));
        assertThat(comp.compare("R104001", "R1604001"), is(-1));
        assertThat(comp.compare("R1604001", "R123456"), is(1));
        assertThat(comp.compare("AVGHG", "R1604001"), is(-1));

    }
}
