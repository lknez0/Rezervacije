package hr.fer.infsus.rezervacije.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "stol")
public class Stol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stola")
    private Long idStola;

    @Column(name = "broj_stolica")
    private Integer brojStolica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_objekta")
    @JsonIgnore
    private UsluzniObjekt usluzniObjekt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pozicije")
    private Pozicija pozicija;

	public Long getIdStola() {
		return idStola;
	}

	public void setIdStola(Long idStola) {
		this.idStola = idStola;
	}

	public Integer getBrojStolica() {
		return brojStolica;
	}

	public void setBrojStolica(Integer brojStolica) {
		this.brojStolica = brojStolica;
	}

	public UsluzniObjekt getUsluzniObjekt() {
		return usluzniObjekt;
	}

	public void setUsluzniObjekt(UsluzniObjekt usluzniObjekt) {
		this.usluzniObjekt = usluzniObjekt;
	}

	public Pozicija getPozicija() {
		return pozicija;
	}

	public void setPozicija(Pozicija pozicija) {
		this.pozicija = pozicija;
	}

	public Stol(Long idStola, Integer brojStolica, UsluzniObjekt usluzniObjekt, Pozicija pozicija) {
		this.idStola = idStola;
		this.brojStolica = brojStolica;
		this.usluzniObjekt = usluzniObjekt;
		this.pozicija = pozicija;
	}

	public Stol() {
	}

    
}
