package hr.fer.infsus.rezervacije.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.services.TerminService;

@RestController
@RequestMapping("/termini")
public class TerminController {

	@Autowired
    private TerminService terminService;
	
    @GetMapping
    public ResponseEntity<?> getAllKorisnici() {
        List<TerminProjection> termini = terminService.getAllIdPocetakZavrsetak();
        return new ResponseEntity<>(termini, HttpStatus.OK);
    }
}