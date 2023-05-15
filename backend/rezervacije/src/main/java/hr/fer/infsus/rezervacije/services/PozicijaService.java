package hr.fer.infsus.rezervacije.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.repository.PozicijaRepository;

@Service
public class PozicijaService {
	@Autowired
    private PozicijaRepository pozicijaRepository;
	
	public List<Pozicija> getAllPozicija() {
        return pozicijaRepository.findAll();
    }

}

