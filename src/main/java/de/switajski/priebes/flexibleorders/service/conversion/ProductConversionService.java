package de.switajski.priebes.flexibleorders.service.conversion;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.web.dto.MagentoProductDto;

@Service
public class ProductConversionService {

    public Set<CatalogProduct> convert(Collection<MagentoProductDto> magentoProducts){
        Set<CatalogProduct> products = new HashSet<CatalogProduct>();
        for (MagentoProductDto mProduct:magentoProducts){
            CatalogProduct product = new CatalogProduct();
            product.setName(mProduct.name);
            //TODO: setProductNumber to string
            product.setProductNumber(mProduct.sku);
            product.setProductType(ProductType.PRODUCT);
            product.setRecommendedPriceNet(new Amount(new BigDecimal(mProduct.regular_price_without_tax), Currency.EUR));
            product.setId(Long.valueOf(mProduct.entity_id));
            products.add(product);
        }
        return products;
    }
}
