package hr.fer.infsus.rezervacije.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.repository.UsluzniObjektRepository;

@Service
public class UsluzniObjektService {

	@Autowired
    private UsluzniObjektRepository usluzniObjektRepository;
	
	public List<UsluzniObjekt> getAllUsluzniObjekti() {
        return usluzniObjektRepository.findAll();
    }


}
