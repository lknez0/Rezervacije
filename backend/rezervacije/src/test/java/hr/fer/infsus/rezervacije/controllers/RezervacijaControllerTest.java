package hr.fer.infsus.rezervacije.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import hr.fer.infsus.rezervacije.models.FormDataRezervacije;
import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.models.Korisnik;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.ReservationData;
import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.services.GostService;
import hr.fer.infsus.rezervacije.services.PozicijaService;
import hr.fer.infsus.rezervacije.services.RezervacijaService;
import hr.fer.infsus.rezervacije.services.TerminService;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
class RezervacijaControllerTest {

	@Mock
	private GostService gostService;

	@Mock
	private PozicijaService pozicijaService;

	@Mock
	private TerminService terminService;

	@Mock
	private UsluzniObjektService usluzniObjektService;

	@Mock
	private RezervacijaService rezervacijaService;

	@InjectMocks
	private RezervacijaController rezervacijaController;

	public RezervacijaControllerTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetCombinedData() {
		// Arrange
		List<GostProjection> gostList = getGostProjections();
		List<Pozicija> pozicijaList = getPozicijaList();
		Map<Long, List<Termin>> terminMap = getTerminGrouped();
		List<UsluzniObjektProjection> usluzniObjektiList = getUsluzniObjektiList();

		when(gostService.getAllKorisniciImePrezimeIdGosta()).thenReturn(gostList);
		when(pozicijaService.getAllPozicija()).thenReturn(pozicijaList);
		when(terminService.getAllTerminGrouped()).thenReturn(terminMap);
		when(usluzniObjektService.getAllProjections(terminMap)).thenReturn(usluzniObjektiList);

		// Act
		ResponseEntity<?> response = rezervacijaController.getCombinedData();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		FormDataRezervacije data = (FormDataRezervacije) response.getBody();

		assertNotNull(data);

		assertEquals(gostList, data.getGosti());
		assertEquals(pozicijaList, data.getPozicije());
		assertEquals(usluzniObjektiList, data.getUsluzniObjekti());

	}
	
