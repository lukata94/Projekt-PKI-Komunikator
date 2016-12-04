package komunikator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Klasa serwera ktory nasluchuje i odbiera wiadomosci.
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
public class Serwer extends Thread{
    private ServerSocket server;
    private okno omain;

    /**
     * Inicjacja serwera
     * @param p port na ktorym bedzie serwer nasluchiwal
     * @param o okno glowne przekazane zeby mozna bylo z jego metod korzystac
     */
    public Serwer(int p, okno o) {
        try {
            server = new ServerSocket(p);
            omain = o;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void run(){
        handleConnection();
    }
    
    /**
     * Metoda uruchamiana po inicjalizacji serwera. Zajmuje sie przejmownaiem polaczen i przetwarzaniu ich w nowych watkach.
     */
    public void handleConnection() {
        while (true) {
            try {
                Socket socket = server.accept();
                new ConnectionHandler(socket, omain);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * Klasa implementujaca runnable zajmujaca sie przetwarzaniem polaczen.
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
class ConnectionHandler implements Runnable {
    private Socket socket;
    private okno okmain;

    /**
     * Inicjalizacja nowego przejmowacza polaczen w nowym watku.
     * @param socket socket uzytkownika od ktorego otrzymujemy polaczenie
     * @param o okno glowne
     */
    public ConnectionHandler(Socket socket, okno o) {
        this.socket = socket;
        this.okmain = o;
        
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            Dane d = (Dane) ois.readObject(); //czytamy obiekt Dane
            okmain.sprawdzDane(d); //uruchamiamy w oknie metode do sprawdzania danych
            
            ois.close();
            socket.close();
           
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
