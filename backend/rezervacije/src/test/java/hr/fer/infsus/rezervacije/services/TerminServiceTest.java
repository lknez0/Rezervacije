package hr.fer.infsus.rezervacije.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.repository.TerminRepository;

class TerminServiceTest {
    @Mock
    private TerminRepository terminRepository;

    @InjectMocks
    private TerminService terminService;

    public TerminServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByIdFound() {
        Termin termin = new Termin();
        termin.setIdTermina(1L);
        when(terminRepository.findById(1L)).thenReturn(Optional.of(termin));

        Termin result = terminService.getById(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getIdTermina());
    }

    @Test
    void testGetByIdNotFound() {
        when(terminRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> terminService.getById(1L));
    }

    @Test
    void testGetAllTerminGrouped() {
        List<Termin> terminList = getTermini();
        
        when(terminRepository.findAll()).thenReturn(terminList);

        Map<Long, List<Termin>> result = terminService.getAllTerminGrouped();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(1L));
        assertEquals(2, result.get(1L).size());
    }
    
    private List<Termin> getTermini() {
    	UsluzniObjekt objekt = buildUsluzniObjekt();
    	
		Termin t1 = new Termin();
		t1.setIdTermina(1L);
		t1.setVrijemePocetka("10:00");
		t1.setVrijemeZavrsetka("11:30");
		t1.setUsluzniObjekt(objekt);

		Termin t2 = new Termin();
		t1.setIdTermina(2L);
		t1.setVrijemePocetka("13:00");
		t1.setVrijemeZavrsetka("14:30");
		t2.setUsluzniObjekt(objekt);

		return List.of(t1, t2);
	}
    
    private UsluzniObjekt buildUsluzniObjekt() {
		UsluzniObjekt objekt = new UsluzniObjekt();
		objekt.setIdObjekta(1L);
		objekt.setAdresaObjekta("Ulica kneza Borne 12");
		objekt.setNazivObjekta("Kafic Mira");
		objekt.setGradObjekta("Zagreb");

		return objekt;
	}

}