package it.project.server.model;


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

    public void setController(ServerController c){
        this.serverController = c;
    }

    public Server() {
        ServerPersistence persistence = new ServerPersistence();
        ServerLogging logging = new ServerLogging();
        this.pool = Executors.newCachedThreadPool();

        try {
            startServer();
        }catch (IOException e){
            logError();
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
                    pool.execute(new ClientHandler(clientSocket, serverController,this));
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

    //Thread accessed functions
    protected synchronized Mailbox getBox(String address){
        return loadedBoxes.computeIfAbsent(address,
            (key) -> {
                try {
                    Mailbox ret = new Mailbox(key);
                    if(ret.createOrExists()){
                        logError();
                    }
                    ret.readMailbox();
                    return ret;
                } catch (URISyntaxException e) { //TODO crash
                    //Database fault fs has been tampered
                    throw new RuntimeException(e);
                } catch (IOException e) { //TODO notify
                    //IO error notify observers
                    throw new RuntimeException(e);
                }
            }
        );
    }
}