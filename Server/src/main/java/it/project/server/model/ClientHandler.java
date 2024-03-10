package it.project.server.model;

import it.project.server.controller.ServerController;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private ServerController serverController;

    public ClientHandler(Socket socket, ServerController controller) throws IOException {
        this.clientSocket = socket;
        this.serverController = controller;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);

    }
    @Override
    public void run() {
            try {
                // Esempio: leggi una linea di testo e inviala indietro al client
                String line = in.readLine();
                out.println("Hai detto: " + line);
                serverController.logConnection("Client connesso: "+line);

                // Pulizia: chiudi gli stream e il socket
                in.close();
                out.close();
                clientSocket.close();
                serverController.logConnection("Client disconnesso: "+line);
            } catch (IOException e) {
                System.out.println("Errore nella gestione del client: " + e.getMessage());
                e.printStackTrace();
            }

            /**
             * System.out.println("Connesso: ");
             *         while(true){
             *             try {
             *                 // Esempio: leggi una linea di testo e inviala indietro al client
             *                 String line = in.readLine();
             *                 out.println("Hai detto: " + line);
             *                 //serverController.logConnection("Client connesso: "+line);
             *
             *             } catch (IOException e) {
             *                 System.out.println("Errore nella gestione del client: " + e.getMessage());
             *                 e.printStackTrace();
             *             }
             *
             *             try {
             *                 // Pulizia: chiudi gli stream e il socket
             *                 in.close();
             *                 out.close();
             *                 clientSocket.close();
             *                 //serverController.logConnection("Client disconnesso: ");
             *             } catch (IOException e) {
             *                 System.out.println("Errore nella gestione del client: " + e.getMessage());
             *                 e.printStackTrace();
             *             }
             *         }
             */
    }
}