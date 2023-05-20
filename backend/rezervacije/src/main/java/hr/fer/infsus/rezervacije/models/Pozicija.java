package hr.fer.infsus.rezervacije.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "pozicija")
public class Pozicija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pozicije")
    private Long idPozicije;
    
    @Column(name = "naziv_pozicije")
    private String nazivPozicije;

	public Long getIdPozicije() {
		return idPozicije;
	}

	public void setIdPozicije(Long idPozicije) {
		this.idPozicije = idPozicije;
	}

	public String getNazivPozicije() {
		return nazivPozicije;
	}

	public void setNazivPozicije(String nazivPozicije) {
		this.nazivPozicije = nazivPozicije;
	}

	public Pozicija(Long idPozicije, String nazivPozicije) {
		this.idPozicije = idPozicije;
		this.nazivPozicije = nazivPozicije;
	}

	public Pozicija() {
	}
    
	
    
}
