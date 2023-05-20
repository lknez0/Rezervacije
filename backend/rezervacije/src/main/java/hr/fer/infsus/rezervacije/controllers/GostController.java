package hr.fer.infsus.rezervacije.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.repository.RezervacijaRepository;

@RestController
@RequestMapping(value = "/gost", produces = MediaType.APPLICATION_JSON_VALUE)
public class GostController {
	@Autowired
	RezervacijaRepository gostRepository;
	
	@GetMapping
	public ResponseEntity<?> getCombinedData() {
		List<Rezervacija> gostList = gostRepository.findAll();
		return ResponseEntity.ok(gostList);
	}

}
