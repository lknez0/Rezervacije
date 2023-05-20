package hr.fer.infsus.rezervacije.models;

import java.time.LocalDate;

public class ReservationData {
    private Long idGosta;
    private Long pozicija;
    private Long terminRezervacije;
    private Long idObjekta;
    private LocalDate datumRezervacije;
    private int brojOsoba;
    private String brojMobitelaGosta;


    public ReservationData() {
    }

    public ReservationData(Long idGosta, Long pozicija, Long terminRezervacije, Long idObjekta, LocalDate datumRezervacije, int brojOsoba, String brojMobitelaGosta) {
        this.idGosta = idGosta;
        this.pozicija = pozicija;
        this.terminRezervacije = terminRezervacije;
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

    public Long getPozicija() {
        return pozicija;
    }

    public void setPozicija(Long pozicija) {
        this.pozicija = pozicija;
    }

    public Long getTerminRezervacije() {
        return terminRezervacije;
    }

    public void setTerminRezervacije(Long terminRezervacije) {
        this.terminRezervacije = terminRezervacije;
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
