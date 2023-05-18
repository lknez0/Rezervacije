package hr.fer.infsus.rezervacije.services;

import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	
	public Map<String, Object> buildRezervacijaProjection(Rezervacija rez) {
		Map<String, Object> data = new LinkedHashMap<>();
		
		Long id = rez.getGost().getIdGosta() * 100000 
				+ rez.getTermin().getIdTermina() * 1000
				+ rez.getStol().getIdStola();
		
		data.put("id", id);
		data.put("nazivObjekta", rez.getUsluzniObjekt().getNazivObjekta());
		data.put("adresaObjekta", rez.getUsluzniObjekt().getAdresaObjekta());
		data.put("imeGosta", rez.getGost().getKorisnik().getImeKorisnika() + " " 
		+ rez.getGost().getKorisnik().getPrezimeKorisnika());
		data.put("brojMobitelaGosta", rez.getGost().getBrojMobitela());
		data.put("vrijemePocetka", rez.getTermin().getVrijemePocetka());
		data.put("vrijemeZavrsetka", rez.getTermin().getVrijemeZavrsetka());
		data.put("datumRezervacije", rez.getDatumRezervacije());
		data.put("danUTjednu", rez.getDatumRezervacije().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("hr", "HR")));
		data.put("brojOsoba", rez.getBrojOsoba());
		data.put("brojStolica", rez.getStol().getBrojStolica());
		data.put("pozicijaStola", rez.getStol().getPozicija().getNazivPozicije());
		
		return data;
		
	}


	public void deleteRezervacijaById(Long id) {
	    Rezervacija rezervacija = getRezervacijaById(id);
	    if (rezervacija != null) {
	        rezervacijaRepository.delete(rezervacija);
	    }
	}

}
