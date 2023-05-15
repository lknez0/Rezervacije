package hr.fer.infsus.rezervacije.models;

import java.sql.Time;

import jakarta.persistence.*;

@Entity
@Table(name = "radno_vrijeme")
public class RadnoVrijeme {
    @Column(name = "vrijeme_otvaranja")
    private Time  vrijemeOtvaranja;

    @Column(name = "vrijeme_zatvaranja")
    private Time  vrijemeZatvaranja;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_radnog_vremena")
    private int idRadnogVremena;

    @ManyToOne
    @JoinColumn(name = "id_objekta")
    private UsluzniObjekt usluzniObjekt;

    @ManyToOne
    @JoinColumn(name = "id_dana")
    private DanUTjednu danUTjednu;

	public Time  getVrijemeOtvaranja() {
		return vrijemeOtvaranja;
	}

	public void setVrijemeOtvaranja(Time  vrijemeOtvaranja) {
		this.vrijemeOtvaranja = vrijemeOtvaranja;
	}

	public Time  getVrijemeZatvaranja() {
		return vrijemeZatvaranja;
	}

	public void setVrijemeZatvaranja(Time  vrijemeZatvaranja) {
		this.vrijemeZatvaranja = vrijemeZatvaranja;
	}

	public int getIdRadnogVremena() {
		return idRadnogVremena;
	}

	public void setIdRadnogVremena(int idRadnogVremena) {
		this.idRadnogVremena = idRadnogVremena;
	}

	public UsluzniObjekt getUsluzniObjekt() {
		return usluzniObjekt;
	}

	public void setUsluzniObjekt(UsluzniObjekt usluzniObjekt) {
		this.usluzniObjekt = usluzniObjekt;
	}

	public DanUTjednu getDanUTjednu() {
		return danUTjednu;
	}

	public void setDanUTjednu(DanUTjednu danUTjednu) {
		this.danUTjednu = danUTjednu;
	}

    
}