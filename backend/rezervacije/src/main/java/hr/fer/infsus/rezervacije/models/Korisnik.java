package hr.fer.infsus.rezervacije.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "korisnik")
public class Korisnik {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_korisnika")
	private Long idKorisnika;

	@Column(name = "ime_korisnika")
	private String imeKorisnika;

	@Column(name = "prezime_korisnika")
	private String prezimeKorisnika;

	@Column(name = "korisnicko_ime", unique = true, nullable = false)
	private String korisnickoIme;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "lozinka", nullable = false)
	private String lozinka;

	@Column(name = "tst", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp tst;

	public Long getIdKorisnika() {
		return idKorisnika;
	}

	public void setIdKorisnika(Long idKorisnika) {
		this.idKorisnika = idKorisnika;
	}

	public String getImeKorisnika() {
		return imeKorisnika;
	}

	public void setImeKorisnika(String imeKorisnika) {
		this.imeKorisnika = imeKorisnika;
	}

	public String getPrezimeKorisnika() {
		return prezimeKorisnika;
	}

	public void setPrezimeKorisnika(String prezimeKorisnika) {
		this.prezimeKorisnika = prezimeKorisnika;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public Timestamp getTst() {
		return tst;
	}

	public void setTst(Timestamp tst) {
		this.tst = tst;
	}

}