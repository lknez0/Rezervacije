package hr.fer.infsus.rezervacije.models;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@IdClass(RezervacijaId.class)
@Table(name = "rezervacija")
public class Rezervacija {
    @Column(name = "broj_osoba")
    private int brojOsoba;

    @Column(name = "datum_rezervacije")
    private LocalDate datumRezervacije;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_gosta")
    private Gost gost;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_termina")
    private Termin termin;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_stola")
    private Stol stol;

    @ManyToOne
    @JoinColumn(name = "id_objekta")
    private UsluzniObjekt usluzniObjekt;

    @Column(name = "tst", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp tst;

	public int getBrojOsoba() {
		return brojOsoba;
	}

	public void setBrojOsoba(int brojOsoba) {
		this.brojOsoba = brojOsoba;
	}

	public LocalDate getDatumRezervacije() {
		return datumRezervacije;
	}

	public void setDatumRezervacije(LocalDate datumRezervacije) {
		this.datumRezervacije = datumRezervacije;
	}

	public Gost getGost() {
		return gost;
	}

	public void setGost(Gost gost) {
		this.gost = gost;
	}

	public Termin getTermin() {
		return termin;
	}

	public void setTermin(Termin termin) {
		this.termin = termin;
	}

	public Stol getStol() {
		return stol;
	}

	public void setStol(Stol stol) {
		this.stol = stol;
	}

	public UsluzniObjekt getUsluzniObjekt() {
		return usluzniObjekt;
	}

	public void setUsluzniObjekt(UsluzniObjekt usluzniObjekt) {
		this.usluzniObjekt = usluzniObjekt;
	}

	public Timestamp getTst() {
		return tst;
	}

	public void setTst(Timestamp tst) {
		this.tst = tst;
	}

	public Rezervacija(int brojOsoba, LocalDate datumRezervacije, Gost gost, Termin termin, Stol stol,
			UsluzniObjekt usluzniObjekt, Timestamp tst) {
		this.brojOsoba = brojOsoba;
		this.datumRezervacije = datumRezervacije;
		this.gost = gost;
		this.termin = termin;
		this.stol = stol;
		this.usluzniObjekt = usluzniObjekt;
		this.tst = tst;
	}
	
	public Rezervacija() {
	}

    
}