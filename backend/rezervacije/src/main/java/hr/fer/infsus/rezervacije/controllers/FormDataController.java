package hr.fer.infsus.rezervacije.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.FormDataRezervacije;
import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.ReservationRequest;
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
@RequestMapping("/rezervacija-forma")
public class FormDataController {
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
	    List<TerminProjection> terminList = terminService.getAllIdPocetakZavrsetak();
	   
	    List<UsluzniObjektProjection> usluzniObjektiList = usluzniObjektService.getAllUsluzniObjekti();

	    FormDataRezervacije data = new FormDataRezervacije(gostList, pozicijaList, terminList, usluzniObjektiList);
	    return ResponseEntity.ok(data);
	}
	
	@PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request) {
       
        try {
        	Long idGosta = request.getIdGosta();
            String brojMobitelaGosta = request.getBrojMobitelaGosta();
            Long idObjekta = request.getIdObjekta();
            String datumRezervacije = request.getDatumRezervacije();
            int brojOsoba = request.getBrojOsoba();
            Long idPozicije = request.getVrstaStola();
            Long idTermina = request.getTerminRezervacija();
            
            
            // priprema podataka
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate rezDatum = LocalDate.parse(datumRezervacije, formatter);
            
            Stol stol = stolService.getAvailableStol(idTermina, idPozicije, rezDatum);  // provjerava dostupnost
            
            if(brojOsoba < stol.getBrojStolica()) {
            	throw new IllegalArgumentException("Nedovoljno stolica");
            }
            
            UsluzniObjekt usluzniObjekt = usluzniObjektService.getById(idObjekta);
            Gost gost = gostService.getById(idGosta);
            Termin termin = terminService.getById(idTermina);
            
            // azuriranje mobitela ako je potrebno
            if(brojMobitelaGosta == null || !brojMobitelaGosta.strip().equals("")) {
            	gost = gostService.updateBrojMobitela(idGosta, brojMobitelaGosta);
            }
            
                   
            Rezervacija reservation = new Rezervacija();
            reservation.setBrojOsoba(brojOsoba);
            reservation.setDatumRezervacije(rezDatum);
            reservation.setGost(gost);
            reservation.setTermin(termin);
            reservation.setStol(stol);
            reservation.setUsluzniObjekt(usluzniObjekt);
            
            
            rezervacijaService.createRezervacija(reservation);

            Long id = idGosta * 100000 + idTermina * 1000 + stol.getIdStola();
            return ResponseEntity.status(HttpStatus.CREATED).body(rezervacijaService.getRezervacijaById(id));
        } catch(RuntimeException e) {
        	String errorMessage = "Error creating reservation: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody ReservationRequest request) {
		try {
			Rezervacija existingReservation = rezervacijaService.getRezervacijaById(id);
			
			Long idGosta = request.getIdGosta();
			String brojMobitelaGosta = request.getBrojMobitelaGosta();
			Long idObjekta = request.getIdObjekta();
			String datumRezervacije = request.getDatumRezervacije();
			int brojOsoba = request.getBrojOsoba();
			Long idPozicije = request.getVrstaStola();
			Long idTermina = request.getTerminRezervacija();

			// priprema podataka
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate rezDatum = LocalDate.parse(datumRezervacije, formatter);

			Stol stol = stolService.getAvailableStol(idTermina, idPozicije, rezDatum);  // provjerava dostupnost

			if (brojOsoba < stol.getBrojStolica()) {
			    throw new IllegalArgumentException("Nedovoljno stolica");
			}

			UsluzniObjekt usluzniObjekt = usluzniObjektService.getById(idObjekta);
			Gost gost = gostService.getById(idGosta);
			Termin termin = terminService.getById(idTermina);

			// azuriranje mobitela ako je potrebno
			if (brojMobitelaGosta == null || !brojMobitelaGosta.strip().equals("")) {
			    gost = gostService.updateBrojMobitela(idGosta, brojMobitelaGosta);
			}

			existingReservation.setBrojOsoba(brojOsoba);
			existingReservation.setDatumRezervacije(rezDatum);
			existingReservation.setGost(gost);
			existingReservation.setTermin(termin);
			existingReservation.setStol(stol);
			existingReservation.setUsluzniObjekt(usluzniObjekt);

			rezervacijaService.updateRezervacija(existingReservation);

			return ResponseEntity.ok(existingReservation);
		} catch(RuntimeException e) {
        	String errorMessage = "Error updating reservation: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

	}

}
