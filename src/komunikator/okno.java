package komunikator;

import java.awt.Color;
import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Klasa okna komunikatora na ktorym uzytkownik bedzie operowal
 * @author Łukasz Dźwigulski Rafał Sosnowski
 */
public class okno extends javax.swing.JFrame {

    private Konfig konfiguracja = new Konfig();
    private Serwer serwer;
    private String username;
    private int portSerwa;
    private PrivateKey kluczPrywatny;
    public Krypter krypter;
    private PublicKey kluczCC;
    private HashMap<String, Integer> uzytkownicy;
    private HashMap<String, PublicKey> certyfikaty;
    private DefaultListModel listModel = new DefaultListModel();
    
    /**
     * Okno komunikatora
     * @param u Nazwa uzytkownika
     * @param k Klucz prywatny uzytkownika
     * @param p Port na ktorym bedzie serwer nasluchiwal
     * @param pk Klucz publiczny CC
     */
    public okno(String u, PrivateKey k, int p, PublicKey pk) {
        
        kluczPrywatny = k;
        portSerwa = p;
        username = u;
        kluczCC = pk;
        
        try{        
            this.krypter = new Krypter();
            
            uzytkownicy = konfiguracja.wczytaj_konfiguracje(kluczCC);
            uzytkownicy.entrySet().forEach((entry) -> {
                listModel.addElement(entry.getKey());
            });
            
            certyfikaty = konfiguracja.klucze;
            
            serwer = new Serwer(portSerwa, this);
            serwer.start();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
        
        
        initComponents();
        jLabel2.setText(u + " / " + Integer.toString(p));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cyfromunikator 1.0");
        setResizable(false);

        jLabel1.setText("Połączony jako:");

        jLabel2.setToolTipText("");

        jTextField1.setToolTipText("");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jList1.setModel(listModel);
        jScrollPane2.setViewportView(jList1);

        jButton2.setText("Wyślij");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("Zabezpiecz");
        jRadioButton1.setToolTipText("");

        jTextPane1.setEditable(false);
        jScrollPane3.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     *Przesyłamy socketem dane do uzytkownika zaznaczonego w select boxie
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String tekst = jTextField1.getText();
        
        String odbiorca = jList1.getSelectedValue();
        int portUsera = uzytkownicy.get(odbiorca);
        Dane dout = null;
        
        try {      
            //przeklamane dane
            if(!jRadioButton1.isSelected()){
                SealedObject so = krypter.encrypt(kluczPrywatny, "bla bla bla");
                dout = new Dane(username, tekst, so);
            }
            //nieprzekłamane dane
            else{
                SealedObject so = krypter.encrypt(kluczPrywatny, tekst);
                dout = new Dane(username, tekst, so);
            }
            
            Socket socket = new Socket("localhost", portUsera);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(dout);

            oos.close();
            socket.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
        }
        
        Wiadomosci("ZWERYFIKOWANY", username, tekst);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * Sprawdzamy poprawnosc danych, czy nie zostaly przeklamane i czy sa wyslane od dobrego uzytkownika.
     * W tym celu porownujemy hash odkodowany kluczem publicznym B z jego certyfikatu z hashem wygenerowanym na podstawie tekstu jawnego przekazanego w obiekcie Dane.
     * @param dane Dane do sprawdzenia
     */
    public void sprawdzDane(Dane dane){
        SealedObject s = dane.zwrocObiekt();

        PublicKey pk;
        String wiadomosc = "";
        try {
            pk = certyfikaty.get(dane.zwrocUsera());
            wiadomosc = krypter.decrypt(pk, s);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(okno.class.getName()).log(Level.SEVERE, null, ex);
        }
                                    
        //sprawdzamy hash
        String SHAcheck = krypter.sha3(dane.zwrocWiadomosc(), true, null);
        if(SHAcheck.equals(wiadomosc)){
            Wiadomosci("ZWERYFIKOWANY", dane.zwrocUsera(), dane.zwrocWiadomosc());
        }
        else if(wiadomosc.equals("")){
            Wiadomosci("NIEZNANY", dane.zwrocUsera(), dane.zwrocWiadomosc());
        }
        else{
            Wiadomosci("NIEZWERYFIKOWANY", dane.zwrocUsera(), dane.zwrocWiadomosc());
        }
    }
    
    /**
     * Ukazywanie wiadomosci w okienku, w zaleznosci czy przeklamane czy nie to rozne czcionki
     * @param warunek okresla czy wiadomosc jest orzeklamana czy nie
     * @param uzytk uzytkownik od ktorego dostalismy wiadomosc
     * @param wiadomosc tresc wiadomosci
     */
    public void Wiadomosci(String warunek, String uzytk, String wiadomosc){
        StyledDocument doc = jTextPane1.getStyledDocument();
        SimpleAttributeSet aSet = new SimpleAttributeSet(); 
        
        if(warunek.equals("ZWERYFIKOWANY")){         
            try {
                StyleConstants.setBold(aSet, true);
                
                doc.insertString(doc.getLength(), uzytk + ": ", aSet);
                
                StyleConstants.setBold(aSet, false);
                doc.insertString(doc.getLength(), wiadomosc + "\n", aSet);
            } catch (BadLocationException ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
            }
        }else if(warunek.equals("NIEZWERYFIKOWANY")){
            try {
                StyleConstants.setForeground(aSet, Color.RED);
                StyleConstants.setBold(aSet, true);
                
                doc.insertString(doc.getLength(), uzytk + ": ", aSet);
                
                StyleConstants.setBold(aSet, false);
                doc.insertString(doc.getLength(), wiadomosc + "\n", aSet);
            } catch (BadLocationException ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
