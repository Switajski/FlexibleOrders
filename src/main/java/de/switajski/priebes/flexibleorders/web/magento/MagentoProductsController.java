package de.switajski.priebes.flexibleorders.web.magento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.service.magento.MagentoService;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;

@RequestMapping("/magentoProducts")
@Controller
public class MagentoProductsController {

    @Autowired
    CatalogProductRepository cProductRepo;
    
    @Autowired
    MagentoService magentoService;
    
    @RequestMapping(value = "/test", method=RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listInJson(){
        List<String> cProduct = magentoService.getProducts();
        return ExtJsResponseCreator.createResponse(cProduct);
    }    
    
}
