Pokretanje:

Import projekta kao maven project u neki IDE.
Pokretanje RezervacijeApplication.java


Mapping:

GET:
/rezervacije/all
Dohvaća sve rezervacije

/rezervacije  
Dohvaća podatke za formu za stvaranje rezervacije

Dobiveni json:

{"gosti":[{"prezimeKorisnika":"Perić","idGosta":4,"imeKorisnika":"Pero"},{"prezimeKorisnika":"Anić","idGosta":2,"imeKorisnika":"Ana"}],
"pozicije":[{"idPozicije":1,"nazivPozicije":"na sredini"},...,{"idPozicije":6,"nazivPozicije":"terasa"}],
"termini":
	{"1":[{"vrijemeZavrsetka":"11:30:00","idTermina":1,"vrijemePocetka":"10:00:00","idObjekta":1},...],
	 "2":[{"vrijemeZavrsetka":"21:00:00","idTermina":6,"vrijemePocetka":"18:00:00","idObjekta":2},...]},
	...},,
"usluzniObjekti":[{"nazivObjekta":"Boogie Lab Zagreb","adresaObjekta":"Ulica kneza Borne 26","idObjekta":1,"gradObjekta":"Zagreb"}, ...]}




POST:
/rezervacije  
Stvara rezervaciju iz podataka poslanih u bodyju




PUT:
/rezervacije/id  

Izmjenjuje rezervaciju s id-om u pathu, mora imati vrijednosti u bodyju





DELETE:
/rezervacije/id  
Briše rezervaciju s id-om u pathu
