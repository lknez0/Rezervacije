package hr.fer.infsus.rezervacije.models;

import jakarta.persistence.*;

@Entity
@Table(name = "dan_u_tjednu")
public class DanUTjednu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dana")
    private Long idDana;
    
    @Column(name = "naziv_dana")
    private String nazivDana;

	public Long getIdDana() {
		return idDana;
	}

	public void setIdDana(Long idDana) {
		this.idDana = idDana;
	}

	public String getNazivDana() {
		return nazivDana;
	}

	public void setNazivDana(String nazivDana) {
		this.nazivDana = nazivDana;
	}
	
	public DanUTjednu() {
	}

	public DanUTjednu(Long idDana, String nazivDana) {
		this.idDana = idDana;
		this.nazivDana = nazivDana;
	}
    
    
}