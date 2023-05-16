package hr.fer.infsus.rezervacije.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.FormDataRezervacije;
import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.services.GostService;
import hr.fer.infsus.rezervacije.services.PozicijaService;
import hr.fer.infsus.rezervacije.services.RezervacijaService;
import hr.fer.infsus.rezervacije.services.StolService;
import hr.fer.infsus.rezervacije.services.TerminService;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

@RestController
@RequestMapping("/rezervacije")
public class RezervacijaController {
	@Autowired
    private GostService gostService;
	@Autowired
	 private PozicijaService pozicijaService;
	@Autowired
	 private TerminService terminService;
	@Autowired
	 private UsluzniObjektService usluzniObjektService;
	@Autowired
	private RezervacijaService rezervacijaService;
	@Autowired
	private StolService stolService;
	
	
	
	@GetMapping
	public ResponseEntity<?> getCombinedData() {
	    List<GostProjection> gostList = gostService.getAllKorisniciImePrezimeIdGosta();
	    List<Pozicija> pozicijaList = pozicijaService.getAllPozicija();
	    Map<Long, List<TerminProjection>> terminMap = terminService.getAllIdPocetakZavrsetakGrouped();
	    List<UsluzniObjektProjection> usluzniObjektiList = usluzniObjektService.getAllUsluzniObjekti();

	    FormDataRezervacije data = new FormDataRezervacije(gostList, pozicijaList, terminMap, usluzniObjektiList);
	    return ResponseEntity.ok(data);
	}
	