	@Test
	void testGetReservations() {
		Rezervacija rez = buildRezervacija();
		Map<String, Object> data = new LinkedHashMap<>();
		
		when(rezervacijaService.getAllRezervacijas()).thenReturn(List.of(rez));
		when(rezervacijaService.buildRezervacijaProjection(rez)).thenReturn(data);
		
		ResponseEntity<?> response = rezervacijaController.getReservations();
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testGetReservation() {
		Long id = 1L;
		Rezervacija rez = buildRezervacija();
		Map<String, Object> data = new LinkedHashMap<>();
		
		when(rezervacijaService.getRezervacijaById(id)).thenReturn(rez);
		when(rezervacijaService.buildRezervacijaProjection(rez)).thenReturn(data);
		
		ResponseEntity<?> response = rezervacijaController.getReservation(id);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(data, response.getBody());
	}

	@Test
	void testCreateReservationSuccess() {
		ReservationData data = getReservationData();
		Rezervacija rez = buildRezervacija();

		when(rezervacijaService.createRezervacija(data)).thenReturn(rez);

		// Act
		ResponseEntity<?> response = rezervacijaController.createReservation(data);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		verify(rezervacijaService).createRezervacija(data);
		verify(rezervacijaService).saveRezervacija(rez);
	}

	@Test
	void testCreateReservationFail() {
		ReservationData data = getReservationData();
		when(rezervacijaService.createRezervacija(data)).thenThrow(IllegalArgumentException.class);

		ResponseEntity<?> response = rezervacijaController.createReservation(data);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		verify(rezervacijaService).createRezervacija(data);
	}

	@Test
	void testUpdateReservationSuccess() {
		ReservationData data = getReservationData();
		Rezervacija rez = buildRezervacija();

		when(rezervacijaService.getRezervacijaById(1L)).thenReturn(rez);
		when(rezervacijaService.requiresNewRezervacija(any(Rezervacija.class), eq(data))).thenReturn(true);
		when(rezervacijaService.createRezervacija(data)).thenReturn(rez);
		when(rezervacijaService.saveRezervacija(rez)).thenReturn(rez);

		// Act
		ResponseEntity<?> response = rezervacijaController.updateReservation(1L, data);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(rezervacijaService).createRezervacija(data);
		verify(rezervacijaService).saveRezervacija(rez);
	}

	@Test
	void testUpdateReservationSuccess_NoIdChange() {
		ReservationData data = getReservationData();
		Rezervacija rez = buildRezervacija();

		when(rezervacijaService.getRezervacijaById(1L)).thenReturn(rez);
		when(rezervacijaService.requiresNewRezervacija(any(Rezervacija.class), eq(data))).thenReturn(false);
		
	
		ResponseEntity<?> response = rezervacijaController.updateReservation(1L, data);

	
		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(rezervacijaService).updateReservation(rez, data);
		verify(rezervacijaService).saveRezervacija(rez);
	}

	@Test
	void testUpdateReservationFail_NoReservation() {
		ReservationData data = getReservationData();

		when(rezervacijaService.getRezervacijaById(1L)).thenReturn(null);
		ResponseEntity<?> response = rezervacijaController.updateReservation(1L, data);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Error updating reservation: Nepoznata rezervacija", response.getBody());
	}


	@Test
	void testDeleteRezervacijaById() {
		Long rezervacijaId = 1L;

		ResponseEntity<?> response = rezervacijaController.deleteReservationById(rezervacijaId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(rezervacijaService).deleteRezervacijaById(rezervacijaId);
	}

	// pomoćne metode

	private List<GostProjection> getGostProjections() {
		GostProjection gost1 = new GostProjection() {
			@Override
			public String getImeKorisnika() {
				return "Ivo";
			}

			@Override
			public String getPrezimeKorisnika() {
				return "Ivić";
			}

			@Override
			public Long getIdGosta() {
				return 1L;
			}
		};

		GostProjection gost2 = new GostProjection() {
			@Override
			public String getImeKorisnika() {
				return "Ana";
			}

			@Override
			public String getPrezimeKorisnika() {
				return "Anić";
			}

			@Override
			public Long getIdGosta() {
				return 2L;
			}
		};

		return Arrays.asList(gost1, gost2);
	}

	private List<Pozicija> getPozicijaList() {
		Pozicija pozicija1 = new Pozicija();
		pozicija1.setIdPozicije(1L);
		pozicija1.setNazivPozicije("na sredini");

		Pozicija pozicija2 = new Pozicija();
		pozicija2.setIdPozicije(2L);
		pozicija2.setNazivPozicije("uz prozor");

		return Arrays.asList(pozicija1, pozicija2);
	}

	private List<Termin> getTermini() {
		Termin t1 = new Termin();
		t1.setIdTermina(1L);
		t1.setVrijemePocetka("10:00");
		t1.setVrijemeZavrsetka("11:30");

		Termin t2 = new Termin();
		t1.setIdTermina(2L);
		t1.setVrijemePocetka("13:00");
		t1.setVrijemeZavrsetka("14:30");

		return List.of(t1, t2);
	}

	private List<TerminProjection> convertToTerminProjection(List<Termin> termini) {
		List<TerminProjection> terminProjections = new LinkedList<>();

		for (var termin : termini) {
			terminProjections.add(new TerminProjection(termin.getIdTermina(), termin.getVrijemePocetka(),
					termin.getVrijemeZavrsetka()));
		}

		return terminProjections;
	}

	private Map<Long, List<Termin>> getTerminGrouped() {
		List<Termin> termini = getTermini();

		Map<Long, List<Termin>> terminMap = new LinkedHashMap<>();
		terminMap.put(1L, List.of(termini.get(0)));
		terminMap.put(2L, List.of(termini.get(1)));

		return terminMap;
	}

	private List<UsluzniObjektProjection> getUsluzniObjektiList() {
		List<TerminProjection> termini = convertToTerminProjection(getTermini());

		UsluzniObjektProjection objekt1 = new UsluzniObjektProjection(1L, "Boogie Lab Zagreb", "Ulica kneza Borne 26",
				"Zagreb", List.of(termini.get(0)));

		UsluzniObjektProjection objekt2 = new UsluzniObjektProjection(2L, "Leggiero", "Maksimirsko naselje IV 25",
				"Zagreb", List.of(termini.get(1)));

		return Arrays.asList(objekt1, objekt2);
	}

	private ReservationData getReservationData() {
		ReservationData reservationData = new ReservationData();

		reservationData.setIdGosta(1L);
		reservationData.setBrojMobitelaGosta("097541283");
		reservationData.setIdObjekta(2L);
		reservationData.setDatumRezervacije(LocalDate.of(2023, 1, 1));
		reservationData.setBrojOsoba(4);
		reservationData.setPozicija(1L);
		reservationData.setTerminRezervacije(1L);

		return reservationData;
	}

	private Rezervacija buildRezervacija() {

		Korisnik korisnik = new Korisnik();
		korisnik.setImeKorisnika("Pero");
		korisnik.setPrezimeKorisnika("Peric");
		korisnik.setIdKorisnika(1L);

		UsluzniObjekt objekt = buildUsluzniObjekt();

		Rezervacija rez = new Rezervacija(1, LocalDate.of(2023, 1, 1),
				new Gost(1L, 'F', new Date(1), "097541283", korisnik),
				new Termin(1L, "10:00", "11:30", "01:30", objekt), new Stol(1L, 4, objekt, buildPozicija()), objekt,
				new Timestamp(System.currentTimeMillis()));

		return rez;
	}

	private UsluzniObjekt buildUsluzniObjekt() {
		UsluzniObjekt objekt = new UsluzniObjekt();
		objekt.setIdObjekta(1L);
		objekt.setAdresaObjekta("Ulica kneza Borne 12");
		objekt.setNazivObjekta("Kafic Mira");
		objekt.setGradObjekta("Zagreb");

		return objekt;
	}

	private Pozicija buildPozicija() {
		return new Pozicija(1L, "na sredini");
	}

}
