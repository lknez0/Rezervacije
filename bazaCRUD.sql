-- Select all data from each table
SELECT * FROM rezervacija;
SELECT * FROM radno_vrijeme;
SELECT * FROM stol;
SELECT * FROM termin;
SELECT * FROM usluzni_objekt;
SELECT * FROM pozicija;
SELECT * FROM vrsta_objekta;
SELECT * FROM gost;
SELECT * FROM vlasnik;
SELECT * FROM korisnik;


-- Update for all tables

ALTER TABLE rezervacija DISABLE TRIGGER ALL;
ALTER TABLE radno_vrijeme DISABLE TRIGGER ALL;
ALTER TABLE stol DISABLE TRIGGER ALL;
ALTER TABLE termin DISABLE TRIGGER ALL;
ALTER TABLE usluzni_objekt DISABLE TRIGGER ALL;
ALTER TABLE pozicija DISABLE TRIGGER ALL;
ALTER TABLE vrsta_objekta DISABLE TRIGGER ALL;
ALTER TABLE gost DISABLE TRIGGER ALL;
ALTER TABLE vlasnik DISABLE TRIGGER ALL;

UPDATE rezervacija
SET datum_rezervacije = '02.02.1978.'
WHERE id_objekta = 1;

UPDATE radno_vrijeme
SET id_objekta = 1
WHERE id_radnog_vremena = 1;

UPDATE stol
SET id_objekta = 2
WHERE id_stola = 1;

UPDATE termin
SET id_objekta = 2
WHERE id_termina = 1;

UPDATE usluzni_objekt
SET naziv_poduzeca = 'Jerin san'
WHERE id_objekta = 2;

UPDATE pozicija
SET naziv_pozicije = 'u podrumu'
WHERE id_pozicije = 2;

UPDATE vrsta_objekta
SET naziv_vrste_objekta = 'Brodica'
WHERE id_vrste_objekta = 1;

UPDATE gost
SET rod = 'M'
WHERE id_gosta = 1;

UPDATE vlasnik
SET godine_iskustva = 12
WHERE id_vlasnika = 1;

UPDATE korisnik
SET korisnicko_ime = 'anonimus'
WHERE id_korisnika = 1;

-- Delete for all tables

DELETE FROM rezervacija WHERE id_objekta = 1;
DELETE FROM radno_vrijeme WHERE id_objekta = 1;
DELETE FROM stol WHERE id_stola = 1;
DELETE FROM termin WHERE id_termina = 1;
DELETE FROM usluzni_objekt WHERE id_objekta = 2;
DELETE FROM pozicija WHERE id_pozicije = 2;
DELETE FROM vrsta_objekta WHERE id_vrste_objekta = 1;
DELETE FROM gost WHERE id_gosta = 1;
DELETE FROM vlasnik WHERE id_vlasnika = 1;
DELETE FROM korisnik WHERE id_korisnika = 1;

-- DROP all tables
DROP TABLE IF EXISTS rezervacija;
DROP TABLE IF EXISTS radno_vrijeme;
DROP TABLE IF EXISTS dan_u_tjednu;
DROP TABLE IF EXISTS stol;
DROP TABLE IF EXISTS termin;
DROP TABLE IF EXISTS usluzni_objekt;
DROP TABLE IF EXISTS pozicija;
DROP TABLE IF EXISTS vrsta_objekta;
DROP TABLE IF EXISTS gost;
DROP TABLE IF EXISTS vlasnik;
DROP TABLE IF EXISTS korisnik;


-- Update values



-- Create tables again
CREATE TABLE korisnik
(
  id_korisnika SERIAL NOT NULL,
  ime_korisnika VARCHAR(100) NULL,
  prezime_korisnika VARCHAR(100) NULL,
  korisnicko_ime VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(300) NOT NULL UNIQUE,
  lozinka VARCHAR(100) NOT NULL,
  tst TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_korisnika)
);

CREATE TABLE gost
(
  rod CHAR(1) NULL,
  datum_rodenja DATE NULL,
  broj_mobitela VARCHAR(100) NULL,
  id_gosta SERIAL NOT NULL,
  PRIMARY KEY (id_gosta),
  FOREIGN KEY (id_gosta) REFERENCES korisnik(id_korisnika)
);

CREATE TABLE vlasnik
(
  godine_iskustva INT NULL,
  broj_poslovnog_mobitela VARCHAR(100) NULL,
  id_vlasnika SERIAL NOT NULL,
  PRIMARY KEY (id_vlasnika),
  FOREIGN KEY (id_vlasnika) REFERENCES korisnik(id_korisnika)
);

CREATE TABLE vrsta_objekta
(
  id_vrste_objekta SERIAL NOT NULL,
  naziv_vrste_objekta VARCHAR(100) NULL,
  PRIMARY KEY (id_vrste_objekta)
);

