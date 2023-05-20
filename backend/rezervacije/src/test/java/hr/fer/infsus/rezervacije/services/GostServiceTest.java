package hr.fer.infsus.rezervacije.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.repository.GostRepository;

class GostServiceTest {

    @Mock
    private GostRepository gostRepository;
    
    @InjectMocks
    private GostService gostService;
    
    public GostServiceTest() {
		MockitoAnnotations.openMocks(this);
	}


    @Test
    void testGetAllKorisniciImePrezimeIdGosta() {
    	List<GostProjection> gosti = getGostProjections();
    	
        when(gostRepository.findAllImePrezimeIdGosta()).thenReturn(gosti);
        
        List<GostProjection> result = gostService.getAllKorisniciImePrezimeIdGosta();

        assertNotNull(result);
        assertEquals(gosti, result);
    }

    @Test
    void testGetById_ValidId_ReturnsGost() {
        Long id = 1L;
        Gost gost = new Gost();
        gost.setIdGosta(id);

        when(gostRepository.findById(id)).thenReturn(Optional.of(gost));


        Gost result = gostService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getIdGosta());
    }

    @Test
    void testGetById_InvalidId() {
        Long id = 1L;

        when(gostRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gostService.getById(id));
    }

    @Test
    void testUpdateNumberIfNeededUpdate() {
        String newBrojMobitela = "123456789";
        Gost existingGost = new Gost();
        existingGost.setIdGosta(1L);
        existingGost.setBrojMobitela("098726492");
       
        when(gostRepository.findById(1L)).thenReturn(Optional.of(existingGost));
        when(gostRepository.save(any(Gost.class))).thenReturn(existingGost);

        Gost result = gostService.updateNumberIfNeeded(1L, newBrojMobitela);
        
        assertNotNull(result);
        assertEquals(newBrojMobitela, result.getBrojMobitela());
        
        verify(gostRepository).save(result);
    }

    @Test
    void testUpdateNumberIfNeededNoUpdate() {
    	String newBrojMobitela = "123456789";
        Gost existingGost = new Gost();
        existingGost.setIdGosta(1L);
        existingGost.setBrojMobitela("123456789");
       
        when(gostRepository.findById(1L)).thenReturn(Optional.of(existingGost));
        when(gostRepository.save(any(Gost.class))).thenReturn(existingGost);
        
        Gost result = gostService.updateNumberIfNeeded(1L, newBrojMobitela);

        assertNotNull(result);
        assertEquals(existingGost.getBrojMobitela(), "123456789");
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


}
