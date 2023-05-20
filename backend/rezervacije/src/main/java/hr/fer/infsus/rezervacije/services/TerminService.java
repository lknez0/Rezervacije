package hr.fer.infsus.rezervacije.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.repository.TerminRepository;

@Service
public class TerminService {

	@Autowired
    private TerminRepository terminRepository;
	
	public Termin getById(Long idTermina) {
		return terminRepository.findById(idTermina).orElseThrow(() -> new IllegalArgumentException("Invalid Termin ID"));
	}

    
    public Map<Long, List<Termin>> getAllTerminGrouped() {
        List<Termin> terminList = terminRepository.findAll();
        return terminList.stream().collect(Collectors.groupingBy(t -> t.getUsluzniObjekt().getIdObjekta()));
    }
}