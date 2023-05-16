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
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.services.GostService;
import hr.fer.infsus.rezervacije.services.PozicijaService;
import hr.fer.infsus.rezervacije.services.TerminService;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
class FormDataControllerTest {

	@Mock
	private GostService gostService;

	@Mock
	private PozicijaService pozicijaService;

	@Mock
	private TerminService terminService;

	@Mock
	private UsluzniObjektService usluzniObjektService;

	@InjectMocks
	private FormDataController formDataController;

	public FormDataControllerTest() {
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
		ResponseEntity<?> response = formDataController.getCombinedData();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		FormDataRezervacije data = (FormDataRezervacije) response.getBody();
		assertNotNull(data);
		assertEquals(gostList, data.getGosti());
		assertEquals(pozicijaList, data.getPozicije());
		assertEquals(terminMap, data.getTermini());
		assertEquals(usluzniObjektiList, data.getUsluzniObjekti());
		
	}

	private Map<Long, List<TerminProjection>> getTerminMap() {

		Map<Long, List<TerminProjection>> terminMap = new LinkedHashMap<>();
		terminMap.put(1L, getTerminList());
		terminMap.put(2L, getTerminList());
		return null;
	}

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

}
