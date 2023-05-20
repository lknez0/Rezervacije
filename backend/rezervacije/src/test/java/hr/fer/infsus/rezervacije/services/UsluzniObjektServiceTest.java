package hr.fer.infsus.rezervacije.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;
import hr.fer.infsus.rezervacije.repository.UsluzniObjektRepository;

class UsluzniObjektServiceTest {
    @Mock
    private UsluzniObjektRepository usluzniObjektRepository;

    @InjectMocks
    private UsluzniObjektService usluzniObjektService;

    public UsluzniObjektServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
    	UsluzniObjekt o1 = buildUsluzniObjekt();
    	UsluzniObjekt o2 = buildUsluzniObjekt();
    	o2.setIdObjekta(2L);
    	
        List<UsluzniObjekt> usluzniObjektList = List.of(o1, o2);
        when(usluzniObjektRepository.findAll()).thenReturn(usluzniObjektList);

        List<UsluzniObjekt> result = usluzniObjektService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetByIdFound() {
        UsluzniObjekt usluzniObjekt = buildUsluzniObjekt();
        when(usluzniObjektRepository.findById(1L)).thenReturn(Optional.of(usluzniObjekt));

        UsluzniObjekt result = usluzniObjektService.getById(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getIdObjekta());
    }

    @Test
    void testGetByIdNotFound() {
        when(usluzniObjektRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> usluzniObjektService.getById(1L));
    }

    @Test
    void testGetAllProjections() {
    	UsluzniObjekt o1 = buildUsluzniObjekt();
    	UsluzniObjekt o2 = buildUsluzniObjekt();
    	o2.setIdObjekta(2L);
    	
        List<UsluzniObjekt> usluzniObjektList = List.of(o1, o2);
        Map<Long, List<Termin>> terminMap = getTerminGrouped();
        
        
        when(usluzniObjektRepository.findAll()).thenReturn(usluzniObjektList);

 
        List<UsluzniObjektProjection> result = usluzniObjektService.getAllProjections(terminMap);

        assertNotNull(result);
        assertEquals(2, result.size());

        UsluzniObjektProjection projection1 = result.get(0);
        assertEquals(Long.valueOf(1L), projection1.getIdObjekta());
        assertEquals(Long.valueOf(1L), projection1.getTermini().get(0).getIdTermina());

        UsluzniObjektProjection projection2 = result.get(1);
        assertEquals(Long.valueOf(2L), projection2.getIdObjekta());
        assertEquals(Long.valueOf(2L), projection2.getTermini().get(0).getIdTermina());
    }
    
    private UsluzniObjekt buildUsluzniObjekt() {
		UsluzniObjekt objekt = new UsluzniObjekt();
		objekt.setIdObjekta(1L);
		objekt.setAdresaObjekta("Ulica kneza Borne 12");
		objekt.setNazivObjekta("Kafic Mira");
		objekt.setGradObjekta("Zagreb");

		return objekt;
	}
    
    private Map<Long, List<Termin>> getTerminGrouped() {
		List<Termin> termini = getTermini();

		Map<Long, List<Termin>> terminMap = new LinkedHashMap<>();
		terminMap.put(1L, List.of(termini.get(0)));
		terminMap.put(2L, List.of(termini.get(1)));

		return terminMap;
	}
    
    private List<Termin> getTermini() {
		Termin t1 = new Termin();
		t1.setIdTermina(1L);
		t1.setVrijemePocetka("10:00");
		t1.setVrijemeZavrsetka("11:30");

		Termin t2 = new Termin();
		t2.setIdTermina(2L);
		t2.setVrijemePocetka("13:00");
		t2.setVrijemeZavrsetka("14:30");

		return List.of(t1, t2);
	}
}