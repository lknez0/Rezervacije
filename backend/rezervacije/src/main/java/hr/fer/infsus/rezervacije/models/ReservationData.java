package hr.fer.infsus.rezervacije.models;

import java.time.LocalDate;

public class ReservationData {
    private Long idGosta;
    private Long idPozicije;
    private Long idTermina;
    private Long idObjekta;
    private LocalDate datumRezervacije;
    private int brojOsoba;
    private String brojMobitelaGosta;


    public ReservationData() {
    }

    public ReservationData(Long idGosta, Long idPozicije, Long idTermina, Long idObjekta, LocalDate datumRezervacije, int brojOsoba, String brojMobitelaGosta) {
        this.idGosta = idGosta;
        this.idPozicije = idPozicije;
        this.idTermina = idTermina;
        this.idObjekta = idObjekta;
        this.datumRezervacije = datumRezervacije;
        this.brojOsoba = brojOsoba;
        this.brojMobitelaGosta = brojMobitelaGosta;
    }

    public Long getIdGosta() {
        return idGosta;
    }

    public void setIdGosta(Long idGosta) {
        this.idGosta = idGosta;
    }

    public Long getIdPozicije() {
        return idPozicije;
    }

    public void setIdPozicije(Long idPozicije) {
        this.idPozicije = idPozicije;
    }

    public Long getIdTermina() {
        return idTermina;
    }

    public void setIdTermina(Long idTermina) {
        this.idTermina = idTermina;
    }

    public Long getIdObjekta() {
        return idObjekta;
    }

    public void setIdObjekta(Long idObjekta) {
        this.idObjekta = idObjekta;
    }

    public LocalDate getDatumRezervacije() {
        return datumRezervacije;
    }

    public void setDatumRezervacije(LocalDate datumRezervacije) {
        this.datumRezervacije = datumRezervacije;
    }

    public int getBrojOsoba() {
        return brojOsoba;
    }

    public void setBrojOsoba(int brojOsoba) {
        this.brojOsoba = brojOsoba;
    }

    public String getBrojMobitelaGosta() {
        return brojMobitelaGosta;
    }

    public void setBrojMobitelaGosta(String brojMobitelaGosta) {
        this.brojMobitelaGosta = brojMobitelaGosta;
    }
    
    
}
