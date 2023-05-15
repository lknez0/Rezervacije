package hr.fer.infsus.rezervacije.models;

import java.util.List;

public class FormDataRezervacije {
	
	private List<GostProjection> gosti; 
	private List<Pozicija> pozicije;
	private List<TerminProjection> termini;
	private List<UsluzniObjektProjection> usluzniObjekti;
	public List<GostProjection> getGosti() {
		return gosti;
	}
	public void setGosti(List<GostProjection> gosti) {
		this.gosti = gosti;
	}
	public List<Pozicija> getPozicije() {
		return pozicije;
	}
	public void setPozicije(List<Pozicija> pozicije) {
		this.pozicije = pozicije;
	}
	public List<TerminProjection> getTermini() {
		return termini;
	}
	public void setTermini(List<TerminProjection> termini) {
		this.termini = termini;
	}
	public List<UsluzniObjektProjection> getUsluzniObjekti() {
		return usluzniObjekti;
	}
	public void setUsluzniObjekti(List<UsluzniObjektProjection> usluzniObjekti) {
		this.usluzniObjekti = usluzniObjekti;
	}
	public FormDataRezervacije(List<GostProjection> gosti, List<Pozicija> pozicije,
			List<TerminProjection> termini, List<UsluzniObjektProjection> usluzniObjekti) {
		this.gosti = gosti;
		this.pozicije = pozicije;
		this.termini = termini;
		this.usluzniObjekti = usluzniObjekti;
	}
	
	
	
	
	

}
