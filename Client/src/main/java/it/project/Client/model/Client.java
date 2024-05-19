package it.project.Client.model;

import it.project.lib.Email;
import it.project.lib.RequestType;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Questa classe rappresenta il modello del client che gestisce la comunicazione con il server.
 * Fornisce metodi per inviare email, effettuare il login, ricevere email, chiudere la connessione...
 */
public class Client {
    private Socket socket;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private String email;

    /**
     * Costruttore della classe Client che inizializza la connessione con il server.
     *
     * @throws IOException se si verifica un errore durante l'apertura della connessione
     */
    public Client() throws IOException {
        openConnection("127.0.0.1", 4040);
    }

    /**
     * Restituisce il socket utilizzato per la connessione con il server.
     *
     * @return il socket utilizzato per la connessione
     */
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Invia una richiesta al server.
     *
     * @param request la richiesta da inviare
     */
    private void sendRequest(Object request) {
        try {
            // Invia la richiesta al server
            output.writeObject(request);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invia un'email al server.
     *
     * @param email l'email da inviare
     * @return true se l'email è stata inviata con successo, altrimenti false
     */
    public boolean sendEmail(Email email) {
        // Filtra i destinatari validi prima di inviare l'email
        email.setRecipients(email.getRecipients().stream().filter(Client::isValidEmail).toList());
        email.setSender(this.email); // Imposta il mittente dell'email
        email.setDate();

        try {
            sendRequest(new RequestType(this.email, 2)); // Invia la richiesta al server
            output.writeObject(email); // Invia l'oggetto Email
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Invia una richiesta di login al server.
     *
     * @param address l'indirizzo email del cliente
     * @return la lista delle email ricevute in risposta alla richiesta di login
     */
    public List<Email> sendLogin(String address) {
        this.email = address;
        sendRequest(new RequestType(address, 1));
        try {
            Object response = input.readObject();
            return (List<Email>) response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invia una richiesta per cancellare un'email dalla Mailbox
     *
     * @param email l'email da cancellare
     */
    public void cancelEmail(Email email) {
        try {
            sendRequest(new RequestType(this.email, 4));
            output.writeObject(email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invia richiesta per riceve/aggiornare le email dal server.
     *
     * @param address l'indirizzo email del cliente
     * @return la lista delle email ricevute dal server
     */
    public List<Email> receivedEmail(String address) {
        sendRequest(new RequestType(address, 3)); // Invio la richiesta al server
        try {
            Object response = input.readObject(); // Leggo la risposta

            // Verifico che la risposta sia una List di Email
            if (response instanceof List<?>) {
                // Effettuo un ulteriore controllo per confermare che gli elementi siano Email
                List<Email> emails = (List<Email>) response;
                for (Object item : emails) {
                    if (!(item instanceof Email)) {
                        throw new RuntimeException("The list contains un object that is not an Email");
                    }
                }
                return emails;
            } else {
                throw new RuntimeException("Expected response was a List<Email> but a different type was received.");
            }
        } catch (IOException | ClassNotFoundException e) {
            // Combina la gestione di IOException e ClassNotFoundException
            throw new RuntimeException("Error occurred while receiving emails.", e);
        }
    }

    /**
     * Apre una connessione con il server utilizzando l'indirizzo IP e la porta specificati.
     * Se la connessione non può essere stabilita, viene gestita un'eccezione di tipo IOException.
     *
     * @param address l'indirizzo IP del server a cui connettersi
     * @param port    la porta del server a cui connettersi
     * @throws IOException se si verifica un errore durante l'apertura della connessione
     */
    public void openConnection(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    /**
     * Chiude la connessione con il server.
     * Prima di chiudere la connessione, invia una richiesta di chiusura al server.
     *
     * @param email l'indirizzo email del cliente
     */
    public void close(String email) {
        try {
            sendRequest(new RequestType(email, 5));
            if (socket != null) {
                socket.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }

    /**
     * Verifica se un indirizzo email è valido.
     * Un indirizzo email è considerato valido se ha una lunghezza massima di 25 caratteri
     *  e corrisponde al formato standard: username@dominio.com o username@dominio.it.
     *
     * @param email l'indirizzo email da verificare
     * @return true se l'indirizzo email è valido, altrimenti false
     */
    public static boolean isValidEmail(String email) {

        if (email.length() > 25) {
            return false;
        }

        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.(com|it)$";

        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }
}