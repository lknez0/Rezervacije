package hr.fer.infsus.rezervacije.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Korisnik;
import hr.fer.infsus.rezervacije.models.KorisnikProjection;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
	 @Query("SELECT k.imeKorisnika AS imeKorisnika, k.prezimeKorisnika AS prezimeKorisnika, g.idGosta AS idGosta " +
	            "FROM Korisnik k JOIN Gost g ON k.idKorisnika = g.idGosta")
	    List<KorisnikProjection> findAllImePrezimeIdGosta();
}