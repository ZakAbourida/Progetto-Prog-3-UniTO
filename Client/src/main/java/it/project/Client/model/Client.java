package it.project.Client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connesso al server.");

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);


            sendMessage("Ciao Server!");


            String response = input.readLine();
            System.out.println("Server dice: " + response);


            close();

        } catch (IOException e) {
            System.out.println("Errore di connessione al server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

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
}