CREATE TABLE pozicija
(
  id_pozicije SERIAL NOT NULL,
  naziv_pozicije VARCHAR(100) NULL,
  PRIMARY KEY (id_pozicije)
);

CREATE TABLE dan_u_tjednu
(
  id_dana SERIAL NOT NULL,
  naziv_dana VARCHAR(100) NULL,
  PRIMARY KEY (id_dana)
);

CREATE TABLE usluzni_objekt
(
  id_objekta SERIAL NOT NULL,
  naziv_objekta VARCHAR(100) NOT NULL,
  naziv_poduzeca VARCHAR(100) NULL,
  godina_otvaranja INT NULL,
  adresa_objekta VARCHAR(200) NULL,
  grad_objekta VARCHAR(100) NULL,
  velicina_objekta NUMERIC(5,2) NULL,
  web_stranica VARCHAR(200) NULL,
  broj_telefona VARCHAR(100) NULL,
  id_vlasnika SERIAL NOT NULL,
  id_vrste SERIAL NOT NULL,
  tst TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_objekta),
  FOREIGN KEY (id_vlasnika) REFERENCES vlasnik(id_vlasnika),
  FOREIGN KEY (id_vrste) REFERENCES vrsta_objekta(id_vrste_objekta)
);

CREATE TABLE stol
(
  id_stola SERIAL NOT NULL,
  broj_stolica INT NULL,
  id_objekta SERIAL NOT NULL,
  id_pozicije SERIAL NOT NULL,
  PRIMARY KEY (id_stola),
  FOREIGN KEY (id_objekta) REFERENCES usluzni_objekt(id_objekta),
  FOREIGN KEY (id_pozicije) REFERENCES pozicija(id_pozicije)
);

CREATE TABLE termin
(
  id_termina SERIAL NOT NULL,
  vrijeme_pocetka TIME NULL,
  vrijeme_zavrsetka TIME NULL,
  trajanje TIME NULL,
  id_objekta SERIAL NOT NULL,
  PRIMARY KEY (id_termina),
  FOREIGN KEY (id_objekta) REFERENCES usluzni_objekt(id_objekta)
);

CREATE TABLE rezervacija
(
  broj_osoba INT NULL,
  datum_rezervacije DATE NOT NULL,
  id_gosta SERIAL NOT NULL,
  id_termina SERIAL NOT NULL,
  id_stola SERIAL NOT NULL,
  id_objekta SERIAL NOT NULL,
  tst TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_gosta, id_termina, id_stola),
  FOREIGN KEY (id_gosta) REFERENCES gost(id_gosta),
  FOREIGN KEY (id_termina) REFERENCES termin(id_termina),
  FOREIGN KEY (id_stola) REFERENCES stol(id_stola),
  FOREIGN KEY (id_objekta) REFERENCES usluzni_objekt(id_objekta)
);

CREATE TABLE radno_vrijeme
(
  vrijeme_otvaranja TIME NULL,
  vrijeme_zatvaranja TIME NULL,
  id_radnog_vremena SERIAL NOT NULL,
  id_objekta SERIAL NOT NULL,
  id_dana SERIAL NOT NULL,
  PRIMARY KEY (id_radnog_vremena, id_objekta),
  FOREIGN KEY (id_objekta) REFERENCES usluzni_objekt(id_objekta),
  FOREIGN KEY (id_dana) REFERENCES dan_u_tjednu(id_dana)
);



INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('ponedjeljak');
INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('utorak');
INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('srijeda');
INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('četvrtak');
INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('petak');
INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('subota');
INSERT INTO dan_u_tjednu (naziv_dana) VALUES ('nedjelja');

INSERT INTO vrsta_objekta (naziv_vrste_objekta) VALUES ('cafe bar');
INSERT INTO vrsta_objekta (naziv_vrste_objekta) VALUES ('restoran');
INSERT INTO vrsta_objekta (naziv_vrste_objekta) VALUES ('noćni bar');
INSERT INTO vrsta_objekta (naziv_vrste_objekta) VALUES ('noćni klub');
INSERT INTO vrsta_objekta (naziv_vrste_objekta) VALUES ('bistro');
INSERT INTO vrsta_objekta (naziv_vrste_objekta) VALUES ('pivnica');

INSERT INTO pozicija (naziv_pozicije) VALUES ('na sredini');
INSERT INTO pozicija (naziv_pozicije) VALUES ('uz prozor');
INSERT INTO pozicija (naziv_pozicije) VALUES ('šank');
INSERT INTO pozicija (naziv_pozicije) VALUES ('separe');
INSERT INTO pozicija (naziv_pozicije) VALUES ('uz prolaz');
INSERT INTO pozicija (naziv_pozicije) VALUES ('terasa');

INSERT INTO korisnik (ime_korisnika, prezime_korisnika, korisnicko_ime, email, lozinka) 
VALUES ('Ivo','Ivić','iivic','ivic.ivo@fer.hr','ivo123');
INSERT INTO vlasnik (id_vlasnika, godine_iskustva, broj_poslovnog_mobitela) VALUES (1, 10, '0915882463');

