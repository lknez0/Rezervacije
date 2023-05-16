package hr.fer.infsus.rezervacije.models;

public interface TerminProjection {
    Long getIdTermina();
    Long getIdObjekta();
    String getVrijemePocetka();
    String getVrijemeZavrsetka();
}
