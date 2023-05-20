package hr.fer.infsus.rezervacije.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.ReservationData;
import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.repository.RezervacijaRepository;

@Service
public class RezervacijaService {
	
	@Autowired
    private RezervacijaRepository rezervacijaRepository;
	
	@Autowired
	private GostService gostService;
	@Autowired
	private TerminService terminService;
	@Autowired
	private UsluzniObjektService usluzniObjektService;
	@Autowired
	private StolService stolService;
	
	private final long GOST_ID_MULT = 100000000;
	private final long TERMIN_ID_MULT =  100000;
	private final long STOL_ID_MULT = 100;
	

    public List<Rezervacija> getAllRezervacijas() {
        return rezervacijaRepository.findAll();
    }
    

    public Rezervacija saveRezervacija(Rezervacija rezervacija) {
    	return rezervacijaRepository.save(rezervacija);
    }

    
    public Rezervacija getRezervacijaById(Long idGosta, Long idTermina, Long idStola) {
		return rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola);
	}

	public Rezervacija getRezervacijaById(Long id) {
		Long idGosta = id / GOST_ID_MULT;
	    Long idTermina = (id / TERMIN_ID_MULT) % STOL_ID_MULT;
	    Long idStola = id % TERMIN_ID_MULT;
	    
		
		return getRezervacijaById(idGosta, idTermina, idStola);
	}
	
	public void deleteRezervacijaById(Long id) {
	    Rezervacija rezervacija = getRezervacijaById(id);
	    if (rezervacija != null) {
	        rezervacijaRepository.delete(rezervacija);
	    }
	}
	
	public Map<String, Object> buildRezervacijaProjection(Rezervacija rez) {
		Map<String, Object> data = new LinkedHashMap<>();
		
		Long id = rez.getGost().getIdGosta() * GOST_ID_MULT 
				+ rez.getTermin().getIdTermina() * TERMIN_ID_MULT
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
	
	 public Rezervacija createRezervacija(ReservationData data) {
		 
	        Stol stol = stolService.getAvailableStol(data.getTerminRezervacije(), data.getPozicija(),
	                data.getDatumRezervacije(), data.getBrojOsoba());
	        UsluzniObjekt usluzniObjekt = usluzniObjektService.getById(data.getIdObjekta());
	        Termin termin = terminService.getById(data.getTerminRezervacije());
	        Gost gost = gostService.updateNumberIfNeeded(data.getIdGosta(), data.getBrojMobitelaGosta());
	        
	        if(!termin.getUsluzniObjekt().getIdObjekta().equals(usluzniObjekt.getIdObjekta())) {
	        	throw new IllegalArgumentException("Odabrani termin nije dostupan u objektu");
	        }

	        return createRezervacija(data, stol, termin, gost, usluzniObjekt);
	    }
	
	private Rezervacija createRezervacija(ReservationData data, Stol stol, Termin termin, Gost gost, UsluzniObjekt objekt) {
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setBrojOsoba(data.getBrojOsoba());
		rezervacija.setDatumRezervacije(data.getDatumRezervacije());
		rezervacija.setGost(gost);
		rezervacija.setTermin(termin);
		rezervacija.setStol(stol);
		rezervacija.setUsluzniObjekt(objekt);
		rezervacija.setTst(new Timestamp(System.currentTimeMillis()));
        
        return rezervacija;
	}

	
	public void updateReservation(Rezervacija existingReservation, ReservationData data) {
		Gost gost = gostService.updateNumberIfNeeded(data.getIdGosta(), data.getBrojMobitelaGosta());
		existingReservation.setGost(gost);
		
	    LocalDate datumRezervacije = data.getDatumRezervacije();
	    int brojOsoba = data.getBrojOsoba();
	  
	    
	    existingReservation.setBrojOsoba(brojOsoba);
	    existingReservation.setDatumRezervacije(datumRezervacije);
	   
	}
	
	public ReservationData extractReservationData(MultiValueMap<String, String> formData) {
		 ReservationData data = new ReservationData();

		 
		 data.setIdGosta(Long.parseLong(formData.getFirst("id_gosta")));
		 data.setPozicija(Long.parseLong(formData.getFirst("vrsta_stola")));
		 data.setTerminRezervacije(Long.parseLong(formData.getFirst("termin_rezervacije")));
		 data.setIdObjekta(Long.parseLong(formData.getFirst("id_objekta")));
		 
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		 data.setDatumRezervacije(LocalDate.parse(formData.getFirst("datum_rezervacije"), formatter));
		 
		 data.setBrojOsoba(Integer.parseInt(formData.getFirst("broj_osoba")));
		 data.setBrojMobitelaGosta(formData.getFirst("broj_mobitela_gosta"));

		 return data;
	}
	
	public boolean requiresNewRezervacija(Rezervacija existingReservation, ReservationData updateData) {
	    Long idGosta = updateData.getIdGosta();
	    Long idPozicije = updateData.getPozicija();
	    Long idTermina = updateData.getTerminRezervacije();
	    int brojOsoba = updateData.getBrojOsoba();

	    return !idGosta.equals(existingReservation.getGost().getIdGosta())
	        || !idTermina.equals(existingReservation.getTermin().getIdTermina())
	        || !idPozicije.equals(existingReservation.getStol().getPozicija().getIdPozicije())
	        || brojOsoba > existingReservation.getStol().getBrojStolica()
	        || !updateData.getDatumRezervacije().equals(existingReservation.getDatumRezervacije());
	}

	

}
