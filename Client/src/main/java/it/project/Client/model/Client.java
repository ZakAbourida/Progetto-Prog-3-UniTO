package it.project.Client.model;

import it.project.lib.RequestType;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private String email;
    private String address;
    private int port;

    public Client(String address, int port, String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("L'email non è valida.");
        }
        this.email = email;
        this.port = port;
        this.address = address;
        /*try {
            socket = new Socket(address, port);
            System.out.println("Connesso al server con l'email: " + email);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Invia l'email al server come primo messaggio
            try{
                RequestType req = new RequestType(email, 0);
                sendRequest(req);
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }

            close();
        } catch (IOException e) {
            System.out.println("Errore di connessione al server: " + e.getMessage());
            e.printStackTrace();
        }*/
    }

    /*public void sendMessage(String message) {
        output.println(message);
    }*/

    public Object sendRequest(Object request) throws IOException, ClassNotFoundException {
        try {
            // Invia la richiesta al server
            output.writeObject(request);
            output.flush();
            // Ricevi la risposta dal server
            Object response = input.readObject();
            System.out.println("CLIENT ==> Il server ha risposto: " + response);
            return response;
        } catch (SocketException | NullPointerException | EOFException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openConnection() throws IOException {
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            output.flush();
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