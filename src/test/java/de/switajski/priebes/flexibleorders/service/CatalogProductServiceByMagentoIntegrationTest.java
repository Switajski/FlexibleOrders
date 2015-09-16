package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;

public class CatalogProductServiceByMagentoIntegrationTest {

    @Test
    public void shouldfindSomeProductByKeyword() {
        CatalogProductServiceByMagento catalogService = new CatalogProductServiceByMagento();
        Page<CatalogProduct> cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "Samson");

        assertThat(cpPage.getContent().isEmpty(), is(false));
    }

    @Test
    public void shouldFindByKeywordWithMultipleRequests() {
        CatalogProductServiceByMagento catalogService = new CatalogProductServiceByMagento();
        Page<CatalogProduct> cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "Samson");
        cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "Samsn");
        cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "asdf");
        cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "rbasdfg");
        cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "Samson");

        assertThat(cpPage.getContent().isEmpty(), is(false));
    }

    @Test
    public void shouldfindByKeywordWithWhiteSpaces() {
        CatalogProductServiceByMagento catalogService = new CatalogProductServiceByMagento();
        Page<CatalogProduct> cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "Samson Stars Lime");

        assertThat(cpPage.getContent().isEmpty(), is(false));
    }

    @Test
    public void shouldfindByKeywordWithNoResult() {
        CatalogProductServiceByMagento catalogService = new CatalogProductServiceByMagento();
        Page<CatalogProduct> cpPage = catalogService.findByKeyword(new PageRequest(0, 10), "asdfgasdfhb");

        assertThat(cpPage.getContent().isEmpty(), is(true));
    }

}
