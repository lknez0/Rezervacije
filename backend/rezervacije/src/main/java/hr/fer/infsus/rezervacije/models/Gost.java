package hr.fer.infsus.rezervacije.models;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "gost")
public class Gost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gosta")
    private Long idGosta;
    
    @Column(name = "rod")
    private Character rod;
    
    @Column(name = "datum_rodenja")
    private Date  datumRodenja;
    
    @Column(name = "broj_mobitela")
    private String brojMobitela;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gosta")
    private Korisnik korisnik;

	public Long getIdGosta() {
		return idGosta;
	}

	public void setIdGosta(Long idGosta) {
		this.idGosta = idGosta;
	}

	public Character getRod() {
		return rod;
	}

	public void setRod(Character rod) {
		this.rod = rod;
	}

	public Date  getDatumRodenja() {
		return datumRodenja;
	}

	public void setDatumRodenja(Date  datumRodenja) {
		this.datumRodenja = datumRodenja;
	}

	public String getBrojMobitela() {
		return brojMobitela;
	}

	public void setBrojMobitela(String brojMobitela) {
		this.brojMobitela = brojMobitela;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	
	public Gost() {
	}
    

	public Gost(Long idGosta, Character rod, Date datumRodenja, String brojMobitela, Korisnik korisnik) {
		this.idGosta = idGosta;
		this.rod = rod;
		this.datumRodenja = datumRodenja;
		this.brojMobitela = brojMobitela;
		this.korisnik = korisnik;
	}
    
    
}