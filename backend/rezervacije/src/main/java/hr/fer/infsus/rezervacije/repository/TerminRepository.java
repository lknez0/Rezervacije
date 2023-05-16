package hr.fer.infsus.rezervacije.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Termin;
import hr.fer.infsus.rezervacije.models.TerminProjection;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Long>{
	@Query("SELECT t.usluzniObjekt.idObjekta as idObjekta, t.idTermina as idTermina, "
			+ "t.vrijemePocetka as vrijemePocetka, t.vrijemeZavrsetka as vrijemeZavrsetka "
			+ "FROM Termin t JOIN t.usluzniObjekt")
	List<TerminProjection> findAllIdPocetakZavrsetak();

}
