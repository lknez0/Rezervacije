package hr.fer.infsus.rezervacije.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.Pozicija;

@Repository
public interface PozicijaRepository extends JpaRepository<Pozicija, Long>{

}