INSERT INTO korisnik (ime_korisnika, prezime_korisnika, korisnicko_ime, email, lozinka) 
VALUES ('Ana','Anić','aanic','anic.ana@fer.hr','ana123');
INSERT INTO gost (id_gosta,rod, datum_rodenja, broj_mobitela) VALUES (2, 'Ž','02.02.1978.', '0924771414');

INSERT INTO korisnik (ime_korisnika, prezime_korisnika, korisnicko_ime, email, lozinka) 
VALUES ('Marko','Marić','mmaric','maric.marko@fer.hr','marko123');
INSERT INTO vlasnik (id_vlasnika, godine_iskustva, broj_poslovnog_mobitela) VALUES (3, 20, '0925653421');

INSERT INTO korisnik (ime_korisnika, prezime_korisnika, korisnicko_ime, email, lozinka) 
VALUES ('Pero','Perić','pperic','peric.pero@fer.hr','pero123');
INSERT INTO gost (id_gosta,rod, datum_rodenja, broj_mobitela) VALUES (4, 'M','11.08.1999.', '0998882425');

INSERT INTO usluzni_objekt (naziv_objekta, naziv_poduzeca, godina_otvaranja, adresa_objekta, grad_objekta, 
velicina_objekta, web_stranica, broj_telefona, id_vlasnika, id_vrste) 
VALUES ('Boogie Lab Zagreb','boogie.doo', 2023, 'Ulica kneza Borne 26','Zagreb', 50, 'www.boogielab.hr', '012855477', 1,5);
INSERT INTO usluzni_objekt (naziv_objekta, naziv_poduzeca, godina_otvaranja, adresa_objekta, grad_objekta, 
velicina_objekta, web_stranica, broj_telefona, id_vlasnika, id_vrste) 
VALUES ('The Beertija','beertija.doo', 2005, 'Ulica Pavla Hatza 16','Zagreb', 70.10, 'www.beertija.com', '01285844', 3,6);
INSERT INTO usluzni_objekt (naziv_objekta, naziv_poduzeca, godina_otvaranja, adresa_objekta, grad_objekta, 
velicina_objekta, web_stranica, broj_telefona, id_vlasnika, id_vrste) 
VALUES ('Leggiero','leggiero.doo', 2015, 'Maksimirsko naselje IV 25','Zagreb', 40.56, 'www.leggiero.hr', '012533426', 3,1);

INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'08:00','23:00',1);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'08:00','23:00',2);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'08:00','23:00',3);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'08:00','23:00',4);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'08:00','24:00',5);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'08:00','24:00',6);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (1,'10:00','18:00',7);

INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'09:00','23:00',1);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'09:00','23:00',2);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'09:00','23:00',3);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'09:00','23:00',4);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'10:00','02:00',5);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'10:00','02:00',6);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (2,'10:00','01:00',7);

INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',1);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',2);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',3);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',4);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',5);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',6);
INSERT INTO radno_vrijeme (id_objekta, vrijeme_otvaranja, vrijeme_zatvaranja, id_dana)
VALUES (3,'07:30','23:00',7);


INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,4,2);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,1,3);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,1,3);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,1,3);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,1,3);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,5,4);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (1,5,4);

INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,4,2);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,1,3);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,1,3);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,5,4);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,2,5);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,5,6);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,5,6);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,5,6);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (2,5,6);

INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,2,1);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,4,2);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,4,2);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,4,2);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,5,6);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,5,6);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,5,6);
INSERT INTO stol (id_objekta, broj_stolica, id_pozicije) 
VALUES (3,5,6);


INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (1,'10:00','11:30','01:30');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (1,'11:30','13:00','01:30');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (1,'13:00','14:00','01:00');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (1,'14:00','15:00','01:00');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (1,'15:00','16:30','01:30');


INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (2,'18:00','21:00','03:00');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (2,'19:00','22:00','03:00');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (2,'20:00','23:00','03:00');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (2,'21:00','24:00','03:00');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (2,'22:00','24:00','02:00');


INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (3,'15:00','15:30','01:30');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (3,'16:30','18:00','01:30');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (3,'18:00','19:30','01:30');
INSERT INTO termin (id_objekta, vrijeme_pocetka, vrijeme_zavrsetka, trajanje) 
VALUES (3,'19:30','21:00','01:30');

INSERT INTO rezervacija (id_gosta, id_objekta, id_termina, id_stola, broj_osoba, datum_rezervacije)
VALUES (2, 1, 2, 4, 5, '24.04.2023');
INSERT INTO rezervacija (id_gosta, id_objekta, id_termina, id_stola, broj_osoba, datum_rezervacije)
VALUES (4, 2, 8, 20, 4, '15.05.2023');


