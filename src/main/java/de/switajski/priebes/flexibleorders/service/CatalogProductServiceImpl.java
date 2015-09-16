package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.service.conversion.ProductConversionService;
import de.switajski.priebes.flexibleorders.web.dto.MagentoApiProductResponseObject;
import de.switajski.priebes.flexibleorders.web.dto.MagentoProductDto;

@Service
public class CatalogProductServiceImpl {

    private static final String URL = "http://priebes.eu/catalog/api/rest/products";

    @Autowired
    private ProductConversionService productConversionService;

    public Page<CatalogProduct> findByKeyword(Pageable pageable, String query) {

        StringBuilder urlBuilder;
        urlBuilder = new StringBuilder(URL)
                .append("?limit=").append(pageable.getPageSize())
                .append("&page=").append(pageable.getPageNumber())
                .append("&filter[1][attribute]=name&filter[1][like]=%")
                .append(query).append("%");

        MagentoApiProductResponseObject productMap = new MagentoApiProductResponseObject();
        try {
            productMap = new RestTemplate().getForObject(urlBuilder.toString(), MagentoApiProductResponseObject.class);
        }
        catch (HttpMessageNotReadableException e) {
            // If magento reponds with no results, it will throw a JSON
            // ParseException
        }
        List<CatalogProduct> products = new ArrayList<CatalogProduct>(productConversionService.convert(productMap.values()));
        return new PageImpl<CatalogProduct>(products);
    }

    public CatalogProduct findByProductNumber(String productNumber) {

        StringBuilder urlBuilder = new StringBuilder(URL);
        urlBuilder.append("?filter[1][attribute]=sku&filter[1][like]=%").append(productNumber);

        RestTemplate restTemplate = new RestTemplate();
        MagentoApiProductResponseObject productMap = restTemplate.getForObject(urlBuilder.toString(), MagentoApiProductResponseObject.class);

        // Workaround for ExtJs erazing leading zeros
        if (productMap.size() > 1) {
            for (MagentoProductDto product : productMap.values()) {
                if (product.sku.equals("0" + productNumber)) return productConversionService.convert(Arrays.asList(product)).iterator().next();
            }
        }

        List<CatalogProduct> products = new ArrayList<CatalogProduct>(productConversionService.convert(productMap.values()));
        return products.iterator().next();
    }

}
