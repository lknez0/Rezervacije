package hr.fer.infsus.rezervacije.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

@RestController
@RequestMapping("/usluzni-objekti")
public class UsluzniObjektController {
	
	 @Autowired
	 private UsluzniObjektService usluzniObjektService;
	
	@GetMapping
    public ResponseEntity<?> getUsluzniObjekti() {
    	List<UsluzniObjektProjection> usluzniObjekti = usluzniObjektService.getAllUsluzniObjekti();
    	 return new ResponseEntity<>(usluzniObjekti, HttpStatus.OK);
    }

}
