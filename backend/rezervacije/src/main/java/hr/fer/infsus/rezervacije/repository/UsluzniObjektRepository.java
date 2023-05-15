package hr.fer.infsus.rezervacije.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hr.fer.infsus.rezervacije.models.UsluzniObjekt;
import hr.fer.infsus.rezervacije.models.UsluzniObjektProjection;

@Repository
public interface UsluzniObjektRepository extends JpaRepository<UsluzniObjekt, Long>{
	 @Query("SELECT u.idObjekta AS idObjekta, u.nazivObjekta AS nazivObjekta, u.adresaObjekta AS adresaObjekta, u.gradObjekta AS gradObjekta FROM UsluzniObjekt u")
	    List<UsluzniObjektProjection> findAllUsluzniObjekti();

}
