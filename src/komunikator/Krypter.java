package komunikator;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.*;
import javax.swing.JOptionPane;

import org.bouncycastle.jcajce.provider.digest.SHA3.*;

/**
 * Klasa zawierajace metody szyfrowania i deszyfrowania
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
public class Krypter {

    Cipher koder;
    Cipher dekoder;

    /**
     * Inicjalizacja Cipherow
     * @throws Exception
     */
    public Krypter() throws Exception {
            this.koder = Cipher.getInstance("RSA");
            this.dekoder = Cipher.getInstance("RSA");
    }
    
    /** 
    * Fukcja haszujaca tekst SHA-3 (256). W zaleznosci czy jest przesylana wiadomosc czy haslo do klucza AES to zwracany jest odpowiednio caly hash lub poł hashu w heksadecymalnie.
    * Rowniez korzystamy z tej funkjci przy sprawdzaniu hashu z certyfikatu. 
    * 
    * @param input string ktory bedzie hashowany
     * @param wiadomosc  parametr okreslajacy czy jest to wiadomosc (true) czy haslo do klucza AES (false)
     * @param cer ewentualnie przesylany certyfikat jesli jest to sprawdzenie poprawnosci certyfikatu
    * @return the hashed input string
    */
    public String sha3(String input, Boolean wiadomosc, Certyfikat cer){
        
        String hash="";
        
        try {
          DigestSHA3 md=new DigestSHA3(256);
          if(wiadomosc)
          {
              md.update(input.getBytes("UTF-8"));
              hash = bytesToHex(md.digest());
          }else if(cer!=null){
            byte[] kluczBajty = cer.zwrocKlucz().getEncoded();
            byte[] uzytkownikBajty = cer.zwrocUzytkownika().getBytes();
            byte[] destination = new byte[kluczBajty.length + uzytkownikBajty.length];
            System.arraycopy(kluczBajty, 0, destination, 0, kluczBajty.length);
            System.arraycopy(uzytkownikBajty, 0, destination, kluczBajty.length, uzytkownikBajty.length);
            md.update(destination);
            byte[] mdbytes = md.digest();
            hash = bytesToHex(mdbytes);
          }
          else
          {
            md.update(input.getBytes("UTF-8"));
            byte[] mdbytes = md.digest();
            byte[] key = new byte[mdbytes.length /2];

            for(int I = 0; I < key.length; I++){
              // using only 128 bits of the 256 generated
              key[I] = mdbytes[I];
            }
          
            hash = bytesToHex(key);
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        
        return hash;
    }
    
    /**
     * Zamiana bajtow na postac string heksadecymalny
     * @param bytes bajty do serializacji
     * @return string heksadecymalny
     */
    public String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
    
    /**
     * Szyfrowanie wiadomosci za pomoca klucza prywatnego.
     * @param key klucz prywatny
     * @param wiadomosc wiadomosc do zaszyfrowania (jej skrot sha3)
     * @return SealObject z zaszyfrowana wiadomoscia
     * @throws InvalidKeyException
     */
    public SealedObject encrypt(PrivateKey key, String wiadomosc) throws InvalidKeyException{
        // Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
        koder.init(Cipher.ENCRYPT_MODE, key);
        // Create a secret message
        String myMessage = sha3(wiadomosc, true, null);
        SealedObject myEncryptedMessage = null;
        try {
            // Encrypt that message using a new SealedObject and the Cipher we created before
            myEncryptedMessage= new SealedObject( myMessage, koder);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalBlockSizeException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        }
        
        return myEncryptedMessage;
    }
    
    /**
     * Odszyfrowywanie SealObjectu z zaszyfrowana wiadomoscia (jej skrotem sha3)
     * @param key klucz publiczny
     * @param ob zaszyfrowany obiekt
     * @return skrot wiadomosci
     * @throws InvalidKeyException
     */
    public String decrypt(PublicKey key, SealedObject ob) throws InvalidKeyException{
        // Initiate the Cipher, telling it that it is going to Decrypt, giving it the private key
        dekoder.init(Cipher.DECRYPT_MODE, key);
        
        // Tell the SealedObject we created before to decrypt the data and return it
        String message = "";
        try {
            message = (String) ob.getObject(dekoder);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        }
        
        return message;
    }
}
