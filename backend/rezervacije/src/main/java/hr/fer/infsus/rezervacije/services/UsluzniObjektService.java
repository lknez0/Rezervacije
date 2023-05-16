package hr.fer.infsus.rezervacije.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.repository.UsluzniObjektRepository;

@Service
public class UsluzniObjektService {

	@Autowired
    private UsluzniObjektRepository usluzniObjektRepository;
	
	public List<UsluzniObjektProjection> getAllUsluzniObjekti() {
        return usluzniObjektRepository.findAllUsluzniObjekti();
    }

	public UsluzniObjekt getById(Long idObjekta) {
		return usluzniObjektRepository.findById(idObjekta).orElseThrow(() -> new IllegalArgumentException("Invalid UsluzniObjekt ID"));
	}


}
