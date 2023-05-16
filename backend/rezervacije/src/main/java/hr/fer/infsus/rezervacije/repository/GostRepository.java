package hr.fer.infsus.rezervacije.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;

@Repository
public interface GostRepository extends JpaRepository<Gost, Long> {
	 @Query("SELECT k.imeKorisnika AS imeKorisnika, k.prezimeKorisnika AS prezimeKorisnika, g.idGosta AS idGosta " +
	            "FROM Korisnik k JOIN Gost g ON k.idKorisnika = g.idGosta")
	    List<GostProjection> findAllImePrezimeIdGosta();
	 
	 
}