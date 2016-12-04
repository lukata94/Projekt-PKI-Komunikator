package komunikator;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Klasa przedstawiajaca certyfikat i posiadajaca metody dostepu do danych certyfikatu.
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
public class Certyfikat implements Serializable{
	private PublicKey klucz;
	private String uzytkownik;
	
	public Certyfikat(PublicKey pk, String user){
		klucz = pk;
		uzytkownik = user;
	}
	
	public String zwrocUzytkownika(){
		return this.uzytkownik;
	}
	
	public PublicKey zwrocKlucz(){
		return this.klucz;
	}
}