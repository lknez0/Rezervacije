package hr.fer.infsus.rezervacije.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.services.PozicijaService;

@RestController
@RequestMapping("/pozicije")
public class PozicijaController {
	 @Autowired
	 private PozicijaService pozicijaService;
	 
	 @GetMapping
	    public List<?> getPozicije() {
	    	return pozicijaService.getAllPozicija();
	    }


}


