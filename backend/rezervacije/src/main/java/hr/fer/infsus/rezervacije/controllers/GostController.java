package hr.fer.infsus.rezervacije.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.services.GostService;

@RestController
@RequestMapping("/gosti")
public class GostController {

	@Autowired
    private GostService gostService;
	
    @GetMapping
    public ResponseEntity<List<GostProjection>> getAllKorisnici() {
        List<GostProjection> korisnici = gostService.getAllKorisniciImePrezimeIdGosta();
        return new ResponseEntity<>(korisnici, HttpStatus.OK);
    }
}