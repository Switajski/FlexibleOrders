package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.service.conversion.ProductConversionService;
import de.switajski.priebes.flexibleorders.web.dto.MagentoApiProductResponseObject;

@Service
public class CatalogProductServiceImpl {

    private static final String URL = "http://switajski.de/api/rest/products";
    
    @Autowired
    private ProductConversionService productConversionService;

    public Page<CatalogProduct> findByKeyword(Pageable pageable, String query) {

        StringBuilder urlBuilder = new StringBuilder(URL);
        urlBuilder.append("?limit=").append(pageable.getPageSize());
        urlBuilder.append("&page=").append(pageable.getPageNumber());
        if (query != null) {
            if (StringUtils.containsWhitespace(query)) throw new IllegalArgumentException("Kann keine Leerzeichen parsen");
            urlBuilder.append("&filter[1][attribute]=name&filter[1][like]=%").append(query).append("%");
        }

        RestTemplate restTemplate = new RestTemplate();
        MagentoApiProductResponseObject productMap = restTemplate.getForObject(urlBuilder.toString(), MagentoApiProductResponseObject.class);
        List<CatalogProduct> products = new ArrayList<CatalogProduct>(productConversionService.convert(productMap.values()));
        return new PageImpl<CatalogProduct>(products);
    }

    public CatalogProduct findByProductNumber(Long productNumber) {
        StringBuilder urlBuilder = new StringBuilder(URL);
        urlBuilder.append("?filter[1][attribute]=sku&filter[1][eq]=").append(productNumber).append("");

        RestTemplate restTemplate = new RestTemplate();
        MagentoApiProductResponseObject productMap = restTemplate.getForObject(urlBuilder.toString(), MagentoApiProductResponseObject.class);
        
        if (productMap.size() != 1)
            return null;
        
        List<CatalogProduct> products = new ArrayList<CatalogProduct>(productConversionService.convert(productMap.values()));
        return products.iterator().next();
    }

}
