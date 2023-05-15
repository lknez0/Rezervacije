package hr.fer.infsus.rezervacije.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.repository.StolRepository;

@Service
public class StolService {
	
	@Autowired
	private StolRepository stolRepository;
	
	public Stol getAvailableStol(Long idTermina, Long idPozicije, LocalDate datumRezervacije) {
		List<Stol> dostupniStolovi = stolRepository.
				findAvailableStolByTerminAndDatumRezervacijeAndPozicija(idTermina, datumRezervacije, idPozicije);
	
		if(dostupniStolovi == null || dostupniStolovi.isEmpty())
			throw new IllegalArgumentException("Stol ID not available");
		
		return dostupniStolovi.get(0);
	
	}

}
