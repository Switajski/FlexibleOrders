package de.switajski.priebes.flexibleorders.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.web.dto.MagentoApiProductResponseObject;
import de.switajski.priebes.flexibleorders.web.dto.MagentoProductDto;

@Service
public class CatalogProductServiceByMagento {

    private static Logger log = Logger.getLogger(CatalogProductServiceByMagento.class);

    private static final String URL = "http://priebes.eu/catalog/api/rest/products";

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
            log.error(e);
        }
        List<CatalogProduct> products = new ArrayList<CatalogProduct>(convert(productMap.values()));
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
                if (product.sku.equals("0" + productNumber)) return convert(Arrays.asList(product)).iterator().next();
            }
        }

        List<CatalogProduct> products = new ArrayList<CatalogProduct>(convert(productMap.values()));
        return products.iterator().next();
    }

    protected Set<CatalogProduct> convert(Collection<MagentoProductDto> magentoProducts) {
        Set<CatalogProduct> products = new HashSet<CatalogProduct>();
        for (MagentoProductDto mProduct : magentoProducts) {
            CatalogProduct product = new CatalogProduct();
            product.setName(mProduct.name);
            // TODO: setProductNumber to string
            product.setProductNumber(mProduct.sku);
            product.setProductType(ProductType.PRODUCT);
            product.setRecommendedPriceNet(new Amount(new BigDecimal(mProduct.regular_price_without_tax), Currency.EUR));
            product.setId(Long.valueOf(mProduct.entity_id));
            products.add(product);
        }
        return products;
    }

}
