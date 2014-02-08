package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;

@Service
public class CatalogProductServiceImpl {

	private CatalogProductRepository catalogProductRepo;

	@Autowired
	public CatalogProductServiceImpl(CatalogProductRepository catalogProductRepo) {
		this.catalogProductRepo = catalogProductRepo;
	}
	
	public CatalogProduct create(CatalogProduct product){
		return catalogProductRepo.save(product);
	}
}
