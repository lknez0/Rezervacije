package hr.fer.infsus.rezervacije.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

			// azuriranje
			existingReservation.setBrojOsoba(brojOsoba);
			existingReservation.setDatumRezervacije(rezDatum);
			existingReservation.setGost(gost);
			existingReservation.setTermin(termin);
			existingReservation.setStol(stol);
			existingReservation.setUsluzniObjekt(usluzniObjekt);

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
