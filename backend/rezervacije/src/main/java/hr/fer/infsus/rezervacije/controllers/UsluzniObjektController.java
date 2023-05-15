package hr.fer.infsus.rezervacije.controllers;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

@RestController
@RequestMapping("/usluzni-objekti")
public class UsluzniObjektController {
	
	 @Autowired
	 private UsluzniObjektService usluzniObjektService;
	
	@GetMapping
    public List<?> getUsluzniObjekti() {
    	List<UsluzniObjekt> usluzniObjekti = usluzniObjektService.getAllUsluzniObjekti();
    	
    	
    	List<Map<String, Object>> body = new LinkedList<>();
    	
    	for(var obj : usluzniObjekti) {
    		Map<String, Object> data = new LinkedHashMap<>();
    		
    		data.put("id", obj.getIdObjekta());
    		data.put("nazivObjekta", obj.getNazivObjekta());
    		data.put("adresaObjekta", obj.getAdresaObjekta());
    		data.put("getGradObjekta", obj.getGradObjekta());
    		
    		body.add(data);
    		
    	}
    	
        return body;
    }

}
