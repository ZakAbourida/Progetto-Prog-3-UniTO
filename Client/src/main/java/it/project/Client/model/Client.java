package it.project.Client.model;

import it.project.lib.Email;
import it.project.lib.RequestType;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private String email;

    public Client() throws IOException {
        openConnection("127.0.0.1", 4040);
    }

    public Socket getSocket() {
        return socket;
    }

    private void sendRequest(Object request) {
        try {
            // Invia la richiesta al server
            output.writeObject(request);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmail(Email email) {
        // Filtra i destinatari validi prima di inviare l'email
        email.setRecipients(email.getRecipients().stream().filter(Client::isValidEmail).toList());
        email.setSender(this.email); // Imposta il mittente dell'email
        email.setDate(); // Imposta la data corrente come data di invio

        try {
            sendRequest(new RequestType(this.email, 2)); // Invia la richiesta al server
            output.writeObject(email); // Invia l'oggetto Email
            return true; // L'invio è riuscito
        } catch (IOException e) {
            // Log dell'errore per scopi di debug o di tracciamento
            e.printStackTrace();
            // Restituisce false poiché l'invio non è riuscito a causa di un'eccezione
            return false;
        }
    }

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

    public void cancelEmail(Email email) {
        try {
            sendRequest(new RequestType(this.email, 4));
            output.writeObject(email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Attendo la risposta e la gestisco
        try {
            Object response = input.readObject();
            System.out.println("RISPOSTA DAL SERVER: "+response);
            if (response instanceof String) {
                String message = (String) response;
                System.out.println(message);
            } else {
                throw new IllegalArgumentException("Response type not recognized.");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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
                        throw new RuntimeException("La lista contiene un oggetto che non è un'Email");
                    }
                }
                return emails;
            } else {
                throw new RuntimeException("La risposta attesa era una List<Email> ma è stato ricevuto un tipo diverso");
            }
        } catch (IOException | ClassNotFoundException e) {
            // Combina la gestione di IOException e ClassNotFoundException
            throw new RuntimeException("Errore durante la ricezione delle email", e);
        }
    }

    public void openConnection(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public void close(String email) {
        try {
            sendRequest(new RequestType(email,5));
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

    // Metodo per verificare la validità dell'email
    public static boolean isValidEmail(String email) {
        // Verifica la lunghezza dell'email
        if (email.length() > 25) {
            return false;
        }

        // Espressione regolare per verificare la forma dell'email
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.(com|it)$";

        // Applica la regex all'email
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

}