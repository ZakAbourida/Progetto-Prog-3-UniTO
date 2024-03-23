package it.project.lib;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Email implements Serializable {
    private String sender;
    private List<String> recipients;
    private String subject;
    private String text;
    private String date;
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m:s");

    /**
     * Costruttore della classe Email.
     *
     * @param sender     l'indirizzo email del mittente
     * @param recipients la lista degli indirizzi email dei destinatari
     * @param subject    l'oggetto dell'email
     * @param text       il corpo del messaggio
     */
    public Email(String sender, List<String> recipients, String subject, String text) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.text = text;
    }

    /**
     * Costruttore della classe Email che prende una riga di testo formattata.
     *
     * @param line una riga di testo formattata che rappresenta un'email
     */
    public Email(String line) {
        String[] parts = line.split("_", 5);
        System.out.println("-->"+parts.length);
        String[] recipients = parts[4].split("_");
        this.recipients = Arrays.stream(recipients).toList();
        this.sender = parts[0];
        this.subject = parts[1];
        this.text = parts[2];
        this.date = parts[3];
    }

    /**
     * Costruttore vuoto temporaneo.
     */
    public Email() {
    }

    /**
     * @return una stringa contenente i dettagli dell'email
     * @Override del metodo toString per ottenere una rappresentazione testuale dell'email.
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(sender + '_' + subject + '_' + text + '_' + date + '_');
        for (int i = 0; i < this.recipients.size(); i++) {
            res.append(recipients.get(i)).append('_');
        }
        return res + "\n";
    }

    /**
     * Metodi getter/setter per recuperare/impostare i valori delle variabili private
     */

    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate() {
        this.date = this.format.format(new Date());
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    /**
     * @param obj l'oggetto da confrontare con l'istanza corrente
     * @return true se i due oggetti sono uguali, altrimenti false
     * @Override del metodo equals per confrontare due oggetti Email.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Email) {
            return Objects.equals(obj.toString(), this.toString());
        }
        return false;
    }

}