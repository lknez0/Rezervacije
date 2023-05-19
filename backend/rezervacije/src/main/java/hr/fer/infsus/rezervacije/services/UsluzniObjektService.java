package hr.fer.infsus.rezervacije.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.repository.UsluzniObjektRepository;

@Service
public class UsluzniObjektService {

	@Autowired
    private UsluzniObjektRepository usluzniObjektRepository;

	
	public List<UsluzniObjekt> getAll() {
		return usluzniObjektRepository.findAll();
	}

	public UsluzniObjekt getById(Long idObjekta) {
		return usluzniObjektRepository.findById(idObjekta).orElseThrow(() -> new IllegalArgumentException("Invalid UsluzniObjekt ID"));
	}

	public List<UsluzniObjektProjection> getAllProjections(Map<Long, List<Termin>> terminMap) {
		 List<UsluzniObjekt> objekti = getAll();
		    List<UsluzniObjektProjection> usluzniObjektiList = new LinkedList<>();

		    for (UsluzniObjekt objekt : objekti) {
		        List<Termin> termini = terminMap.get(objekt.getIdObjekta());
		        List<TerminProjection> terminProjections = new LinkedList<>();

		        if (termini != null) {
		            for (Termin termin : termini) {
		                TerminProjection terminProjection = new TerminProjection(
		                    termin.getIdTermina(),
		                    termin.getVrijemePocetka(),
		                    termin.getVrijemeZavrsetka()
		                );
		                terminProjections.add(terminProjection);
		            }
		        }

		        UsluzniObjektProjection usluzniObjektProjection = new UsluzniObjektProjection(
		            objekt.getIdObjekta(),
		            objekt.getNazivObjekta(),
		            objekt.getAdresaObjekta(),
		            objekt.getGradObjekta(),
		            terminProjections
		        );
		        usluzniObjektiList.add(usluzniObjektProjection);
		    }

		    return usluzniObjektiList;
	}


}
