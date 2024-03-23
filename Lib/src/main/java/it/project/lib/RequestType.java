package it.project.lib;

import java.io.Serializable;

/**
 * Questa classe rappresenta un tipo di richiesta inviata al server.
 * Contiene l'indirizzo email del client e il tipo di richiesta.
 * Il tipo di richiesta può essere:
 * -     1 -> richiesta di accesso
 * -     2 -> richiesta di invio email
 * -     3 -> richiesta di ricezione email
 * -     4 -> richiesta di eliminazione email
 * -     5 -> richiesta di chiusura connessione
 */
public class RequestType implements Serializable {

    private String email; // L'indirizzo email del client
    private int type;     // Il tipo di richiesta

    /**
     * Costruisce un oggetto di tipo RequestType con l'indirizzo email e il tipo specificati.
     *
     * @param email L'indirizzo email del client.
     * @param type  Il tipo di richiesta.
     */
    public RequestType(String email, int type) {
        this.email = email;
        this.type = type;
    }

    /**
     * Metodo getter per l'indirizzo email associato alla richiesta.
     *
     * @return L'indirizzo email associato alla richiesta.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce il tipo di richiesta.
     *
     * @return Il tipo di richiesta.
     */
    public int getType() {
        return type;
    }
}

