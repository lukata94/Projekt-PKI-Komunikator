package komunikator;

import java.io.Serializable;
import javax.crypto.SealedObject;

/**
 * Klasa obiektu z danymi przesyłanymi między komunikatorami.
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
public class Dane implements Serializable{
    private SealedObject object;
    private String username;
    private String wiadomosc;
    
    public Dane(String u){
        username = u;
    }
    
    
    public Dane(String u, String w, SealedObject o){
        username = u;
        object = o;
        wiadomosc = w;
    }
    
    public SealedObject zwrocObiekt() {
        return this.object;
    }
    
    public String zwrocUsera() {
        return this.username;
    }
    
    public String zwrocWiadomosc() {
        return this.wiadomosc;
    }
    
}
