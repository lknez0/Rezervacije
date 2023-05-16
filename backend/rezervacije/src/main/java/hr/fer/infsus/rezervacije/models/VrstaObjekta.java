package hr.fer.infsus.rezervacije.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vrsta_objekta")
public class VrstaObjekta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vrste_objekta")
    private Long idVrsteObjekta;
    
    @Column(name = "naziv_vrste_objekta")
    private String nazivVrsteObjekta;

	public Long getIdVrsteObjekta() {
		return idVrsteObjekta;
	}

	public void setIdVrsteObjekta(Long idVrsteObjekta) {
		this.idVrsteObjekta = idVrsteObjekta;
	}

	public String getNazivVrsteObjekta() {
		return nazivVrsteObjekta;
	}

	public void setNazivVrsteObjekta(String nazivVrsteObjekta) {
		this.nazivVrsteObjekta = nazivVrsteObjekta;
	}
    
    
}