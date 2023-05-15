package hr.fer.infsus.rezervacije.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.KorisnikProjection;
import hr.fer.infsus.rezervacije.repository.KorisnikRepository;

@Service
public class KorisnikService {

	@Autowired
    private KorisnikRepository korisnikRepository;

    public List<KorisnikProjection> getAllKorisniciImePrezimeIdGosta() {
        return korisnikRepository.findAllImePrezimeIdGosta();
    }

}