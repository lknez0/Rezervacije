package hr.fer.infsus.rezervacije.models;

import java.util.List;

public class UsluzniObjektProjection {
    private Long idObjekta;
    private String nazivObjekta;
    private String adresaObjekta;
    private String gradObjekta;
    private List<TerminProjection> termini;

    public UsluzniObjektProjection(Long idObjekta, String nazivObjekta, String adresaObjekta, String gradObjekta, List<TerminProjection> termini) {
        this.idObjekta = idObjekta;
        this.nazivObjekta = nazivObjekta;
        this.adresaObjekta = adresaObjekta;
        this.gradObjekta = gradObjekta;
        this.termini = termini;
    }

    public Long getIdObjekta() {
        return idObjekta;
    }

    public String getNazivObjekta() {
        return nazivObjekta;
    }

    public String getAdresaObjekta() {
        return adresaObjekta;
    }

    public String getGradObjekta() {
        return gradObjekta;
    }

    public List<TerminProjection> getTermini() {
        return termini;
    }

    public void setTermini(List<TerminProjection> termini) {
        this.termini = termini;
    }
}