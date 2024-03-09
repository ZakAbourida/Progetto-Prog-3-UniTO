package it.project.server.model;

import it.project.server.controller.ServerController;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    private ServerController serverController;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Per la demo, apriamo solo gli stream di input e output e li chiudiamo subito
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Esempio: leggi una linea di testo e inviala indietro al client
            String line = in.readLine();
            out.println("Hai detto: " + line);

            // Pulizia: chiudi gli stream e il socket
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Errore nella gestione del client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}