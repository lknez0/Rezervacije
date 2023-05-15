package hr.fer.infsus.rezervacije.models;

import java.io.Serializable;
import java.util.Objects;

public class RezervacijaId implements Serializable {
	private static final long serialVersionUID = 9104363068840650682L;
	private Long gost;
    private Long termin;
    private Long stol;
	public Long getGost() {
		return gost;
	}
	public void setGost(Long gost) {
		this.gost = gost;
	}
	public Long getTermin() {
		return termin;
	}
	public void setTermin(Long termin) {
		this.termin = termin;
	}
	public Long getStol() {
		return stol;
	}
	public void setStol(Long stol) {
		this.stol = stol;
	}
	public RezervacijaId() {

	}
	public RezervacijaId(Long gost, Long termin, Long stol) {
		super();
		this.gost = gost;
		this.termin = termin;
		this.stol = stol;
	}
	@Override
	public int hashCode() {
		return Objects.hash(gost, stol, termin);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RezervacijaId other = (RezervacijaId) obj;
		return Objects.equals(gost, other.gost) && Objects.equals(stol, other.stol)
				&& Objects.equals(termin, other.termin);
	}
    
    

}