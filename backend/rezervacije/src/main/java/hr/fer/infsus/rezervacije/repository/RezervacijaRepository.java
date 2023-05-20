package hr.fer.infsus.rezervacije.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Rezervacija;

@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {
	Rezervacija findByGostIdGostaAndTerminIdTerminaAndStolIdStola(Long idGosta, Long idTermina, Long idStola);
	
}