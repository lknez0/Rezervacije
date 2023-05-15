package hr.fer.infsus.rezervacije.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.repository.TerminRepository;

@Service
public class TerminService {

	@Autowired
    private TerminRepository terminRepository;
	
	public Termin getById(Long idTermina) {
		return terminRepository.findById(idTermina).orElseThrow(() -> new IllegalArgumentException("Invalid Termin ID"));
	}

    public List<TerminProjection> getAllIdPocetakZavrsetak() {
        return terminRepository.findAllIdPocetakZavrsetak();
    }

}