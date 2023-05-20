package hr.fer.infsus.rezervacije.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.repository.StolRepository;

class StolServiceTest {
	@Mock
	private StolRepository stolRepository;

	@InjectMocks
	private StolService stolService;

	
	public StolServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAvailableStolAvailableStol() {
		List<Stol> dostupniStolovi = buildStolovi();
		
		when(stolRepository
				.findAvailableStolByTerminAndDatumRezervacijeAndPozicija(anyLong(), any(LocalDate.class),anyLong()))
		        .thenReturn(dostupniStolovi);

		Stol result = stolService.getAvailableStol(1L, 1L, LocalDate.now(), 2);

		assertNotNull(result);
		assertEquals(Integer.valueOf(2), result.getBrojStolica());
	}

	@Test
	void testGetAvailableStolNotAvailableStol() {
		when(stolRepository
				.findAvailableStolByTerminAndDatumRezervacijeAndPozicija(anyLong(), any(LocalDate.class), anyLong()))
				.thenReturn(null);

		assertThrows(IllegalArgumentException.class, () -> stolService.getAvailableStol(1L, 1L, LocalDate.now(), 8));
	}

	@Test
	void testGetAvailableStoPremaloStolica() {
		List<Stol> dostupniStolovi = buildStolovi();
		when(stolRepository
			.findAvailableStolByTerminAndDatumRezervacijeAndPozicija(anyLong(), any(LocalDate.class), anyLong()))
			.thenReturn(dostupniStolovi);

		
		assertThrows(IllegalArgumentException.class, () -> stolService.getAvailableStol(1L, 1L, LocalDate.now(), 20));
	}
	
	private List<Stol> buildStolovi() {
		UsluzniObjekt objekt = buildUsluzniObjekt();
		Pozicija pozicija = buildPozicija();
		
		Stol stol1 = new Stol(1L, 2, objekt, pozicija);
		Stol stol2 = new Stol(2L, 3, objekt, pozicija);
		
		return List.of(stol1, stol2);
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