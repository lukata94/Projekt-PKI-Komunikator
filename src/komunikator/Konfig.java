package komunikator;

import java.io.*;
import java.security.PublicKey;
import java.util.HashMap;
import javax.crypto.SealedObject;
import javax.swing.JOptionPane;

/**
 * Klasa zawierajaca metody konfiguracyjne aplikacji, czyli wczytywanie danych.
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
public class Konfig {
	
    /**
     * HashMap z portami dla kazdego uzytkownika z ktorym mozemy sie skomunikowac.
     */
    public HashMap<String, Integer> users;

    /**
     * HashMap z kluczami publicznymi dla kazdego uzytkownika z ktorym mozemy sie skomunikowac.
     */
    public HashMap<String, PublicKey> klucze;
	
    /**
     * Inicjalizacja
     */
    public Konfig(){
		users = new HashMap<String, Integer>();
                klucze = new HashMap<String, PublicKey>();
	}
	
    /**
     * Funkcja zwracajaca hashmap z portami i uzytkownikami wczytanymi z pliku konfiguracyjnego.
     * Pobiera rowniez dla tych uzytkownikow certyfikaty i sprawdza ich autentycznosc.
     * @param kluczCC klucz publiczny CC
     * @return hashmap z uzytkownikami i przyporzadkowanymi portami
     */
    public HashMap<String, Integer> wczytaj_konfiguracje(PublicKey kluczCC){
            try{
                FileInputStream fis = new FileInputStream("src/files/konfig.txt");
                BufferedInputStream bis = new BufferedInputStream(fis);
                InputStreamReader isr = new InputStreamReader(bis);
                LineNumberReader lnr = new LineNumberReader(isr);
                String line = null;
                while ((line = lnr.readLine()) != null) {
                        if (line.contains(":")){
                            String[] tab;
                            tab = line.split(":");
                            try{
                                Certyfikat c = OdbierzCertyfikat("src/files/" + tab[0] + ".cer", kluczCC);
                                klucze.put(tab[0], c.zwrocKlucz());
                                users.put(tab[0], Integer.parseInt(tab[1]));
                            }
                            catch (Exception ex){
                                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                }
                lnr.close();
            }
            catch (IOException ex){
                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
            }
            
            return users;
	}
        
    /**
     * Funkcja sprawdzajaca certyfikat uzytkownika.
     * @param certpath sciezka do certyfikatu
     * @param pk klucz publiczny
     * @return Certyfikat
     */
    public Certyfikat OdbierzCertyfikat(String certpath, PublicKey pk){
            Certyfikat CertUsera = null;
            try{
                //Odczytujemy plik z certyfikatem
		FileInputStream streamIn = new FileInputStream(certpath);
                HackedObjectInputStream objectinputstream = new HackedObjectInputStream(streamIn);
                SealedObject so = (SealedObject) objectinputstream.readObject();
                Certyfikat cf = (Certyfikat) objectinputstream.readObject();
		streamIn.close();
                
                //Sprawdzamy wiarygodnosc czyli dekodujemy
                Krypter kr = new Krypter();
                String odkodowanyHash = kr.decrypt(pk, so);
                String certyfikatHash = kr.sha3("", false, cf);
                
                if(odkodowanyHash.equals(certyfikatHash)){
                    CertUsera = cf;
                }

            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
            }
            
            return CertUsera;
        }
     
    /**
     * Klasa specjalna rozszerzajaca ObjectInputStream aby package sie zgadzal (w CC klasa Certyfikat jest w package zts a tutaj w komunikator)
     */
    class HackedObjectInputStream extends ObjectInputStream {

    public HackedObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

        if (resultClassDescriptor.getName().equals("zts.Certyfikat"))
            resultClassDescriptor = ObjectStreamClass.lookup(komunikator.Certyfikat.class);

        return resultClassDescriptor;
    }
}
}