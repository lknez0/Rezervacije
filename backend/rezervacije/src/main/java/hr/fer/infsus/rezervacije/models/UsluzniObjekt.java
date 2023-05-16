package hr.fer.infsus.rezervacije.models;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "usluzni_objekt")
public class UsluzniObjekt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objekta")
    private Long idObjekta;

    @Column(name = "naziv_objekta", nullable = false)
    private String nazivObjekta;

    @Column(name = "naziv_poduzeca")
    private String nazivPoduzeca;

    @Column(name = "godina_otvaranja")
    private Integer godinaOtvaranja;

    @Column(name = "adresa_objekta")
    private String adresaObjekta;

    @Column(name = "grad_objekta")
    private String gradObjekta;

    @Column(name = "velicina_objekta")
    private Long velicinaObjekta;

    @Column(name = "web_stranica")
    private String webStranica;

    @Column(name = "broj_telefona")
    private String brojTelefona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vlasnika")
    @JsonIgnore
    private Vlasnik vlasnik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vrste")
    @JsonIgnore
    private VrstaObjekta vrstaObjekta;

    @OneToMany(mappedBy = "usluzniObjekt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stol> stolovi;

    @OneToMany(mappedBy = "usluzniObjekt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Termin> termini;

    @OneToMany(mappedBy = "usluzniObjekt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RadnoVrijeme> radnaVremena;

	public Long getIdObjekta() {
		return idObjekta;
	}

	public void setIdObjekta(Long idObjekta) {
		this.idObjekta = idObjekta;
	}

	public String getNazivObjekta() {
		return nazivObjekta;
	}

	public void setNazivObjekta(String nazivObjekta) {
		this.nazivObjekta = nazivObjekta;
	}

	public String getNazivPoduzeca() {
		return nazivPoduzeca;
	}

	public void setNazivPoduzeca(String nazivPoduzeca) {
		this.nazivPoduzeca = nazivPoduzeca;
	}

	public Integer getGodinaOtvaranja() {
		return godinaOtvaranja;
	}

	public void setGodinaOtvaranja(Integer godinaOtvaranja) {
		this.godinaOtvaranja = godinaOtvaranja;
	}

	public String getAdresaObjekta() {
		return adresaObjekta;
	}

	public void setAdresaObjekta(String adresaObjekta) {
		this.adresaObjekta = adresaObjekta;
	}

	public String getGradObjekta() {
		return gradObjekta;
	}

	public void setGradObjekta(String gradObjekta) {
		this.gradObjekta = gradObjekta;
	}

	public Long getVelicinaObjekta() {
		return velicinaObjekta;
	}

	public void setVelicinaObjekta(Long velicinaObjekta) {
		this.velicinaObjekta = velicinaObjekta;
	}

	public String getWebStranica() {
		return webStranica;
	}

	public void setWebStranica(String webStranica) {
		this.webStranica = webStranica;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public Vlasnik getVlasnik() {
		return vlasnik;
	}

	public void setVlasnik(Vlasnik vlasnik) {
		this.vlasnik = vlasnik;
	}

	public VrstaObjekta getVrstaObjekta() {
		return vrstaObjekta;
	}

	public void setVrstaObjekta(VrstaObjekta vrstaObjekta) {
		this.vrstaObjekta = vrstaObjekta;
	}

	public List<Stol> getStolovi() {
		return stolovi;
	}

	public void setStolovi(List<Stol> stolovi) {
		this.stolovi = stolovi;
	}

	public List<Termin> getTermini() {
		return termini;
	}

	public void setTermini(List<Termin> termini) {
		this.termini = termini;
	}

	public List<RadnoVrijeme> getRadnaVremena() {
		return radnaVremena;
	}

	public void setRadnaVremena(List<RadnoVrijeme> radnaVremena) {
		this.radnaVremena = radnaVremena;
	}

    
}