package hr.fer.infsus.rezervacije.models;
public class ReservationRequest {
    private Long idGosta;
    private String brojMobitelaGosta;
    private Long idObjekta;
    private String datumRezervacije;
    private int brojOsoba;
    private Long vrstaStola;
    private Long terminRezervacija;

    public ReservationRequest() {
    }
    
    

    public ReservationRequest(Long idGosta, String brojMobitelaGosta, Long idObjekta, String datumRezervacije,
			int brojOsoba, Long vrstaStola, Long terminRezervacija) {
		this.idGosta = idGosta;
		this.brojMobitelaGosta = brojMobitelaGosta;
		this.idObjekta = idObjekta;
		this.datumRezervacije = datumRezervacije;
		this.brojOsoba = brojOsoba;
		this.vrstaStola = vrstaStola;
		this.terminRezervacija = terminRezervacija;
	}



	public Long getIdGosta() {
        return idGosta;
    }

    public void setIdGosta(Long idGosta) {
        this.idGosta = idGosta;
    }

    public String getBrojMobitelaGosta() {
        return brojMobitelaGosta;
    }

    public void setBrojMobitelaGosta(String brojMobitelaGosta) {
        this.brojMobitelaGosta = brojMobitelaGosta;
    }

    public Long getIdObjekta() {
        return idObjekta;
    }

    public void setIdObjekta(Long idObjekta) {
        this.idObjekta = idObjekta;
    }

    public String getDatumRezervacije() {
        return datumRezervacije;
    }

    public void setDatumRezervacije(String datumRezervacije) {
        this.datumRezervacije = datumRezervacije;
    }

    public int getBrojOsoba() {
        return brojOsoba;
    }

    public void setBrojOsoba(int brojOsoba) {
        this.brojOsoba = brojOsoba;
    }

    public Long getVrstaStola() {
        return vrstaStola;
    }

    public void setVrstaStola(Long vrstaStola) {
        this.vrstaStola = vrstaStola;
    }

    public Long getTerminRezervacija() {
        return terminRezervacija;
    }

    public void setTerminRezervacija(Long terminRezervacija) {
        this.terminRezervacija = terminRezervacija;
    }
}