	@GetMapping("/all")
    public ResponseEntity<?> getRezervacijas() {
    	List<Rezervacija> rezervacije = rezervacijaService.getAllRezervacijas();
    	
    	
    	List<Map<String, Object>> body = new LinkedList<>();
    	
    	for(var rez : rezervacije) {
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
    		
    		body.add(data);
    		
    	}
    	
        return ResponseEntity.ok(body);
    }
	
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> createReservation(@RequestBody  MultiValueMap<String, String> formData) {
       
        try {
        	// čitanje dobivenih podataka
        	Long idGosta= Long.parseLong(formData.getFirst("id_gosta"));
        	String brojMobitelaGosta = formData.getFirst("broj_mobitela_gosta");
        	Long idObjekta = Long.parseLong(formData.getFirst("id_objekta"));
            String datumRezervacije = formData.getFirst("datum_rezervacije");
            int brojOsoba = Integer.parseInt(formData.getFirst("broj_osoba"));
            Long idPozicije = Long.parseLong(formData.getFirst("vrsta_stola"));
            Long idTermina = Long.parseLong(formData.getFirst("termin_rezervacija"));           
            
            // priprema podataka
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            LocalDate rezDatum = LocalDate.parse(datumRezervacije, formatter);
            
            Stol stol = stolService.getAvailableStol(idTermina, idPozicije, rezDatum, brojOsoba);  // provjerava dostupnost
            UsluzniObjekt usluzniObjekt = usluzniObjektService.getById(idObjekta);
            Gost gost = gostService.getById(idGosta);
            Termin termin = terminService.getById(idTermina);
            

            // moguće azuriranje podataka
            
            if (brojMobitelaGosta != null && !brojMobitelaGosta.strip().isEmpty()) {
                gost = gostService.updateBrojMobitela(idGosta, brojMobitelaGosta);
            }
            
            // stvaranje rezervacije
            
            Rezervacija reservation = new Rezervacija();
            reservation.setBrojOsoba(brojOsoba);
            reservation.setDatumRezervacije(rezDatum);
            reservation.setGost(gost);
            reservation.setTermin(termin);
            reservation.setStol(stol);
            reservation.setUsluzniObjekt(usluzniObjekt);
            reservation.setTst(new Timestamp(System.currentTimeMillis()));
            
            // pohrana
            rezervacijaService.createRezervacija(reservation);
            
            Rezervacija rez = rezervacijaService.getRezervacijaById(idGosta, idTermina, stol.getIdStola());
            return ResponseEntity.status(HttpStatus.CREATED).body(rez);
        } catch(RuntimeException e) {
        	String errorMessage = "Error creating reservation: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
	

	@PutMapping("/{id}")
	public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestParam MultiValueMap<String, String> formData) {
		try {
			// dohvat rezervacije
			
			Rezervacija existingReservation = rezervacijaService.getRezervacijaById(id);
			if(existingReservation == null) {
				throw new IllegalArgumentException("Nepoznata rezervacija");
			}
			
			// čitanje dobivenih podataka
			Long idGosta = Long.parseLong(formData.getFirst("id_gosta"));
			Long idPozicije = Long.parseLong(formData.getFirst("vrsta_stola"));
	        Long idTermina = Long.parseLong(formData.getFirst("termin_rezervacija"));
	        Long idObjekta = Long.parseLong(formData.getFirst("id_objekta"));
	        String datumRezervacije = formData.getFirst("datum_rezervacije");
	        int brojOsoba = Integer.parseInt(formData.getFirst("broj_osoba"));
	        String brojMobitelaGosta = formData.getFirst("broj_mobitela_gosta");
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
			LocalDate rezDatum = LocalDate.parse(datumRezervacije, formatter);
	        
	        // provjera mijenja li se primarni ključ
	        // ako da, potrebno stvoriti novu rezervaciju
			
			// promjena idObjekta => promjena idTermina, pa nije potrebna provjera
	        if(!idGosta.equals(existingReservation.getGost().getIdGosta())
	           || !idTermina.equals(existingReservation.getTermin().getIdTermina()) 
	           || !idPozicije.equals(existingReservation.getStol().getPozicija().getIdPozicije()) 
	           || brojOsoba > existingReservation.getStol().getBrojStolica()) // premalo stolica
	        {
	        	// Briši postojeću rezervaciju
	            rezervacijaService.deleteRezervacijaById(id);

	            // Kreiraj novu rezervaciju sa novim idStola
	            Stol stol = stolService.getAvailableStol(idTermina, idPozicije, rezDatum, brojOsoba);
	            UsluzniObjekt usluzniObjekt = usluzniObjektService.getById(idObjekta);
	            Gost gost = gostService.getById(idGosta);
	            Termin termin = terminService.getById(idTermina);
	            
	            // Moguće azuriranje podataka gosta
				
				if (brojMobitelaGosta != null && !brojMobitelaGosta.strip().isEmpty()) {
					gost = gostService.updateBrojMobitela(idGosta, brojMobitelaGosta);
					existingReservation.setGost(gost);
				    
				}

	            // Kreiraj novu rezervaciju
	            Rezervacija newReservation = new Rezervacija();
	            newReservation.setBrojOsoba(brojOsoba);
	            newReservation.setDatumRezervacije(rezDatum);
	            newReservation.setGost(gost);
	            newReservation.setTermin(termin);
	            newReservation.setStol(stol);
	            newReservation.setUsluzniObjekt(usluzniObjekt);
	            newReservation.setTst(existingReservation.getTst());
	            

	            // Pohrani novu rezervaciju
	            rezervacijaService.createRezervacija(newReservation);

	            return ResponseEntity.ok(newReservation);
	        }
	        System.out.println("Here");
	        // samo update
	        // moguće za vrijednosti: broj osoba, datum rezervacije, broj mobitela gosta
	        existingReservation.setBrojOsoba(brojOsoba);
	        existingReservation.setDatumRezervacije(rezDatum);
	     

			// moguće azuriranje podataka gosta
			
			if (brojMobitelaGosta != null && !brojMobitelaGosta.strip().isEmpty()) {
				Gost gost = gostService.updateBrojMobitela(idGosta, brojMobitelaGosta);
				existingReservation.setGost(gost);
			    
			}
			
			//pohrana
			rezervacijaService.updateRezervacija(existingReservation);

			return ResponseEntity.ok(existingReservation);
		} catch(RuntimeException e) {
        	String errorMessage = "Error updating reservation: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRezervacijaById(@PathVariable Long id) {
        rezervacijaService.deleteRezervacijaById(id);
        return ResponseEntity.noContent().build();
    }

}
