package hr.fer.infsus.rezervacije.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.FormDataRezervacije;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.ReservationData;
import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.services.GostService;
import hr.fer.infsus.rezervacije.services.PozicijaService;
import hr.fer.infsus.rezervacije.services.RezervacijaService;
import hr.fer.infsus.rezervacije.services.TerminService;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/rezervacije", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@GetMapping
	public ResponseEntity<?> getCombinedData() {
		List<GostProjection> gostList = gostService.getAllKorisniciImePrezimeIdGosta();
		List<Pozicija> pozicijaList = pozicijaService.getAllPozicija();
		Map<Long, List<Termin>> terminMap = terminService.getAllTerminGrouped();
		List<UsluzniObjektProjection> usluzniObjektiList = usluzniObjektService.getAllProjections(terminMap);

		FormDataRezervacije data = new FormDataRezervacije(gostList, pozicijaList, usluzniObjektiList);
		return ResponseEntity.ok(data);
	}

	@GetMapping("/all")
	public ResponseEntity<?> getReservations() {
		List<Rezervacija> rezervacije = rezervacijaService.getAllRezervacijas();
		List<Map<String, Object>> body = new LinkedList<>();

		for (var rez : rezervacije) {
			Map<String, Object> data = rezervacijaService.buildRezervacijaProjection(rez);
			body.add(data);
		}

		return ResponseEntity.ok(body);
	}

	@GetMapping("/info/{id}")
	public ResponseEntity<?> getReservation(@PathVariable Long id) {
		Rezervacija rez = rezervacijaService.getRezervacijaById(id);
		Map<String, Object> body = rezervacijaService.buildRezervacijaProjection(rez);

		return ResponseEntity.ok(body);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getReservationForm(@PathVariable Long id) {
		Rezervacija rez = rezervacijaService.getRezervacijaById(id);
		
		if(rez == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Reservation not found");
		}

		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		formData.add("idGosta", rez.getGost().getIdGosta());
		formData.add("brojMobitelaGosta", rez.getGost().getBrojMobitela());
		formData.add("idObjekta", rez.getUsluzniObjekt().getIdObjekta());
		formData.add("datumRezervacije", rez.getDatumRezervacije());
		formData.add("brojOsoba", rez.getBrojOsoba());
		formData.add("pozicija", rez.getStol().getPozicija().getIdPozicije());
		formData.add("terminRezervacija", rez.getTermin().getIdTermina());

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(formData.toSingleValueMap());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createReservation(@RequestBody ReservationData data) {
		try {
			Rezervacija rez = rezervacijaService.createRezervacija(data);
			rez = rezervacijaService.saveRezervacija(rez);

			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
					.body(rezervacijaService.buildRezervacijaProjection(rez));
		} catch (RuntimeException e) {
			String errorMessage = "Error creating reservation: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateReservation(@PathVariable Long id,
			@RequestBody  ReservationData data) {
		try {
			Rezervacija existingReservation = rezervacijaService.getRezervacijaById(id);

			if (existingReservation == null) {
				throw new IllegalArgumentException("Nepoznata rezervacija");
			}

			if (rezervacijaService.requiresNewRezervacija(existingReservation, data)) {
				Rezervacija rez = rezervacijaService.createRezervacija(data);
				rez = rezervacijaService.saveRezervacija(rez);

				return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(rezervacijaService.buildRezervacijaProjection(rez));
			} else {
				rezervacijaService.updateReservation(existingReservation, data);
				rezervacijaService.saveRezervacija(existingReservation);

				return ResponseEntity.ok(rezervacijaService.buildRezervacijaProjection(existingReservation));
			}
		} catch (RuntimeException e) {
			String errorMessage = "Error updating reservation: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReservationById(@PathVariable Long id) {
		rezervacijaService.deleteRezervacijaById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
