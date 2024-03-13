package it.project.server.model;


import it.project.lib.MailEventReceiver;
import it.project.lib.Mailbox;
import it.project.server.controller.ServerController;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port = 4040; // Porta di default
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean running = false;
    private ServerController serverController = null;
    private HashMap<String, Mailbox> loadedBoxes = new HashMap<>();
    private List<MailEventReceiver> er;

    public void setController(ServerController c){
        this.serverController = c;
    }

    public Server() {
        this.er = new ArrayList<>();
        ServerPersistence persistence = new ServerPersistence();
        ServerLogging logging = new ServerLogging();
        this.er.add(persistence);
        this.er.add(logging);
        this.pool = Executors.newCachedThreadPool();

        try {
            startServer();
        }catch (IOException e){
            for (MailEventReceiver receiver: this.er){
                receiver.handleException(e);
            }
        }
        this.running = true;
    }

    public void startServer() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    // Gestisci la connessione del client in un thread separato
                    pool.execute(new ClientHandler(clientSocket, serverController));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    this.serverSocket.close();
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

    //Thread accessible functions
    protected synchronized Mailbox getBox(String address){
        try {
             return loadedBoxes.getOrDefault(address, this.loadedBoxes.put(address ,new Mailbox(address)));
        }catch (Exception e){
            for (MailEventReceiver receiver: this.er){
                receiver.handleException(e);
            }
            return null;
        }
    }
}