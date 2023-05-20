package hr.fer.infsus.rezervacije.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.infsus.rezervacije.models.Pozicija;
import hr.fer.infsus.rezervacije.repository.PozicijaRepository;

class PozicijaServiceTest {
    @Mock
    private PozicijaRepository pozicijaRepository;

    @InjectMocks
    private PozicijaService pozicijaService;

    public PozicijaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPozicija() {
        List<Pozicija> pozicije = new LinkedList<>();
        pozicije.add(new Pozicija(1L, "na sredini"));
        pozicije.add(new Pozicija(2L, "uz prozor"));

        when(pozicijaRepository.findAll()).thenReturn(pozicije);

        List<Pozicija> result = pozicijaService.getAllPozicija();

        assertEquals(pozicije, result);
    }
}