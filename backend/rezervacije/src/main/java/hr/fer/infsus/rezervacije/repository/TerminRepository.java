package hr.fer.infsus.rezervacije.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Long>{
	@Query("SELECT t.idTermina AS idTermina, t.vrijemePocetka AS vrijemePocetka, t.vrijemeZavrsetka AS vrijemeZavrsetka FROM Termin t ")
    List<TerminProjection> findAllIdPocetakZavrsetak();

}
