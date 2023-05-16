package hr.fer.infsus.rezervacije.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.repository.RezervacijaRepository;

@Service
public class RezervacijaService {
	
	@Autowired
    private RezervacijaRepository rezervacijaRepository;

    public List<Rezervacija> getAllRezervacijas() {
        return rezervacijaRepository.findAll();
    }
    

    public void createRezervacija(Rezervacija rezervacija) {
    	rezervacijaRepository.save(rezervacija);
    }

    public void updateRezervacija(Rezervacija rezervacija) {
        rezervacijaRepository.save(rezervacija);
    }
    
    public Rezervacija getRezervacijaById(Long idGosta, Long idTermina, Long idStola) {
		return rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola);
	}

	public Rezervacija getRezervacijaById(Long id) {
		Long idGosta = id / 100000;
	    Long idTermina = (id / 1000) % 100;
	    Long idStola = id % 1000;
	    
		
		return getRezervacijaById(idGosta, idTermina, idStola);
	}


	public void deleteRezervacijaById(Long id) {
	    Rezervacija rezervacija = getRezervacijaById(id);
	    if (rezervacija != null) {
	        rezervacijaRepository.delete(rezervacija);
	    }
	}

}
