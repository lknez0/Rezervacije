package hr.fer.infsus.rezervacije.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.Korisnik;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.ReservationData;
import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.repository.RezervacijaRepository;

class RezervacijaServiceTest {

	@Mock
	private RezervacijaRepository rezervacijaRepository;

	@Mock
	private GostService gostService;

	@Mock
	private TerminService terminService;

	@Mock
	private UsluzniObjektService usluzniObjektService;

	@Mock
	private StolService stolService;

	@InjectMocks
	private RezervacijaService rezervacijaService;

	public RezervacijaServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllRezervacijas() {
		List<Rezervacija> rezervacije = List.of(buildRezervacija());
		when(rezervacijaRepository.findAll()).thenReturn(rezervacije);

		List<Rezervacija> result = rezervacijaService.getAllRezervacijas();

		assertEquals(rezervacije, result);
		verify(rezervacijaRepository).findAll();
	}

	@Test
	void testSaveRezervacija() {
		Rezervacija rezervacija = buildRezervacija();
		when(rezervacijaRepository.save(rezervacija)).thenReturn(rezervacija);

		Rezervacija savedRezervacija = rezervacijaService.saveRezervacija(rezervacija);

		assertEquals(rezervacija, savedRezervacija);
		verify(rezervacijaRepository).save(rezervacija);
	}

	@Test
	void testGetRezervacijaById() {
		Long idGosta = 1L;
		Long idTermina = 1L;
		Long idStola = 1L;
		Rezervacija expectedRezervacija = buildRezervacija();

		when(rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola))
				.thenReturn(expectedRezervacija);

		Rezervacija rezervacija = rezervacijaService.getRezervacijaById(idGosta, idTermina, idStola);

		assertEquals(expectedRezervacija, rezervacija);
		verify(rezervacijaRepository).findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola);
	}

	@Test
	void testGetRezervacijaById_LongId() {
		Long id = 100100001L;
		Long idGosta = 1L;
		Long idTermina = 1L;
		Long idStola = 1L;
		Rezervacija expectedRezervacija = buildRezervacija();

		when(rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola))
				.thenReturn(expectedRezervacija);

		Rezervacija rezervacija = rezervacijaService.getRezervacijaById(id);

		assertEquals(expectedRezervacija, rezervacija);
		verify(rezervacijaRepository).findByGostIdGostaAndTerminIdTerminaAndStolIdStola(idGosta, idTermina, idStola);
	}

	@Test
	void testDeleteRezervacija() {
		Long id = 100100001L;
		Rezervacija rez = buildRezervacija();
		
		when(rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(1L, 1L, 1L))
		.thenReturn(rez);
		
		rezervacijaService.deleteRezervacijaById(id);
		
		verify(rezervacijaRepository).delete(rez);
	}

	@Test
	void testBuildRezervacijaProjection() {
		Rezervacija rezervacija = buildRezervacija();

		Long id = rezervacija.getGost().getIdGosta() * 100000000 + rezervacija.getTermin().getIdTermina() * 100000
				+ rezervacija.getStol().getIdStola();

		Map<String, Object> projection = rezervacijaService.buildRezervacijaProjection(rezervacija);

		assertNotNull(projection);
		assertEquals(id, projection.get("id"));
	}

	@Test
	void testRequiresNewRezervacija() {
		Rezervacija rezervacija = buildRezervacija();
		ReservationData data = new ReservationData(2L, 1L, 1L, null, null, 1, null);

		boolean requiresNew = rezervacijaService.requiresNewRezervacija(rezervacija, data);

		assertTrue(requiresNew);
	}

	@Test
	void testRequiresNewRezervacijaFalse() {
		Rezervacija rezervacija = buildRezervacija();
		ReservationData data = new ReservationData(1L, 1L, 1L, null, null, 1, null);

		boolean requiresNew = rezervacijaService.requiresNewRezervacija(rezervacija, data);

		assertFalse(requiresNew);
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