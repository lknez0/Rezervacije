package hr.fer.infsus.rezervacije.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Stol;

@Repository
public interface StolRepository extends JpaRepository<Stol, Long> {

	@Query("SELECT s FROM Stol s WHERE s.idStola NOT IN " +
		       "(SELECT r.stol.idStola FROM Rezervacija r " +
		       "WHERE r.termin.idTermina = :terminId " +
		       "AND r.datumRezervacije = :datumRezervacije) " +
		       "AND s.pozicija.idPozicije = :idPozicije")
	    List<Stol> findAvailableStolByTerminAndDatumRezervacijeAndPozicija(
	            @Param("terminId") Long terminId,
	            @Param("datumRezervacije") LocalDate datumRezervacije,
	            @Param("idPozicije") Long idPozicije);
}