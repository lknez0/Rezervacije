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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
	private StolService stolService;
	
	@Mock
	private RezervacijaService rezervacijaService;
	

	@InjectMocks
	private RezervacijaController rezervacijaController;

	public RezervacijaControllerTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetCombinedData() {
		// Arrange
		List<GostProjection> gostList = getGostProjections();
		List<Pozicija> pozicijaList = getPozicijaList();
		Map<Long, List<TerminProjection>> terminMap = getTerminMap();
		List<UsluzniObjektProjection> usluzniObjektiList = getUsluzniObjektiList();

		when(gostService.getAllKorisniciImePrezimeIdGosta()).thenReturn(gostList);
		when(pozicijaService.getAllPozicija()).thenReturn(pozicijaList);
		when(terminService.getAllIdPocetakZavrsetakGrouped()).thenReturn(terminMap);
		when(usluzniObjektService.getAllUsluzniObjekti()).thenReturn(usluzniObjektiList);

		// Act
		ResponseEntity<?> response = rezervacijaController.getCombinedData();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		FormDataRezervacije data = (FormDataRezervacije) response.getBody();
		assertNotNull(data);
		assertEquals(gostList, data.getGosti());
		assertEquals(pozicijaList, data.getPozicije());
		assertEquals(terminMap, data.getTermini());
		assertEquals(usluzniObjektiList, data.getUsluzniObjekti());
		
	}
	
	@Test
    void createReservationSuccess() {
        // Arrange
		
		// Stvori "zahtjev"
        MultiValueMap<String, String> formData = createFormData();

        // Mock objekti
        Gost gost = new Gost();
        Stol stol = new Stol();
        stol.setBrojStolica(6);  // dovoljno stolica
        stol.setIdStola(2L);  // id za get kasnije
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();
        Rezervacija createdReservation = new Rezervacija();

        // Set up mock service methods
        when(gostService.getById(1L)).thenReturn(gost);
        when(gostService.updateBrojMobitela(1L, "097541283")).thenReturn(gost);
        when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenReturn(stol);
        when(usluzniObjektService.getById(2L)).thenReturn(usluzniObjekt);
        when(terminService.getById(1L)).thenReturn(termin);
        when(rezervacijaService.getRezervacijaById(1L, 1L, 2L)).thenReturn(createdReservation);
        doNothing().when(rezervacijaService).createRezervacija(any(Rezervacija.class));

        // Act
        ResponseEntity<?> response = rezervacijaController.createReservation(formData);

        // Assert
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(createdReservation, response.getBody());
        
        // Provjera s čime su pozvane metode za izmjenu
        verify(rezervacijaService).createRezervacija(any(Rezervacija.class));
        verify(gostService).updateBrojMobitela(1L, "097541283");
    }

	@Test
    void createReservationFail_NoAvailableTables() {
        // Arrange
		
		// Stvori "zahtjev"
        MultiValueMap<String, String> formData = createFormData();

        // Mock objekti
        Gost gost = new Gost();
        Stol stol = new Stol();
        stol.setBrojStolica(6);  // dovoljno stolica
        stol.setIdStola(2L);  // id za get kasnije
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();

        // Set up mock service methods
        when(gostService.getById(1L)).thenReturn(gost);
        when(gostService.updateBrojMobitela(1L, "097541283")).thenReturn(gost);
        when(usluzniObjektService.getById(2L)).thenReturn(usluzniObjekt);
        when(terminService.getById(4L)).thenReturn(termin);
        when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenThrow(new IllegalArgumentException("Nema dostupnih stolova"));

        // Act
        ResponseEntity<?> response = rezervacijaController.createReservation(formData);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error creating reservation: Nema dostupnih stolova", response.getBody());
    }
	
	@Test
    void createReservationFail_NotEnoughSeats() {
        // Arrange
		
		// Stvori "zahtjev"
        MultiValueMap<String, String> formData = createFormData();

        // Mock objekti
        Gost gost = new Gost();
        Stol stol = new Stol();
        stol.setBrojStolica(1);  // nedovoljno stolica
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();

        // Set up mock service methods
        when(gostService.getById(1L)).thenReturn(gost);
        when(gostService.updateBrojMobitela(1L, "097541283")).thenReturn(gost);
        when(usluzniObjektService.getById(2L)).thenReturn(usluzniObjekt);
        when(terminService.getById(4L)).thenReturn(termin);
        when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenThrow(new IllegalArgumentException("Nema dovoljno stolica"));

        // Act
        ResponseEntity<?> response = rezervacijaController.createReservation(formData);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error creating reservation: Nema dovoljno stolica", response.getBody());
    }
	
	@Test
	void updateReservationSuccess() {
	    // Arrange
	    
		// Stvori "zahtjev"
	    MultiValueMap<String, String> formData = createFormData();

	    // Mock objekti
        Rezervacija existingReservation = new Rezervacija();
        Gost gost = new Gost();
        Stol stol = new Stol();
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();
        
        stol.setIdStola(1L);
        stol.setPozicija(new Pozicija(1L, "sredina"));
        gost.setIdGosta(1L);
        termin.setIdTermina(1L);
        stol.setBrojStolica(6);  // dovoljno stolica
        
        existingReservation.setGost(gost);
        existingReservation.setStol(stol);
        existingReservation.setTermin(termin);
	    
	    // Set up mock service methods
        doNothing().when(rezervacijaService).deleteRezervacijaById(anyLong());
	    when(rezervacijaService.getRezervacijaById(anyLong())).thenReturn(existingReservation);
	    when(gostService.getById(anyLong())).thenReturn(gost);
	    when(gostService.updateBrojMobitela(anyLong(), anyString())).thenReturn(gost);
	    when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenReturn(stol);
	    when(usluzniObjektService.getById(anyLong())).thenReturn(usluzniObjekt);
	    when(terminService.getById(anyLong())).thenReturn(termin);
	    doNothing().when(rezervacijaService).updateRezervacija(any(Rezervacija.class));

	    // Act
	    ResponseEntity<?> response = rezervacijaController.updateReservation(1L, formData);

	    // Assert
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(existingReservation, response.getBody());  
	    
	    // Verify service methods were called
	    verify(gostService).updateBrojMobitela(anyLong(), anyString());
	}
	
	@Test
	void updateReservationSuccess_IdChange() {
	    // Arrange
	    
		// Stvori "zahtjev"
	    MultiValueMap<String, String> formData = createFormData();

	    // Mock objekti
        Rezervacija existingReservation = new Rezervacija();
        Gost gost = new Gost();
        Stol stol = new Stol();
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();
        
        stol.setIdStola(1L);
        stol.setPozicija(new Pozicija(1L, "sredina"));
        gost.setIdGosta(2L);  // novi ključ!
        termin.setIdTermina(1L);
        stol.setBrojStolica(6);  // dovoljno stolica
        
        existingReservation.setGost(gost);
        existingReservation.setStol(stol);
        existingReservation.setTermin(termin);
	    
	    // Set up mock service methods
        doNothing().when(rezervacijaService).deleteRezervacijaById(anyLong());
	    when(rezervacijaService.getRezervacijaById(anyLong())).thenReturn(existingReservation);
	    when(gostService.getById(anyLong())).thenReturn(gost);
	    when(gostService.updateBrojMobitela(anyLong(), anyString())).thenReturn(gost);
	    when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenReturn(stol);
	    when(usluzniObjektService.getById(anyLong())).thenReturn(usluzniObjekt);
	    when(terminService.getById(anyLong())).thenReturn(termin);
	    doNothing().when(rezervacijaService).updateRezervacija(any(Rezervacija.class));

	    // Act
	    ResponseEntity<?> response = rezervacijaController.updateReservation(1L, formData);

	    // Assert
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertNotEquals(existingReservation, response.getBody());  // jer promjena primary keya
	    
	    // Verify service methods were called
	    verify(gostService).updateBrojMobitela(anyLong(), anyString());
	}
	
	@Test
	void updateReservationFail_NoReservation() {
	    // Arrange
	    
	    // Stvori "zahtjev"
	    MultiValueMap<String, String> formData = createFormData();

	    
	    when(rezervacijaService.getRezervacijaById(anyLong())).thenReturn(null);

	    // Act
	    ResponseEntity<?> response = rezervacijaController.updateReservation(1L, formData);

	    // Assert
	    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	    assertEquals("Error updating reservation: Nepoznata rezervacija", response.getBody());
	}
	
	@Test
    void updateReservationFail_NoAvailableTables() {
        // Arrange
		
		// Stvori "zahtjev"
        MultiValueMap<String, String> formData = createFormData();

        // Mock objekti
        Rezervacija existingReservation = new Rezervacija();
        Gost gost = new Gost();
        Stol stol = new Stol();
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();
        
        stol.setIdStola(1L);
        stol.setPozicija(new Pozicija(1L, "sredina"));
        gost.setIdGosta(1L);
        termin.setIdTermina(1L);
        stol.setBrojStolica(1);  // nedovoljno stolica
        
        existingReservation.setGost(gost);
        existingReservation.setStol(stol);
        existingReservation.setTermin(termin);

        // Set up mock service methods
        when(rezervacijaService.getRezervacijaById(anyLong())).thenReturn(existingReservation);
        when(gostService.getById(1L)).thenReturn(gost);
        when(gostService.updateBrojMobitela(1L, "097541283")).thenReturn(gost);
        when(usluzniObjektService.getById(2L)).thenReturn(usluzniObjekt);
        when(terminService.getById(4L)).thenReturn(termin);
        when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenThrow(new IllegalArgumentException("Nema dostupnih stolova"));

        // Act
        ResponseEntity<?> response = rezervacijaController.updateReservation(1L, formData);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error updating reservation: Nema dostupnih stolova", response.getBody());
    }
	
	@Test
    void updateReservationFail_NotEnoughSeats() {
        // Arrange
		
		// Stvori "zahtjev"
        MultiValueMap<String, String> formData = createFormData();

        // Mock objekti
        Rezervacija existingReservation = new Rezervacija();
        Gost gost = new Gost();
        Stol stol = new Stol();
        UsluzniObjekt usluzniObjekt = new UsluzniObjekt();
        Termin termin = new Termin();
        
        stol.setIdStola(1L);
        stol.setPozicija(new Pozicija(1L, "sredina"));
        gost.setIdGosta(1L);
        termin.setIdTermina(1L);
        stol.setBrojStolica(1);  // nedovoljno stolica
        
        existingReservation.setGost(gost);
        existingReservation.setStol(stol);
        existingReservation.setTermin(termin);
        // Set up mock service methods
        when(rezervacijaService.getRezervacijaById(anyLong())).thenReturn(existingReservation);
        when(gostService.getById(1L)).thenReturn(gost);
        when(gostService.updateBrojMobitela(1L, "097541283")).thenReturn(gost);
        when(usluzniObjektService.getById(1L)).thenReturn(usluzniObjekt);
        when(terminService.getById(1L)).thenReturn(termin);
        when(stolService.getAvailableStol(anyLong(), anyLong(), any(LocalDate.class), anyInt())).thenThrow(new IllegalArgumentException("Nema dovoljno stolica"));

        // Act
        ResponseEntity<?> response = rezervacijaController.updateReservation(1L, formData);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error updating reservation: Nema dovoljno stolica", response.getBody());
    }
	
	@Test
	void deleteRezervacijaById() {
	    // Arrange
	    Long rezervacijaId = 1L;
	    doNothing().when(rezervacijaService).deleteRezervacijaById(anyLong());
	    
	    // Act
	    ResponseEntity<?> response = rezervacijaController.deleteRezervacijaById(rezervacijaId);

	    // Assert
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
	
	private Map<Long, List<TerminProjection>> getTerminMap() {

		Map<Long, List<TerminProjection>> terminMap = new LinkedHashMap<>();
		terminMap.put(1L, getTerminList());
		terminMap.put(2L, getTerminList());
		return null;
	}

	private List<TerminProjection> getTerminList() {
		TerminProjection termin1 = new TerminProjection() {
			@Override
			public Long getIdTermina() {
				return 1L;
			}

			@Override
			public String getVrijemePocetka() {
				return "10:00";
			}

			@Override
			public String getVrijemeZavrsetka() {
				return "11:30";
			}

			@Override
			public Long getIdObjekta() {
				return 1L;
			}
		};

		TerminProjection termin2 = new TerminProjection() {
			@Override
			public Long getIdTermina() {
				return 2L;
			}

			@Override
			public String getVrijemePocetka() {
				return "13:00";
			}

			@Override
			public String getVrijemeZavrsetka() {
				return "14:30";
			}

			@Override
			public Long getIdObjekta() {
				return 2L;
			}
		};

		return Arrays.asList(termin1, termin2);
	}

	private List<UsluzniObjektProjection> getUsluzniObjektiList() {
		UsluzniObjektProjection objekt1 = new UsluzniObjektProjection() {
			@Override
			public Long getIdObjekta() {
				return 1L;
			}

			@Override
			public String getNazivObjekta() {
				return "Boogie Lab Zagreb";
			}

			@Override
			public String getAdresaObjekta() {
				return "Ulica kneza Borne 26";
			}

			@Override
			public String getGradObjekta() {
				return "Zagreb";
			}
		};

		UsluzniObjektProjection objekt2 = new UsluzniObjektProjection() {
			@Override
			public Long getIdObjekta() {
				return 2L;
			}

			@Override
			public String getNazivObjekta() {
				return "Leggiero";
			}

			@Override
			public String getAdresaObjekta() {
				return "Maksimirsko naselje IV 25";
			}

			@Override
			public String getGradObjekta() {
				return "Zagreb";
			}
		};

		return Arrays.asList(objekt1, objekt2);
	}
	
	private MultiValueMap<String, String> createFormData() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id_gosta", "1");
        formData.add("broj_mobitela_gosta", "097541283");
        formData.add("id_objekta", "2");
        formData.add("datum_rezervacije", "01.01.2023.");
        formData.add("broj_osoba", "4");
        formData.add("vrsta_stola", "1");
        formData.add("termin_rezervacija", "1");
        
        return formData;
	}

}
