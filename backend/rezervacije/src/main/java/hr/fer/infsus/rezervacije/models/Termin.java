package hr.fer.infsus.rezervacije.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "termin")
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_termina")
    private Long idTermina;

    @Column(name = "vrijeme_pocetka")
    private String vrijemePocetka;

    @Column(name = "vrijeme_zavrsetka")
    private String vrijemeZavrsetka;

    @Column(name = "trajanje")
    private String trajanje;

    @ManyToOne
    @JoinColumn(name = "id_objekta")
    @JsonIgnore
    private UsluzniObjekt usluzniObjekt;

	public Long getIdTermina() {
		return idTermina;
	}

	public void setIdTermina(Long idTermina) {
		this.idTermina = idTermina;
	}

	public String getVrijemePocetka() {
		return vrijemePocetka;
	}

	public void setVrijemePocetka(String vrijemePocetka) {
		this.vrijemePocetka = vrijemePocetka;
	}

	public String getVrijemeZavrsetka() {
		return vrijemeZavrsetka;
	}

	public void setVrijemeZavrsetka(String vrijemeZavrsetka) {
		this.vrijemeZavrsetka = vrijemeZavrsetka;
	}

	public String getTrajanje() {
		return trajanje;
	}

	public void setTrajanje(String trajanje) {
		this.trajanje = trajanje;
	}

	public UsluzniObjekt getUsluzniObjekt() {
		return usluzniObjekt;
	}

	public void setUsluzniObjekt(UsluzniObjekt usluzniObjekt) {
		this.usluzniObjekt = usluzniObjekt;
	}

	public Termin() {
		// TODO Auto-generated constructor stub
	}

	public Termin(Long idTermina, String vrijemePocetka, String vrijemeZavrsetka, String trajanje,
			UsluzniObjekt usluzniObjekt) {
		super();
		this.idTermina = idTermina;
		this.vrijemePocetka = vrijemePocetka;
		this.vrijemeZavrsetka = vrijemeZavrsetka;
		this.trajanje = trajanje;
		this.usluzniObjekt = usluzniObjekt;
	}
	
	
    
}