package hr.fer.infsus.rezervacije.models;

public class TerminProjection {
    private Long idTermina;
    private String vrijemePocetka;
    private String vrijemeZavrsetka;

    public TerminProjection(Long idTermina, String vrijemePocetka, String vrijemeZavrsetka) {
        this.idTermina = idTermina;
        this.vrijemePocetka = vrijemePocetka;
        this.vrijemeZavrsetka = vrijemeZavrsetka;
    }

    public Long getIdTermina() {
        return idTermina;
    }

    public String getVrijemePocetka() {
        return vrijemePocetka;
    }

    public String getVrijemeZavrsetka() {
        return vrijemeZavrsetka;
    }
}
