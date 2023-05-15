package hr.fer.infsus.rezervacije.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.KorisnikProjection;
import hr.fer.infsus.rezervacije.services.KorisnikService;

@RestController
@RequestMapping("/korisnici")
public class KorisnikController {

	@Autowired
    private KorisnikService korisnikService;
	
    @GetMapping
    public ResponseEntity<List<KorisnikProjection>> getAllKorisnici() {
        List<KorisnikProjection> korisnici = korisnikService.getAllKorisniciImePrezimeIdGosta();
        return new ResponseEntity<>(korisnici, HttpStatus.OK);
    }
}