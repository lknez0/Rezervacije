package hr.fer.infsus.rezervacije.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Termin;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Long>{
}
