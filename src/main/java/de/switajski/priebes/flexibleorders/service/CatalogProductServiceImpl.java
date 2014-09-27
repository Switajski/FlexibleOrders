package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.exceptions.BusinessInputException;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;

@Service
public class CatalogProductServiceImpl {

	private CatalogProductRepository catalogProductRepo;

	@Autowired
	public CatalogProductServiceImpl(CatalogProductRepository catalogProductRepo) {
		this.catalogProductRepo = catalogProductRepo;
	}
	
	//TODO create by attributes, not by object
	@Transactional
	public CatalogProduct create(CatalogProduct product){
		return catalogProductRepo.save(product);
	}

	/**
	 * 
	 * @param productNumber
	 */
	@Transactional
	public void delete(Long productNumber) {
		CatalogProduct p = catalogProductRepo.findByProductNumber(productNumber);
		if (p == null)
			throw new BusinessInputException("Produktnr. nicht gefunden");
		catalogProductRepo.delete(p);
	}
	
	/**
	 * 
	 * @param productNumber
	 * @return
	 */
	@Transactional(readOnly = true)
	public Amount retrieveRecommendedPriceNet(Long productNumber) {
		CatalogProduct product = 
				catalogProductRepo.findByProductNumber(productNumber);
		if (product == null)
			//TODO: find a more suitable Exception - something like NotFoundException
			throw new BusinessInputException("Product with given productno. not found in catalog");
		if (product.getRecommendedPriceNet() == null)
			throw new BusinessInputException("Price of product with given productno. not set in catalog");
		return product.getRecommendedPriceNet();	
	}
}
