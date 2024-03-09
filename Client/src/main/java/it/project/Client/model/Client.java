package it.project.Client.model;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
public class Client {

    private Socket socket;
    private String host;
    private int port;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }


    public Object sendRequest(Object request) throws IOException, ClassNotFoundException {
        try {
            openConnection();
            // Invia la richiesta al server
            out.writeObject(request);
            // Ricevi la risposta dal server
            Object response = in.readObject();
            // Chiudi la connessione al server
            closeConnection();
            //flagPopUp = true;
            return response;
        } catch (SocketException | NullPointerException | EOFException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void openConnection() throws IOException {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.flush();
        } catch (ConnectException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Client c = new Client("127.0.0.1", 3737);
        try{
            c.openConnection();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
