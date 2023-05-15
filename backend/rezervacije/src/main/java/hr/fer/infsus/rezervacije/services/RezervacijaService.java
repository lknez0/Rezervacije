package hr.fer.infsus.rezervacije.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.repository.RezervacijaRepository;

@Service
public class RezervacijaService {
	
	@Autowired
    private RezervacijaRepository rezervacijaRepository;

    public List<Rezervacija> getAllRezervacijas() {
        return rezervacijaRepository.findAll();
    }
    
//    public Rezervacija getRezervacijaById(Long gost, Long termin, Long stol) {
//    	Optional<Rezervacija> findByIdGostaAndIdTerminaAndIdStola(Long idGosta, Long idTermina, Long idStola);
//        Optional<Rezervacija> rezervacija = rezervacijaRepository.findByIdGostaAndIdTerminaAndIdStola(gost, termin, stol);
//        return rezervacija.orElse(null);
//    }
    
    

    public void createRezervacija(Rezervacija Rezervacija) {
    	rezervacijaRepository.save(Rezervacija);
    }

	public Rezervacija updateRezervacija(Long id, Rezervacija rezervacija) {
		// TODO 
		return null;
	}

	public Rezervacija getRezervacijaById(Long id) {
		Long idGosta = id / 100000;
	    Long idTermina = (id / 1000) % 100;
	    Long idStola = id % 1000;
	    
		
		return rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola);
	}

	public void deleteRezervacijaById(Long id) {
	    Rezervacija rezervacija = getRezervacijaById(id);
	    if (rezervacija != null) {
	        rezervacijaRepository.delete(rezervacija);
	    }
	}

}
