package hr.fer.infsus.rezervacije.controllers;

import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.infsus.rezervacije.models.Rezervacija;
import hr.fer.infsus.rezervacije.services.RezervacijaService;

@RestController
@RequestMapping("/rezervacija")
public class RezervacijaController {

    @Autowired
    private RezervacijaService rezervacijaService;

    @GetMapping
    public ResponseEntity<?> getRezervacijas() {
    	List<Rezervacija> rezervacije = rezervacijaService.getAllRezervacijas();
    	
    	
    	List<Map<String, Object>> body = new LinkedList<>();
    	
    	for(var rez : rezervacije) {
    		Map<String, Object> data = new LinkedHashMap<>();
    		
    		Long id = rez.getGost().getIdGosta() * 100000 
    				+ rez.getTermin().getIdTermina() * 1000
    				+ rez.getStol().getIdStola();
    		
    		data.put("id", id);
    		data.put("nazivObjekta", rez.getUsluzniObjekt().getNazivObjekta());
    		data.put("adresaObjekta", rez.getUsluzniObjekt().getAdresaObjekta());
    		data.put("imeGosta", rez.getGost().getKorisnik().getImeKorisnika() + " " 
    		+ rez.getGost().getKorisnik().getPrezimeKorisnika());
    		data.put("brojMobitelaGosta", rez.getGost().getBrojMobitela());
    		data.put("vrijemePocetka", rez.getTermin().getVrijemePocetka());
    		data.put("vrijemeZavrsetka", rez.getTermin().getVrijemeZavrsetka());
    		data.put("datumRezervacije", rez.getDatumRezervacije());
    		data.put("danUTjednu", rez.getDatumRezervacije().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("hr", "HR")));
    		data.put("brojStolica", rez.getStol().getBrojStolica());
    		data.put("pozicijaStola", rez.getStol().getPozicija().getNazivPozicije());
    		
    		body.add(data);
    		
    	}
    	
        return ResponseEntity.ok(body);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getRezervacijaById(@PathVariable Long id) {
        Rezervacija rezervacija = rezervacijaService.getRezervacijaById(id);
        if (rezervacija != null) {
            Map<String, Object> responseData = new LinkedHashMap<>();
            responseData.put("id", id);
            responseData.put("nazivObjekta", rezervacija.getUsluzniObjekt().getNazivObjekta());
            responseData.put("adresaObjekta", rezervacija.getUsluzniObjekt().getAdresaObjekta());
            responseData.put("imeGosta", rezervacija.getGost().getKorisnik().getImeKorisnika() + " " + rezervacija.getGost().getKorisnik().getPrezimeKorisnika());
            responseData.put("brojMobitelaGosta", rezervacija.getGost().getBrojMobitela());
            responseData.put("vrijemePocetka", rezervacija.getTermin().getVrijemePocetka());
            responseData.put("vrijemeZavrsetka", rezervacija.getTermin().getVrijemeZavrsetka());
            responseData.put("datumRezervacije", rezervacija.getDatumRezervacije());
            responseData.put("danUTjednu", rezervacija.getDatumRezervacije().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("hr", "HR")));
            responseData.put("brojStolica", rezervacija.getStol().getBrojStolica());
            responseData.put("pozicijaStola", rezervacija.getStol().getPozicija().getNazivPozicije());
            
            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRezervacijaById(@PathVariable Long id) {
        rezervacijaService.deleteRezervacijaById(id);
        return ResponseEntity.noContent().build();
    }
}