package it.project.server.model;


import it.project.server.controller.ServerController;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port = 12345; // Porta di default
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newCachedThreadPool(); // Per gestire molteplici connessioni
    private boolean running = false;
    private ServerController serverController = null;

    public void setController(ServerController c){
        this.serverController = c;
    }


    public void startServer() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                running = true;
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        // Gestisci la connessione del client in un thread separato
                        pool.execute(new ClientHandler(clientSocket, serverController));
                    } catch (IOException e) {
                        if (!running) break; // Uscire se il server è stato fermato
                        System.out.println("Errore nella connessione del client: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void stopServer() throws IOException {
        running = false;
        pool.shutdown(); // Interrompe tutti i thread attivi
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public boolean isRunning() {
        return running;
    }
}