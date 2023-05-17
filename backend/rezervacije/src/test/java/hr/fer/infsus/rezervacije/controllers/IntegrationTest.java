package hr.fer.infsus.rezervacije.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.models.Korisnik;
import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.models.Stol;
import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.repository.GostRepository;
import hr.fer.infsus.rezervacije.repository.PozicijaRepository;
import hr.fer.infsus.rezervacije.repository.RezervacijaRepository;
import hr.fer.infsus.rezervacije.repository.StolRepository;
import hr.fer.infsus.rezervacije.repository.TerminRepository;
import hr.fer.infsus.rezervacije.repository.UsluzniObjektRepository;
import hr.fer.infsus.rezervacije.services.GostService;
import hr.fer.infsus.rezervacije.services.PozicijaService;
import hr.fer.infsus.rezervacije.services.RezervacijaService;
import hr.fer.infsus.rezervacije.services.StolService;
import hr.fer.infsus.rezervacije.services.TerminService;
import hr.fer.infsus.rezervacije.services.UsluzniObjektService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IntegrationTest {

	@Mock
	private GostRepository gostRepository;

	@Mock
	private RezervacijaRepository rezervacijaRepository;

	@Mock
	private PozicijaRepository pozicijaRepository;

	@Mock
	private UsluzniObjektRepository usluzniObjektRepository;

	@Mock
	private TerminRepository terminRepository;

	@Mock
	private StolRepository stolRepository;

	@InjectMocks
	private GostService gostService;

	@InjectMocks
	private PozicijaService pozicijaService;

	@InjectMocks
	private TerminService terminService;

	@InjectMocks
	private UsluzniObjektService usluzniObjektService;

	@InjectMocks
	private RezervacijaService rezervacijaService;

	@InjectMocks
	private StolService stolService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RezervacijaController rezervacijaController;
	

	@Test
	public void testGetCombinedData() throws Exception {
		List<GostProjection> gosti = buildGosti();
		List<Pozicija> pozicije = Arrays.asList(buildPozicija());
		List<UsluzniObjektProjection> usluzniObjektiList = buildUsluzniObjektProjections();
		List<TerminProjection> termini = buildTerminProjections();

		when(gostRepository.findAllImePrezimeIdGosta()).thenReturn(gosti);
		when(pozicijaRepository.findAll()).thenReturn(pozicije);
		when(usluzniObjektRepository.findAllUsluzniObjekti()).thenReturn(usluzniObjektiList);
		when(terminRepository.findAllIdPocetakZavrsetak()).thenReturn(termini);

		ReflectionTestUtils.setField(rezervacijaController, "gostService", gostService);
		ReflectionTestUtils.setField(rezervacijaController, "pozicijaService", pozicijaService);
		ReflectionTestUtils.setField(rezervacijaController, "usluzniObjektService", usluzniObjektService);
		ReflectionTestUtils.setField(rezervacijaController, "terminService", terminService);

		mockMvc = MockMvcBuilders.standaloneSetup(rezervacijaController).build();

		mockMvc.perform(MockMvcRequestBuilders.get("/rezervacije").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.gosti.length()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$.termini.length()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pozicije.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.usluzniObjekti.length()").value(2));

	}

	@Test
	public void testGetRezervacijas() throws Exception {
		Rezervacija rez = buildRezervacija();

		when(rezervacijaRepository.findAll()).thenReturn(Arrays.asList(rez));

		ReflectionTestUtils.setField(rezervacijaController, "rezervacijaService", rezervacijaService);

		mockMvc.perform(MockMvcRequestBuilders.get("/rezervacije/all").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nazivObjekta").value("Kafic Mira"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].adresaObjekta").value("Ulica kneza Borne 12"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].imeGosta").value("Pero Peric"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].brojMobitelaGosta").value("123456789"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].vrijemePocetka").value("14:00"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].vrijemeZavrsetka").value("15:00"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].datumRezervacije").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].danUTjednu").value("srijeda"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].brojOsoba").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].brojStolica").value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].pozicijaStola").value("na sredini"));
	}

	@Test
	public void testCreateReservation() throws Exception {
		MultiValueMap<String, String> formData = buildFormData();
		Rezervacija createdReservation = buildRezervacija();

		UsluzniObjekt objekt = buildUsluzniObjekt();
		Stol stol = new Stol(1L, 4, objekt, buildPozicija());
		Termin termin = new Termin(1L, "14:00", "15:00", "01:00", objekt);
		Gost gost = new Gost();
		gost.setIdGosta(1L);

		
		when(gostRepository.findById(1L)).thenReturn(Optional.of(gost));
		when(usluzniObjektRepository.findById(1L)).thenReturn(Optional.of(objekt));
		when(terminRepository.findById(1L)).thenReturn(Optional.of(termin));
		when(stolRepository.findAvailableStolByTerminAndDatumRezervacijeAndPozicija(1L, LocalDate.of(2023, 5, 17), 1L))
				.thenReturn(List.of(stol));
		when(rezervacijaRepository.save(any(Rezervacija.class))).thenReturn(createdReservation);
		when(rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(1L, 1L, 1L))
				.thenReturn(createdReservation);

		ReflectionTestUtils.setField(rezervacijaController, "rezervacijaService", rezervacijaService);
		ReflectionTestUtils.setField(rezervacijaController, "gostService", gostService);
		ReflectionTestUtils.setField(rezervacijaController, "stolService", stolService);
		ReflectionTestUtils.setField(rezervacijaController, "usluzniObjektService", usluzniObjektService);
		ReflectionTestUtils.setField(rezervacijaController, "terminService", terminService);

		mockMvc.perform(MockMvcRequestBuilders.post("/rezervacije").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(getFormDataString(formData))).andExpect(MockMvcResultMatchers.status().isCreated());

		verify(gostRepository, atLeastOnce()).findById(1L);
		verify(usluzniObjektRepository).findById(1L);
		verify(terminRepository).findById(1L);
		verify(stolRepository).findAvailableStolByTerminAndDatumRezervacijeAndPozicija(1L, LocalDate.of(2023, 5, 17),
				1L);
		verify(rezervacijaRepository).save(any(Rezervacija.class));
		verify(rezervacijaRepository).findByGostIdGostaAndTerminIdTerminaAndStolIdStola(1L, 1L, 1L);
	}

	@Test
	public void testUpdateReservation() throws Exception {
		MultiValueMap<String, String> formData = buildFormData();
		formData.replace("broj_osoba", List.of("3"));
		
		Rezervacija createdReservation = buildRezervacija();
		Gost gost = new Gost();
		gost.setIdGosta(1L);

		when(gostRepository.findById(1L)).thenReturn(Optional.of(gost));
		when(rezervacijaRepository.save(any(Rezervacija.class))).thenReturn(createdReservation);
		when(rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(any(), any(), any()))
				.thenReturn(createdReservation);

		ReflectionTestUtils.setField(rezervacijaController, "rezervacijaService", rezervacijaService);
		ReflectionTestUtils.setField(rezervacijaController, "gostService", gostService);
		ReflectionTestUtils.setField(rezervacijaController, "stolService", stolService);
		ReflectionTestUtils.setField(rezervacijaController, "usluzniObjektService", usluzniObjektService);
		ReflectionTestUtils.setField(rezervacijaController, "terminService", terminService);

		mockMvc.perform(MockMvcRequestBuilders.put("/rezervacije/101001")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getFormDataString(formData)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(gostRepository, atLeastOnce()).findById(1L);
		verify(rezervacijaRepository).save(any(Rezervacija.class));
		verify(rezervacijaRepository).findByGostIdGostaAndTerminIdTerminaAndStolIdStola(1L, 1L, 1L);
	}

	@Test
	public void testDeleteReservation() throws Exception {
		Rezervacija rezervacija = buildRezervacija();

		when(rezervacijaRepository.findByGostIdGostaAndTerminIdTerminaAndStolIdStola(any(), any(), any()))
				.thenReturn(rezervacija);
		doNothing().when(rezervacijaRepository).delete(rezervacija);

		ReflectionTestUtils.setField(rezervacijaController, "rezervacijaService", rezervacijaService);

		mockMvc.perform(MockMvcRequestBuilders
					.delete("/rezervacije/122876")
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		verify(rezervacijaRepository, times(1)).delete(rezervacija);
	}
	
	
	

	private Rezervacija buildRezervacija() {

		Korisnik korisnik = new Korisnik();
		korisnik.setImeKorisnika("Pero");
		korisnik.setPrezimeKorisnika("Peric");
		korisnik.setIdKorisnika(1L);

		UsluzniObjekt objekt = buildUsluzniObjekt();

		Rezervacija rez = new Rezervacija(2, LocalDate.of(2023, 5, 17),
				new Gost(1L, 'F', new Date(1), "123456789", korisnik),
				new Termin(1L, "14:00", "15:00", "01:00", objekt), new Stol(1L, 4, objekt, buildPozicija()), objekt,
				new Timestamp(System.currentTimeMillis()));

		return rez;
	}

	private Pozicija buildPozicija() {
		return new Pozicija(1L, "na sredini");
	}

	private List<UsluzniObjektProjection> buildUsluzniObjektProjections() {
		UsluzniObjektProjection uop1 = new UsluzniObjektProjection() {

			@Override
			public String getNazivObjekta() {
				return "Leggiero";
			}

			@Override
			public Long getIdObjekta() {
				return 1L;
			}

			@Override
			public String getGradObjekta() {
				return "Zagreb";
			}

			@Override
			public String getAdresaObjekta() {
				return "Maksimirsko naselje IV 25";
			}
		};

		UsluzniObjektProjection uop2 = new UsluzniObjektProjection() {

			@Override
			public String getNazivObjekta() {
				return "Boogie Lab Zagreb";
			}

			@Override
			public Long getIdObjekta() {
				return 2L;
			}

			@Override
			public String getGradObjekta() {
				return "Zagreb";
			}

			@Override
			public String getAdresaObjekta() {
				return "Ulica kneza Borne 26";
			}
		};

		return Arrays.asList(uop1, uop2);
	}

	public List<TerminProjection> buildTerminProjections() {
		TerminProjection tp1 = new TerminProjection() {

			@Override
			public String getVrijemeZavrsetka() {
				return "16:00";
			}

			@Override
			public String getVrijemePocetka() {
				return "14:00";
			}

			@Override
			public Long getIdTermina() {
				return 1L;
			}

			@Override
			public Long getIdObjekta() {
				return 1L;
			}
		};

		TerminProjection tp2 = new TerminProjection() {

			@Override
			public String getVrijemeZavrsetka() {
				return "15:00";
			}

			@Override
			public String getVrijemePocetka() {
				return "13:00";
			}

			@Override
			public Long getIdTermina() {
				return 2L;
			}

			@Override
			public Long getIdObjekta() {
				return 2L;
			}
		};

		return Arrays.asList(tp1, tp2);
	}

	private List<GostProjection> buildGosti() {
		GostProjection gost1 = new GostProjection() {

			@Override
			public String getPrezimeKorisnika() {
				return "Polo";
			}

			@Override
			public String getImeKorisnika() {
				return "Marco";
			}

			@Override
			public Long getIdGosta() {
				return 1L;
			}
		};

		GostProjection gost2 = new GostProjection() {

			@Override
			public String getPrezimeKorisnika() {
				return "Peric";
			}

			@Override
			public String getImeKorisnika() {
				return "Pero";
			}

			@Override
			public Long getIdGosta() {
				return 2L;
			}
		};

		return Arrays.asList(gost1, gost2);
	}

	private UsluzniObjekt buildUsluzniObjekt() {
		UsluzniObjekt objekt = new UsluzniObjekt();
		objekt.setIdObjekta(1L);
		objekt.setAdresaObjekta("Ulica kneza Borne 12");
		objekt.setNazivObjekta("Kafic Mira");
		objekt.setGradObjekta("Zagreb");

		return objekt;
	}

	private MultiValueMap<String, String> buildFormData() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("id_gosta", "1");
		formData.add("broj_mobitela_gosta", "123456789");
		formData.add("id_objekta", "1");
		formData.add("datum_rezervacije", "17.05.2023.");
		formData.add("broj_osoba", "2");
		formData.add("vrsta_stola", "1");
		formData.add("termin_rezervacija", "1");

		return formData;
	}

	private String getFormDataString(MultiValueMap<String, String> formData) {
		StringBuilder builder = new StringBuilder();
		for (String key : formData.keySet()) {
			for (String value : formData.get(key)) {
				if (builder.length() > 0) {
					builder.append("&");
				}
				builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
				builder.append("=");
				builder.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
			}
		}
		return builder.toString();
	}
}
