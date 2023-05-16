package hr.fer.infsus.rezervacije.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vlasnik")
public class Vlasnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vlasnika")
    private Long idVlasnika;
    
    @Column(name = "godine_iskustva")
    private Integer godineIskustva;
    
    @Column(name = "broj_poslovnog_mobitela")
    private String brojPoslovnogMobitela;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vlasnika")
    private Korisnik korisnik;

	public Long getIdVlasnika() {
		return idVlasnika;
	}

	public void setIdVlasnika(Long idVlasnika) {
		this.idVlasnika = idVlasnika;
	}

	public Integer getGodineIskustva() {
		return godineIskustva;
	}

	public void setGodineIskustva(Integer godineIskustva) {
		this.godineIskustva = godineIskustva;
	}

	public String getBrojPoslovnogMobitela() {
		return brojPoslovnogMobitela;
	}

	public void setBrojPoslovnogMobitela(String brojPoslovnogMobitela) {
		this.brojPoslovnogMobitela = brojPoslovnogMobitela;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
    
    
}