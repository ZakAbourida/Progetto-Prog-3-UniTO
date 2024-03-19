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
        openConnection("127.0.0.1",4040);
    }
    public Socket getSocket(){return socket;}

    private void sendRequest(Object request){
        try {
            // Invia la richiesta al server
            output.writeObject(request);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(Email email) {
        email.setRecipients(email.getRecipients().stream().filter(Client::isValidEmail).toList());
        email.setSender(this.email);
        email.setDate();
        try {
            sendRequest(new RequestType(this.email, 2));
            output.writeObject(email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Email> sendLogin(String address){
        this.email  = address;
        sendRequest(new RequestType(address,1));
        try {
            Object response = input.readObject();
            return (List<Email>) response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void openConnection(String address,int port) throws IOException {
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException e) {
            System.err.println("Connection error: " + e.getMessage());
        }}

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            System.out.println("Connessione chiusa.");
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